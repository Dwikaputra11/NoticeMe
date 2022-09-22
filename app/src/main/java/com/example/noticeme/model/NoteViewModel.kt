package com.example.noticeme.model

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.noticeme.data.NoteRepository

class NoteViewModel(application: Application) : ViewModel() {
    companion object{
        lateinit var repository: NoteRepository
    }
    lateinit var allContacts:LiveData<List<Note>>

    val noteList = listOf<Note>(
        Note(title = "Breakfast", desc = "Eat My Breakfast", category =  "health", id = 1),
        Note(title = "Lunch",desc =  "Eat My Lunch",  category ="health", id = 2),
        Note(title = "Dinner",desc =  "Eat My Dinner", category = "health", id = 3),
        Note(title = "Football", desc = "Playing Football", category = "sport", id = 4),
        Note(title = "Sleep", desc = "Sleeping Well", category = "health", id = 5),
    )

    init {
        repository = NoteRepository(application)
        allContacts = repository.getAllData()
    }
}