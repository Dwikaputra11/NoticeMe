package com.example.noticeme.ui

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.noticeme.adapter.NoteItemAdapter
import com.example.noticeme.databinding.FragmentHomeBinding
import com.example.noticeme.model.Note
import com.example.noticeme.model.NoteViewModel
import com.example.noticeme.sharedpref.SharedPref

class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    private lateinit var sharedPref: SharedPreferences
    private lateinit var noteItemAdapter: NoteItemAdapter
    private lateinit var noteVM: NoteViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        sharedPref = activity?.getSharedPreferences(SharedPref.name, Context.MODE_PRIVATE)!!
        // TODO make the right view model and integrate with the repository
        noteVM = ViewModelProvider(this)[NoteViewModel::class.java]

        binding.fabAddNote.setOnClickListener {
            val addNoteBottomSheetFragment = BottomSheetAddNoteFragment()
            addNoteBottomSheetFragment.show(activity?.supportFragmentManager?.beginTransaction()!!, addNoteBottomSheetFragment.tag)
        }
    }

    fun setView(){
        val username = sharedPref.getString(SharedPref.username, "User")
        binding.tvUsername.text = "$username,"
        noteVM.allContacts.observe(viewLifecycleOwner, Observer {
            noteItemAdapter.setNoteList(it as List<Note>)
        })
        noteItemAdapter = NoteItemAdapter(noteVM.noteList)
        val layoutManager = object: LinearLayoutManager(context){
            override fun canScrollVertically(): Boolean {
                return false
            }
        }
        binding.rvNoteList.layoutManager = layoutManager
        binding.rvNoteList.adapter = noteItemAdapter

    }

}