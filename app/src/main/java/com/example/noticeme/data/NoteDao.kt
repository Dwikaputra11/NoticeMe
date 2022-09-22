package com.example.noticeme.data

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.noticeme.model.Note

@Dao
interface NoteDao {
    @Query("SELECT * FROM Note ORDER BY title ASC")
    fun getAllNote():  LiveData<List<Note>>

    @Query("SELECT * FROM Note WHERE Note.id == :id")
    fun getNote(id: Int): LiveData<Note>

    @Query("DELETE FROM Note")
    fun deleteAll()

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertNote(note: Note)

    @Update
    fun updateNote(note: Note)

    @Delete
    fun deleteNote(note: Note): Int
}