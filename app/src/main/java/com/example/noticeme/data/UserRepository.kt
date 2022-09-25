package com.example.noticeme.data

import androidx.lifecycle.LiveData
import com.example.noticeme.db.NoteDatabase
import com.example.noticeme.model.User
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class UserRepository(private val userDao: UserDao) {

    fun findUser(username:String): LiveData<User> = userDao.findUser(username)

    fun countUser(username: String): Int = userDao.countExistUser(username)

    fun addUser(user: User){
        NoteDatabase.databaseWriteExecutor.execute { userDao.addUser(user)}
    }

    fun updateUser(user: User){
        NoteDatabase.databaseWriteExecutor.execute { userDao.updateUser(user) }
    }

    fun  deleteUser(user: User){
        NoteDatabase.databaseWriteExecutor.execute { userDao.deleteUser(user) }
    }
}