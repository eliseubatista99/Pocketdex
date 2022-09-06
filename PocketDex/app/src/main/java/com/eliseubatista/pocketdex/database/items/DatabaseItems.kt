package com.eliseubatista.pocketdex.database.items

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class DatabaseItems constructor(
    @PrimaryKey
    val id: Int,
    val cost: Int,
    val name: String,
    val attributes: List<String>,
    val category: String,
    val effects: List<String>,
    val flavor: String,
    val spriteUrl: String,
)