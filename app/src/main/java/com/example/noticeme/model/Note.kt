@file:Suppress("unused", "unused", "unused", "unused", "unused", "unused", "unused", "unused",
    "unused"
)

package com.example.noticeme.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Suppress("unused", "unused", "unused", "unused", "unused", "unused")
@Entity
data class Note(
    @PrimaryKey(autoGenerate = true)
    var id: Int,
    @ColumnInfo
    var user_id: Int,
    @ColumnInfo
    var title: String,
    @ColumnInfo
    var desc: String,
    @ColumnInfo
    var category: String,
)
