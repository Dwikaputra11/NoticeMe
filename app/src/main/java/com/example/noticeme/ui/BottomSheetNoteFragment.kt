package com.example.noticeme.ui

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.example.noticeme.R
import com.example.noticeme.databinding.FragmentBottomSheetAddNoteBinding
import com.example.noticeme.helper.QueryMenu
import com.example.noticeme.model.Note
import com.example.noticeme.model.NoteViewModel
import com.example.noticeme.sharedpref.SharedPref
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class BottomSheetNoteFragment(private var menu: QueryMenu,private var updateNote: Note?) : BottomSheetDialogFragment() {

    private lateinit var binding: FragmentBottomSheetAddNoteBinding
    private lateinit var rbSelected: RadioButton
    private lateinit var noteVM: NoteViewModel
    private lateinit var sharedPref: SharedPreferences

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentBottomSheetAddNoteBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        sharedPref = activity?.getSharedPreferences(SharedPref.name, Context.MODE_PRIVATE)!!
        noteVM = ViewModelProvider(this)[NoteViewModel::class.java]
        if(menu == QueryMenu.EDIT){
            binding.etInputTitle.setText(updateNote?.title)
            binding.etInputDesc.setText(updateNote?.desc)
            setRadioButton()
        }
        binding.btnSubmit.setOnClickListener {
            when(menu){
                QueryMenu.ADD -> addNote()
                QueryMenu.EDIT -> editNote()
                else -> {}
            }
        }
    }

    // edit success to update database
    private fun editNote() {
        Log.d("Bottom Sheet", "editNote: Started")
        val title = binding.etInputTitle.text.toString()
        val desc = binding.etInputDesc.text.toString()
        val selectedId = binding.rgCategory.checkedRadioButtonId
        rbSelected = binding.root.findViewById(selectedId)
        val category = rbSelected.text.toString()

        if(isValid(title, desc, category)){
            val note = Note(
                id = updateNote!!.id,
                user_id = updateNote!!.user_id,
                title = title,
                desc = desc,
                category = category
            )
            noteVM.updateNote(note)
            toastMessage("Successfully Update!")
            dismiss()
        }else{
            toastMessage("Please fill out all fields.")
        }
    }

    private fun setRadioButton(){
        val selectedId = when (updateNote?.category){
            binding.rbSport.text.toString() -> R.id.rbSport
            binding.rbEducation.text.toString() -> R.id.rbEducation
            binding.rbHealth.text.toString() -> R.id.rbHealth
            binding.rbHealing.text.toString() -> R.id.rbHealing
            else -> 0
        }
        Log.d("Bottom Sheet", "setRadioButton: $selectedId")
        binding.rgCategory.check(selectedId)
    }

    private fun addNote(){
        val title = binding.etInputTitle.text.toString()
        val desc = binding.etInputDesc.text.toString()
        val selectedId = binding.rgCategory.checkedRadioButtonId
        rbSelected = binding.root.findViewById(selectedId)
        val category = rbSelected.text.toString()
        val userId = sharedPref.getInt(SharedPref.userId, -1)

        if(isValid(title, desc, category)){
            val note = Note(
                id = 0,
                user_id = userId,
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