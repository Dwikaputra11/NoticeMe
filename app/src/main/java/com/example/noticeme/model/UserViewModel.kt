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

    init {
        val userDao = NoteDatabase.getDatabase(
            application
        ).userDao()
        repository = UserRepository(userDao)
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

    fun countUser(username: String): Int{
        return repository.countUser(username)
    }
}