package com.example.noticeme.model

import android.app.Application
import androidx.lifecycle.*
import com.example.noticeme.data.NoteDao
import com.example.noticeme.data.NoteRepository
import com.example.noticeme.db.NoteDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class NoteViewModel(application: Application) : AndroidViewModel(application) {

    private val readAllData:LiveData<List<Note>>
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

    fun searchDatabase(searchQuery: String): LiveData<List<Note>>{
        return repository.searchDatabase(searchQuery)
    }
}