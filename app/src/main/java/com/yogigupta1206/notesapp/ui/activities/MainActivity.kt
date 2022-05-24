package com.yogigupta1206.notesapp.ui.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.SearchView
import androidx.activity.viewModels
import androidx.core.view.MenuItemCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.yogigupta1206.notesapp.R
import com.yogigupta1206.notesapp.databinding.ActivityMainBinding
import com.yogigupta1206.notesapp.ui.adapter.NotesAdapter
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
        setAdapter()
    }

    private fun setClickListeners() {
        mBinding.btnAdd.setOnClickListener {
            //TODO("Not yet implemented")
        }
        mBinding.btnDeleteAll.setOnClickListener {
            //TODO("Not yet implemented")
        }
    }

    private fun setAdapter() {
        notesAdapter = NotesAdapter(object : NotesAdapter.OnNotesClickListener{
            override fun onClick(index: Int) {
                //TODO("Not yet implemented")
            }

            override fun onEdit(index: Int) {
                //TODO("Not yet implemented")
            }

            override fun onDelete(index: Int) {
                //TODO("Not yet implemented")
            }
        })

        mBinding.recyclerView.layoutManager = LinearLayoutManager(this)
        mBinding.recyclerView.adapter = notesAdapter
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

}