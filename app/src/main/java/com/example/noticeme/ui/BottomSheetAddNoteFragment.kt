package com.example.noticeme.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.noticeme.R
import com.example.noticeme.databinding.FragmentBottomSheetAddNoteBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class BottomSheetAddNoteFragment : BottomSheetDialogFragment() {

    private lateinit var binding: FragmentBottomSheetAddNoteBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentBottomSheetAddNoteBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.btnSubmit.setOnClickListener {
            addNote()
        }
    }

    fun addNote(){
        val title = binding.etInputTitle.text.toString()
        val desc = binding.etInputDesc.text.toString()
    }
}