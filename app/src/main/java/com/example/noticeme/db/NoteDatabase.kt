package com.example.noticeme.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.noticeme.data.NoteDao
import com.example.noticeme.model.Note
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

@Database(entities = [Note::class], version = 1)
abstract class NoteDatabase: RoomDatabase() {
    abstract fun noteDao(): NoteDao
    companion object{
        private const val NUMBER_OF_THREADS = 4

        @Volatile
        private var INSTANCE: NoteDatabase? = null
        val databaseWriteExecutor: ExecutorService =
            Executors.newFixedThreadPool(NUMBER_OF_THREADS) // using for creating a database but not in main thread
//     that can interrupt our ui, so we have to create database in background

        //     that can interrupt our ui, so we have to create database in background
        fun getDatabase(context: Context): NoteDatabase {
            if (INSTANCE == null) {
                synchronized(NoteDatabase::class.java) {
                    if (INSTANCE == null) {
                        INSTANCE = Room.databaseBuilder(
                            context.applicationContext,
                            NoteDatabase::class.java,
                            "note_db"
                        )
                            .addCallback(sRoomDatabaseCallback)
                            .build()
                    }
                }
            }
            return INSTANCE!!
        }

        private val sRoomDatabaseCallback: Callback = object : Callback() {
            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)
                databaseWriteExecutor.execute {
                    val contactDao: NoteDao = INSTANCE!!.noteDao()
                    contactDao.deleteAll()

//                // just add data for first time when database created
//                var contact: n? = Contact("Dwika", "Student")
//                contactDao.insert(contact)
//                contact = Contact("James Bond", "Spy")
//                contactDao.insert(contact)
                }
            }
        }
    }

}