package com.yogigupta1206.notesapp.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.yogigupta1206.notesapp.R
import com.yogigupta1206.notesapp.data.model.Note
import com.yogigupta1206.notesapp.databinding.NoteSingleRowBinding

class NotesAdapter(
    var listener: OnNotesClickListener
) : RecyclerView.Adapter<NotesAdapter.DataViewHolder>() {

    private var list = arrayListOf<Note>()


    inner class DataViewHolder(var binding: NoteSingleRowBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(index: Int, note: Note, listener: OnNotesClickListener) = binding.apply {
            data = note
            executePendingBindings()
            imgDelete.setOnClickListener {
                listener.onDelete(index)
            }
            imgEdit.setOnClickListener {
                listener.onEdit(index)
            }
            clRoot.setOnClickListener {
                listener.onClick(index)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DataViewHolder =
        DataViewHolder(
            DataBindingUtil.inflate(
                LayoutInflater.from(parent.context) , R.layout.note_single_row, parent, false
            )
        )

    override fun onBindViewHolder(holder: DataViewHolder, position: Int) {
        holder.bind(position, list[position], listener)
    }

    override fun getItemCount(): Int  = list.size

    interface OnNotesClickListener {
        fun onClick(index: Int)
        fun onEdit(index: Int)
        fun onDelete(index: Int)
    }
}