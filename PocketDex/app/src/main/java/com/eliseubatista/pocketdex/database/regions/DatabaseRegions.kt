package com.eliseubatista.pocketdex.database.regions

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class DatabaseRegions constructor(
    @PrimaryKey
    val id: Int,
    val name: String,
    val locations: List<String>
)
