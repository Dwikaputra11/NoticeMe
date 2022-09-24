package com.example.noticeme.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.noticeme.data.NoteDao
import com.example.noticeme.data.UserDao
import com.example.noticeme.model.Note
import com.example.noticeme.model.User
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

@Database(entities = [Note::class, User::class], version = 1, exportSchema = false)
abstract class NoteDatabase: RoomDatabase() {
    abstract fun noteDao(): NoteDao
    abstract fun userDao(): UserDao
    companion object{
        private const val NUMBER_OF_THREADS = 4

        @Volatile
        private var INSTANCE: NoteDatabase? = null
        val databaseWriteExecutor: ExecutorService =
            Executors.newFixedThreadPool(NUMBER_OF_THREADS) // using for creating a database but not in main thread
//     that can interrupt our ui, so we have to create database in background

        //     that can interrupt our ui, so we have to create database in background
        fun getDatabase(context: Context): NoteDatabase {
            val tempInstance = INSTANCE
            if(tempInstance != null){
                return tempInstance
            }
            synchronized(NoteDatabase::class.java) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    NoteDatabase::class.java,
                    "notice_me_db"
                ).addCallback(sRoomDatabaseCallback).build()
                INSTANCE = instance
                return instance
            }
        }

        private val sRoomDatabaseCallback: Callback = object : Callback() {
            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)
                databaseWriteExecutor.execute {
//                    val noteDao: NoteDao = INSTANCE!!.noteDao()
//                    noteDao.deleteAll()

//                // just add data for first time when database created
//                var note = Note(0, "Eating", "Eating the food", "Health")
//                noteDao.insertNote(note)
//                note = Note(1, "Jogging", "Run around the home", "Sport")
//                noteDao.insertNote(note)
                }
            }
        }
    }

}