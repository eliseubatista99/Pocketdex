package com.eliseubatista.pocketdex.database.items

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class DatabaseItemCategories constructor(
    @PrimaryKey
    val id: Int,
    val name: String
)
