package com.example.noticeme.data

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.noticeme.db.NoteDatabase
import com.example.noticeme.model.Note

class NoteRepository(private val noteDao: NoteDao) {
    private var allContacts: LiveData<List<Note>> = noteDao.getAllNote()

    fun getAllNotes(): LiveData<List<Note>> {
        return allContacts
    }

    fun insert(note: Note) {
        NoteDatabase.databaseWriteExecutor.execute { noteDao.insertNote(note) }
    }

    fun get(id: Int): LiveData<Note> {
        return noteDao.getNote(id)
    }

    fun update(note: Note) {
        NoteDatabase.databaseWriteExecutor.execute { noteDao.updateNote(note) }
    }

    fun delete(note: Note) {
        NoteDatabase.databaseWriteExecutor.execute { noteDao.deleteNote(note) }
    }

    fun deleteAllNotes(){
        NoteDatabase.databaseWriteExecutor.execute{ noteDao.deleteAll() }
    }

    fun searchDatabase(searchQuery: String): LiveData<List<Note>> {
        return noteDao.searchDatabase(searchQuery)
    }
}