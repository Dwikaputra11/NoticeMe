package com.example.noticeme.data

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.noticeme.model.User

@Dao
interface UserDao {

    @Query("SELECT * FROM user WHERE username == :username")
    // live data for when we update the user profile the user information change immediately in UI
    fun findUser(username:String): LiveData<User>

    @Query("SELECT username FROM user")
    fun getAllUsername(): List<String>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun addUser(user: User)

    @Update
    fun updateUser(user: User)

    @Delete
    fun deleteUser(user: User)
}