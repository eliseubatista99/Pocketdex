package com.eliseubatista.pocketdex.database.profile

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class DatabaseFavorite constructor(
    @PrimaryKey(autoGenerate = true)
    val id: Long,
    val name: String,
    val category: String
)