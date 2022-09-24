package com.example.noticeme.data

import androidx.lifecycle.LiveData
import com.example.noticeme.db.NoteDatabase
import com.example.noticeme.model.User
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class UserRepository(private val userDao: UserDao) {
    private lateinit var usernameList: List<String>
    private lateinit var user: LiveData<User>

    @OptIn(DelicateCoroutinesApi::class)
    fun findUser(username:String): LiveData<User>{
        GlobalScope.launch(Dispatchers.IO){
            user = userDao.findUser(username)
        }
        return user
    }

    @OptIn(DelicateCoroutinesApi::class)
    suspend fun getAllUsername(): List<String>{
        GlobalScope.launch(Dispatchers.IO){
            usernameList = userDao.getAllUsername()
        }
        return usernameList
    }

    @OptIn(DelicateCoroutinesApi::class)
    suspend fun addUser(user: User){
        NoteDatabase.databaseWriteExecutor.execute {
            GlobalScope.launch(Dispatchers.IO) {
                userDao.addUser(user)
            }
        }
    }

    @OptIn(DelicateCoroutinesApi::class)
    suspend fun updateUser(user: User){
        NoteDatabase.databaseWriteExecutor.execute {
            GlobalScope.launch(Dispatchers.IO){
                userDao.updateUser(user)
            }
        }
    }

    @OptIn(DelicateCoroutinesApi::class)
    suspend fun  deleteUser(user: User){
        NoteDatabase.databaseWriteExecutor.execute {
            GlobalScope.launch(Dispatchers.IO){
                userDao.deleteUser(user)
            }
        }
    }
}