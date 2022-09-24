package com.example.noticeme.data

import androidx.lifecycle.LiveData
import com.example.noticeme.db.NoteDatabase
import com.example.noticeme.model.User

class UserRepository(private val userDao: UserDao) {

    fun findUser(username:String): LiveData<User>{
        return userDao.findUser(username)
    }

    fun addUser(user: User){
        NoteDatabase.databaseWriteExecutor.execute { userDao.addUser(user) }
    }

    fun updateUser(user: User){
        NoteDatabase.databaseWriteExecutor.execute { userDao.updateUser(user) }
    }

    fun  deleteUser(user: User){
        NoteDatabase.databaseWriteExecutor.execute { userDao.deleteUser(user) }
    }
}