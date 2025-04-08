package com.example.md_stonetrack.data.db.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_table")
data class UserEntity(
    @PrimaryKey(autoGenerate = false)
    val id: Int = 1, // Фиксированный ID для единственного пользователя
    val access: String,
    val refresh: String,
    val timestamp: Long = System.currentTimeMillis()
)