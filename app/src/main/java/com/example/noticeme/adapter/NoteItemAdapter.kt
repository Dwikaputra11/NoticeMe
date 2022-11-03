package com.example.noticeme.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.noticeme.databinding.NoteItemBinding
import com.example.noticeme.helper.QueryMenu
import com.example.noticeme.model.Note

class NoteItemAdapter: RecyclerView.Adapter<NoteItemAdapter.ViewHolder>() {

    private lateinit var context: Context
    private lateinit var listener: OnItemClickListener

    interface OnItemClickListener{
        fun onItemClick(menu: QueryMenu, note: Note)
    }

    fun setOnItemClickListener(listener: OnItemClickListener){
        this.listener = listener
    }

    private var diffCallback = object : DiffUtil.ItemCallback<Note>(){
        override fun areItemsTheSame(oldItem: Note, newItem: Note): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Note, newItem: Note): Boolean {
            return oldItem.hashCode() == newItem.hashCode()
        }
    }

    private val differ = AsyncListDiffer(this, diffCallback)

    inner class ViewHolder(var binding: NoteItemBinding):RecyclerView.ViewHolder(binding.root) {
        fun setData(itemData: Note){
            binding.note = itemData
        }
        init {
            binding.ivNoteEdit.setOnClickListener {
                listener.onItemClick(QueryMenu.EDIT, differ.currentList[adapterPosition])
            }
            binding.ivNoteDelete.setOnClickListener {
                listener.onItemClick(QueryMenu.DELETE,differ.currentList[adapterPosition])
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = NoteItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        context = parent.context
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        Log.d("Adapter", "onBindViewHolder: $position updated")
        holder.setData(differ.currentList[position])
//        holder.binding.ivNoteEdit.setOnClickListener {
//            val noteBottomSheetFragment = BottomSheetNoteFragment()
//            noteBottomSheetFragment.show((context as AppCompatActivity).supportFragmentManager.beginTransaction(), noteBottomSheetFragment.tag)
//        }
    }

    override fun getItemCount(): Int {
        return  differ.currentList.size
    }

    fun setNoteList(noteList: List<Note>) = differ.submitList(noteList)
}