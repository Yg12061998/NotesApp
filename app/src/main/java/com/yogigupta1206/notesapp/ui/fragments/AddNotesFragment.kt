package com.yogigupta1206.notesapp.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.yogigupta1206.notesapp.R
import com.yogigupta1206.notesapp.databinding.FragmentAddNotesBinding
import com.yogigupta1206.notesapp.viewmodels.MainViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AddNotesFragment : Fragment() {

    private val viewModel: MainViewModel by activityViewModels()
    private lateinit var mBindings: FragmentAddNotesBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mBindings = FragmentAddNotesBinding.inflate(layoutInflater, container, false)
        return mBindings.root
    }

}