package com.yogigupta1206.notesapp.ui.activities

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.SearchView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.MenuItemCompat
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.yogigupta1206.notesapp.R
import com.yogigupta1206.notesapp.data.model.Note
import com.yogigupta1206.notesapp.databinding.ActivityMainBinding
import com.yogigupta1206.notesapp.ui.adapter.NotesAdapter
import com.yogigupta1206.notesapp.ui.fragments.AddNotesFragment
import com.yogigupta1206.notesapp.utils.*
import com.yogigupta1206.notesapp.viewmodels.MainViewModel
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var mBinding: ActivityMainBinding
    private val viewModel: MainViewModel by viewModels()
    private var notesAdapter: NotesAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mBinding.root)

        setClickListeners()
        setObservers()
        setAdapter()
        viewModel.init()
    }

    private fun setObservers() {
        viewModel.uiState.observe(this) {
            when (it) {
                is MainViewModel.UiState.Loading -> {
                    mBinding.progress.show()
                }
                is MainViewModel.UiState.LoadingFinish -> {
                    mBinding.progress.hide()
                }
                is MainViewModel.UiState.ErrorState -> {
                    mBinding.txtNoData.show()
                    mBinding.btnDeleteAll.hide()
                }
            }
        }

        viewModel.notesDataQuery.observe(this){
            when(it){
                MainViewModel.NotesDataQuery.DeleteAllData -> {
                    notesAdapter?.deleteAll()
                }
                is MainViewModel.NotesDataQuery.AddAllData -> {
                    mBinding.txtNoData.hide()
                    mBinding.btnDeleteAll.show()
                    notesAdapter?.submitList(it.list)
                }
                is MainViewModel.NotesDataQuery.DeleteDataAtPosition -> {
                    notesAdapter?.deletePosition(it.index)
                }
                is MainViewModel.NotesDataQuery.AddData -> {
                    mBinding.txtNoData.hide()
                    mBinding.btnDeleteAll.show()
                    notesAdapter?.addData(it.note)
                    notesAdapter?.notifyDataSetChanged()
                }

                is MainViewModel.NotesDataQuery.UpdateDataAtPosition -> {
                    notesAdapter?.updatePosition(it.index, it.note)
                    notesAdapter?.notifyDataSetChanged()
                }
            }
        }

    }

    private fun setClickListeners() {
        mBinding.btnAdd.setOnClickListener {
            navigateToAddUpdateFragment(purpose = FOR_ADDING_DATA)
        }

        mBinding.btnDeleteAll.setOnClickListener {
            viewModel.deleteAllData()
        }
    }

    private fun setAdapter() {
        notesAdapter = NotesAdapter(object : NotesAdapter.OnNotesClickListener{
            override fun onClick(index: Int, note: Note) {
                Toast.makeText(this@MainActivity, "$index", Toast.LENGTH_SHORT).show()
            }

            override fun onEdit(index: Int, note: Note) {
                navigateToAddUpdateFragment(FOR_UPDATING_DATA, index ,note)
            }

            override fun onDelete(index: Int, note: Note) {
                viewModel.deleteData(index, note)
            }
        })

        mBinding.recyclerView.layoutManager = LinearLayoutManager(this)
        mBinding.recyclerView.adapter = notesAdapter

        val itemTouchHelper = ItemTouchHelper(simpleCallback)
        itemTouchHelper.attachToRecyclerView(mBinding.recyclerView)
    }

    private fun navigateToAddUpdateFragment(purpose: Int, index:Int = 0, note: Note? = null) {

        val fragment = AddNotesFragment()
        val bundle = Bundle()
        bundle.putInt(ADD_UPDATE_CALLED_FOR, purpose)

        if(purpose == FOR_UPDATING_DATA){
            bundle.putInt(INDEX, index)
            note?.let {
                bundle.putInt(NOTE_ID, it.noteId)
                bundle.putString(TITLE, it.title)
                bundle.putString(DESCRIPTION, it.description)
            }
        }

        fragment.arguments = bundle

        addReplaceFragment(R.id.container, fragment ,
            addFragment = true,
            addToBackStack = true
        )
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.menu, menu)
        val searchViewItem: MenuItem =
            menu.findItem(R.id.app_bar_search)
        val searchView: SearchView = MenuItemCompat.getActionView(searchViewItem) as SearchView
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                filterData(newText)
                return false
            }
        })
        return super.onCreateOptionsMenu(menu)
    }

    private fun filterData(text: String?) {
        val filteredList = viewModel.filter(text.toString())
        mBinding.recyclerView.layoutManager?.scrollToPosition(0)
        notesAdapter?.submitList(filteredList)
    }

    var simpleCallback: ItemTouchHelper.SimpleCallback = object : ItemTouchHelper.SimpleCallback(
        ItemTouchHelper.UP or ItemTouchHelper.DOWN or ItemTouchHelper.START or ItemTouchHelper.END,
        0
    ) {
        override fun onMove(
            recyclerView: RecyclerView,
            viewHolder: RecyclerView.ViewHolder,
            target: RecyclerView.ViewHolder
        ): Boolean {
            val fromPosition = viewHolder.adapterPosition
            val toPosition = target.adapterPosition
            viewModel.updateDataAfterDrag(fromPosition, toPosition)
            notesAdapter?.updatePositionAfterDrag(fromPosition, toPosition)
            //recyclerView.adapter!!.notifyItemMoved(fromPosition, toPosition)
            return false
        }

        override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {}
    }

}