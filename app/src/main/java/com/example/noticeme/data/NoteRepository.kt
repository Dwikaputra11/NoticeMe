package com.example.noticeme.data

import androidx.lifecycle.LiveData
import com.example.noticeme.db.NoteDatabase
import com.example.noticeme.model.Note

class NoteRepository(private val noteDao: NoteDao) {

    fun getAllNotes(userId: Int): LiveData<List<Note>> {
        return noteDao.getAllNote(userId)
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

    fun deleteAllNotes(userId: Int){
        NoteDatabase.databaseWriteExecutor.execute{ noteDao.deleteAll(userId) }
    }

    fun searchDatabase(searchQuery: String, userId: Int): LiveData<List<Note>> {
        return noteDao.searchDatabase(searchQuery, userId)
    }
}