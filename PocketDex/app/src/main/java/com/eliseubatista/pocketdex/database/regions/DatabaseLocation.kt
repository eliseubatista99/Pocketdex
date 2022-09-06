package com.eliseubatista.pocketdex.database.regions

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class DatabaseLocation constructor(
    @PrimaryKey
    val id: Int,
    val name: String,
    //val areas: List<String>,
    //val pokemonEncounters: List<String>
)