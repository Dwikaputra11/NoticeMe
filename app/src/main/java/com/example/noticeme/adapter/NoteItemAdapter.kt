package com.example.noticeme.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.noticeme.model.Note
import com.example.noticeme.databinding.NoteItemBinding

class NoteItemAdapter(private var noteList: List<Note>): RecyclerView.Adapter<NoteItemAdapter.ViewHolder>() {

    class ViewHolder(var binding: NoteItemBinding):RecyclerView.ViewHolder(binding.root) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = NoteItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.tvNoteTitle.text = noteList[position].title
        holder.binding.tvNoteDesc.text = noteList[position].desc
        holder.binding.tvNoteCategory.text = noteList[position].category
    }

    override fun getItemCount(): Int {
        return  noteList.size
    }

    fun setNoteList(noteList: List<Note>){
        this.noteList = noteList
    }
}