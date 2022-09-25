package com.example.noticeme.model

import android.app.Application
import androidx.lifecycle.*
import com.example.noticeme.data.NoteDao
import com.example.noticeme.data.NoteRepository
import com.example.noticeme.db.NoteDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class NoteViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: NoteRepository

    init {
        val noteDao = NoteDatabase.getDatabase(
            application
        ).noteDao()
        repository = NoteRepository(noteDao)
    }
    // get all notes collection base on userId that login
    fun getAllNotes(userId: Int):LiveData<List<Note>> = repository.getAllNotes(userId)

    fun getNote(id: Int): LiveData<Note> = repository.get(id)

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

    fun deleteAllNotes(userId: Int){
        viewModelScope.launch(Dispatchers.IO){
            repository.deleteAllNotes(userId)
        }
    }

    fun searchDatabase(searchQuery: String, userId: Int): LiveData<List<Note>>{
        return repository.searchDatabase(searchQuery,userId)
    }
}