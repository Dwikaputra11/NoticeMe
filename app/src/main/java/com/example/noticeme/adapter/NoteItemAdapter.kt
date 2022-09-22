package com.example.noticeme.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.noticeme.model.Note
import com.example.noticeme.databinding.NoteItemBinding

class NoteItemAdapter(): RecyclerView.Adapter<NoteItemAdapter.ViewHolder>() {

    private var diffCallback = object : DiffUtil.ItemCallback<Note>(){
        override fun areItemsTheSame(oldItem: Note, newItem: Note): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Note, newItem: Note): Boolean {
            return oldItem.hashCode() == newItem.hashCode()
        }
    }

    private val differ = AsyncListDiffer(this, diffCallback)

    class ViewHolder(var binding: NoteItemBinding):RecyclerView.ViewHolder(binding.root) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = NoteItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        Log.d("Adapter", "onBindViewHolder: $position updated")
        holder.binding.tvNoteTitle.text = differ.currentList[position].title
        holder.binding.tvNoteDesc.text = differ.currentList[position].title
        holder.binding.tvNoteCategory.text = differ.currentList[position].title
    }

    override fun getItemCount(): Int {
        return  differ.currentList.size
    }

    fun setNoteList(noteList: List<Note>) = differ.submitList(noteList)
}