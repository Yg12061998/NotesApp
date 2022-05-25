package com.yogigupta1206.notesapp.ui.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.yogigupta1206.notesapp.R
import com.yogigupta1206.notesapp.data.model.Note
import com.yogigupta1206.notesapp.databinding.NoteSingleRowBinding
import java.util.*

class NotesAdapter(
    var listener: OnNotesClickListener
) : RecyclerView.Adapter<NotesAdapter.DataViewHolder>() {

    private var list = arrayListOf<Note>()


    inner class DataViewHolder(var binding: NoteSingleRowBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(index: Int, note: Note, listener: OnNotesClickListener) = binding.apply {
            data = note
            executePendingBindings()
            imgDelete.setOnClickListener {
                listener.onDelete(index, note)
            }
            imgEdit.setOnClickListener {
                listener.onEdit(index, note)
            }
            imgShare.setOnClickListener {
                listener.onShare(note)
            }
            clRoot.setOnClickListener {
                listener.onClick(index, note)
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

    @SuppressLint("NotifyDataSetChanged")
    fun submitList(newList: MutableList<Note>){
        list.clear()
        list.addAll(newList)
        notifyDataSetChanged()
    }

    @SuppressLint("NotifyDataSetChanged")
    fun deletePosition(index: Int){
        list.removeAt(index)
        notifyItemRemoved(index)
        notifyDataSetChanged()
    }

    @SuppressLint("NotifyDataSetChanged")
    fun deleteAll(){
        list.clear()
        notifyDataSetChanged()
    }

    fun updatePosition(index: Int, note: Note){
        list.removeAt(index)
        notifyItemRemoved(index)
        addData(note)
    }

    fun addData(note: Note){
        list.add(0, note)
        notifyItemInserted(0)
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updatePositionAfterDrag(fromPosition: Int, toPosition: Int) {
        Collections.swap(list, fromPosition, toPosition)
        notifyItemMoved(fromPosition, toPosition)
    }

    override fun getItemCount(): Int  = list.size

    interface OnNotesClickListener {
        fun onClick(index: Int, note: Note)
        fun onEdit(index: Int, note: Note)
        fun onDelete(index: Int, note: Note)
        fun onShare(note: Note)
    }
}