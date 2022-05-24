package com.yogigupta1206.notesapp.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.yogigupta1206.notesapp.data.model.Note
import com.yogigupta1206.notesapp.databinding.FragmentAddNotesBinding
import com.yogigupta1206.notesapp.utils.*
import com.yogigupta1206.notesapp.viewmodels.MainViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AddNotesFragment : Fragment() {

    private lateinit var mBinding: FragmentAddNotesBinding
    private val viewModel: MainViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mBinding = FragmentAddNotesBinding.inflate(inflater, container, false)
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setObservers()
        setClickListeners()

        arguments?.let {
            if(it.getInt(ADD_UPDATE_CALLED_FOR) == FOR_UPDATING_DATA){
                val note = Note(
                    it.getInt(NOTE_ID),
                    it.getString(TITLE),
                    it.getString(DESCRIPTION)
                )
                viewModel.initAddUpdateFragment(FOR_UPDATING_DATA, it.getInt(INDEX) ,note )
            }
            else{
                viewModel.initAddUpdateFragment(FOR_ADDING_DATA)
            }

        }
    }

    private fun setObservers() {
        viewModel.addOrRemoveFragmentEvent.observe(viewLifecycleOwner){
            when(it){
                is MainViewModel.AddOrRemoveFragmentEvent.Init -> {
                    mBinding.edtTitle.editText?.setText(it.note.title)
                    mBinding.edtDescription.editText?.setText(it.note.description)
                }
                MainViewModel.AddOrRemoveFragmentEvent.CloseFragment -> {
                    viewModel.setNullEvent()
                    activity?.supportFragmentManager?.popBackStack()
                }
                null -> {}
            }
        }
    }

    private fun setClickListeners() {
        mBinding.btnSave.setOnClickListener {
            viewModel.saveData(mBinding.edtTitle.editText?.text.toString(), mBinding.edtDescription.editText?.text.toString())
        }
        mBinding.btnCancel.setOnClickListener {
            viewModel.closeFragment()
        }
    }

}