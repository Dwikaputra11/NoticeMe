package com.example.noticeme.data

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.noticeme.model.Note

@Dao
interface NoteDao {
    @Query("SELECT * FROM Note WHERE user_id == :userId ORDER BY title ASC")
    fun getAllNote(userId: Int):  LiveData<List<Note>>

    @Query("SELECT * FROM Note WHERE Note.id == :id")
    fun getNote(id: Int): LiveData<Note>

    @Query("SELECT * FROM Note WHERE title LIKE :searchQuery AND user_id == :userId")
    fun searchDatabase(searchQuery: String,userId: Int): LiveData<List<Note>>

    @Query("DELETE FROM Note WHERE user_id == :userId")
    fun deleteAll(userId: Int)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertNote(note: Note)

    @Update
    fun updateNote(note: Note)

    @Delete
    fun deleteNote(note: Note): Int
}