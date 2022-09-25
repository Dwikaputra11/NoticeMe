package com.example.noticeme.data

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.noticeme.model.User

@Dao
interface UserDao {

    @Query("SELECT * FROM user WHERE username LIKE :username")
    // live data for when we update the user profile the user information change immediately in UI
    fun findUser(username:String): LiveData<User>

    @Query("SELECT COUNT() FROM user WHERE username == :username")
    fun countExistUser(username: String): Int

    @Insert
    fun addUser(user: User)

    @Update
    fun updateUser(user: User)

    @Delete
    fun deleteUser(user: User)
}