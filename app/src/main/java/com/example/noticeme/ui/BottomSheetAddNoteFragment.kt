package com.example.noticeme.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import com.example.noticeme.R
import com.example.noticeme.databinding.FragmentBottomSheetAddNoteBinding
import com.example.noticeme.model.Note
import com.example.noticeme.model.NoteViewModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class BottomSheetAddNoteFragment : BottomSheetDialogFragment() {

    // TODO: add note to the database and update the ui

    private lateinit var binding: FragmentBottomSheetAddNoteBinding
    private lateinit var rbSelected: RadioButton
    private lateinit var noteVM: NoteViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentBottomSheetAddNoteBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        noteVM = ViewModelProvider(this)[NoteViewModel::class.java]
        binding.btnSubmit.setOnClickListener {
            addNote()
        }
    }

    private fun addNote(){
        val title = binding.etInputTitle.text.toString()
        val desc = binding.etInputDesc.text.toString()
        val selectedId = binding.rgCategory.checkedRadioButtonId
        rbSelected = binding.root.findViewById(selectedId)
        val category = rbSelected.text.toString()


        if(isValid(title, desc, category)){
            val note = Note(
                id = 0,
                title = title,
                desc = desc,
                category = category
            )
            noteVM.addNote(note)
            toastMessage("Successfully added!")
            dismiss()
        }else{
            toastMessage("Please fill out all fields.")
        }
    }

    private fun toastMessage(msg: String){
        Toast.makeText(context, msg, Toast.LENGTH_LONG).show()
    }


    private fun isValid(title: String, desc: String, category: String): Boolean{
        return (title.isNotEmpty() && desc.isNotEmpty() && category.isNotEmpty())
    }
}