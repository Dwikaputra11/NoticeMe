package com.example.noticeme.model

import android.app.Application
import androidx.lifecycle.*
import com.example.noticeme.data.NoteDao
import com.example.noticeme.data.NoteRepository
import com.example.noticeme.db.NoteDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class NoteViewModel(application: Application) : AndroidViewModel(application) {

    val readAllData:LiveData<List<Note>>
    private val repository: NoteRepository

    init {
        val noteDao = NoteDatabase.getDatabase(
            application
        ).noteDao()
        repository = NoteRepository(noteDao)
        readAllData = repository.getAllNotes()
    }

    fun getAllNotes():LiveData<List<Note>> = readAllData

    fun addNote(note: Note){
        viewModelScope.launch(Dispatchers.IO) {
            repository.insert(note)
        }
    }

    fun updateNote(note: Note){
        viewModelScope.launch(Dispatchers.IO) {
            repository.update(note)
        }
    }

    fun deleteNote(note: Note){
        viewModelScope.launch(Dispatchers.IO){
            repository.delete(note)
        }
    }

    fun deleteAllNotes(){
        viewModelScope.launch(Dispatchers.IO){
            repository.deleteAllNotes()
        }
    }

//    val noteList = listOf<Note>(
//        Note(title = "Breakfast", desc = "Eat My Breakfast", category =  "health", id = 1),
//        Note(title = "Lunch",desc =  "Eat My Lunch",  category ="health", id = 2),
//        Note(title = "Dinner",desc =  "Eat My Dinner", category = "health", id = 3),
//        Note(title = "Football", desc = "Playing Football", category = "sport", id = 4),
//        Note(title = "Sleep", desc = "Sleeping Well", category = "health", id = 5),
//    )
//
//    var noteListLiveData: MutableLiveData<List<Note>> = MutableLiveData()
//
//    fun getLiveData(){
//        noteListLiveData.value = noteList
//    }
}