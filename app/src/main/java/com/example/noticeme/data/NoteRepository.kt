package com.example.noticeme.data

import android.app.Application
import androidx.lifecycle.LiveData
import com.example.noticeme.db.NoteDatabase
import com.example.noticeme.model.Note

class NoteRepository(application: Application) {
    private var noteDao: NoteDao? = null
    private lateinit var allContacts: LiveData<List<Note>>

    init {
        val db: NoteDatabase = NoteDatabase.getDatabase(application)
        noteDao = db.noteDao()
        allContacts = noteDao!!.getAllNote()
    }

    fun getAllData(): LiveData<List<Note>> {
        return allContacts
    }

    fun insert(note: Note) {
        NoteDatabase.databaseWriteExecutor.execute { noteDao!!.insertNote(note) }
    }

    fun get(id: Int): LiveData<Note> {
        return noteDao!!.getNote(id)
    }

    fun update(note: Note) {
        NoteDatabase.databaseWriteExecutor.execute { noteDao!!.updateNote(note) }
    }

    fun delete(note: Note) {
        NoteDatabase.databaseWriteExecutor.execute { noteDao!!.deleteNote(note) }
    }
}