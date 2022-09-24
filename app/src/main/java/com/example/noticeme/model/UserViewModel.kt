package com.example.noticeme.model

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.noticeme.data.UserRepository
import com.example.noticeme.db.NoteDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class UserViewModel(application: Application): AndroidViewModel(application) {
    private val repository: UserRepository
    private lateinit var userAccount: LiveData<User>
    private lateinit var allUsername: List<String>

    init {
        val userDao = NoteDatabase.getDatabase(
            application
        ).userDao()
        repository = UserRepository(userDao)
    }

    fun getUser(username:String): LiveData<User>{
        userAccount = findUser(username)
        return userAccount
    }

    suspend fun getAllUsername(): List<String>{
        viewModelScope.launch(Dispatchers.IO) {
            allUsername = repository.getAllUsername()
        }
        return allUsername
    }

    fun addUser(user: User){
        viewModelScope.launch(Dispatchers.IO){ repository.addUser(user) }
    }

    fun updateUser(user: User){
        viewModelScope.launch(Dispatchers.IO){ repository.updateUser(user) }
    }

    fun deleteUser(user: User){
        viewModelScope.launch(Dispatchers.IO){ repository.deleteUser(user) }
    }

    fun findUser(username:String): LiveData<User>{
        return repository.findUser(username)
    }
}