package com.example.noticeme.data

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.noticeme.model.User

@Dao
interface UserDao {

    @Query("SELECT * FROM user WHERE username == :username")
    fun findUser(username:String): LiveData<User>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun addUser(user: User)

    @Update
    fun updateUser(user: User)

    @Delete
    fun deleteUser(user: User)
}