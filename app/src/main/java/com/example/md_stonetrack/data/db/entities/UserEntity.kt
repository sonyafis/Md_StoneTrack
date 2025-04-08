package com.example.md_stonetrack.data.db.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_table")
data class UserEntity(
    @PrimaryKey(autoGenerate = false)
    val id: Int = 1, // Фиксированный ID для единственного пользователя
    val name: String,
    val email: String,
    val first_name: String? = null,  // Новый параметр для имени
    val last_name: String? = null,   // Новый параметр для фамилии
    val phone_number: String? = null, // Новый параметр для номера телефона
    val type_user: String? = null,   // Новый параметр для типа пользователя
    val timestamp: Long = System.currentTimeMillis()
)