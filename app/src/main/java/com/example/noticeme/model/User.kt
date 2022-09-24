package com.example.noticeme.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class User(
    @PrimaryKey(autoGenerate = true)
    var id: Int,
    @ColumnInfo
    var username: String,
    @ColumnInfo
    var fullname: String,
    @ColumnInfo
    var password: String
)
