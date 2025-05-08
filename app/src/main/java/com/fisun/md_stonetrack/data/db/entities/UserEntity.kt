package com.fisun.md_stonetrack.data.db.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_table")
data class UserEntity(
    @PrimaryKey(autoGenerate = false)
    val id: Int,
    val name: String,
    val email: String,
    val first_name: String? = null,
    val last_name: String? = null,
    val phone_number: String? = null,
    val type_user: String? = null,
    val timestamp: Long = System.currentTimeMillis()
)