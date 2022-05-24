package com.yogigupta1206.notesapp.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.yogigupta1206.notesapp.databinding.FragmentAddNotesBinding
import com.yogigupta1206.notesapp.viewmodels.MainViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AddNotesFragment : Fragment() {

    private val viewModel: MainViewModel by activityViewModels()
    private lateinit var mBinding: FragmentAddNotesBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mBinding = FragmentAddNotesBinding.inflate(layoutInflater, container, false)
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setClickListeners()
    }

    private fun setClickListeners() {
        mBinding.btnSave.setOnClickListener {
            //TODO("Not yet implemented")
        }
        mBinding.btnCancel.setOnClickListener {
            //TODO("Not yet implemented")
        }
    }

}