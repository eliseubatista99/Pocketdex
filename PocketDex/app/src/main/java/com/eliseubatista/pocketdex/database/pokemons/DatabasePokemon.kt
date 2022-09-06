package com.eliseubatista.pocketdex.database.pokemons

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class DatabasePokemon constructor(
    @PrimaryKey
    val id: Int,
    val height: Int,
    val name: String,
    val color: String,
    val evolutionChain: List<String>,
    val flavor: String,
    val genus: String,
    val spriteUrl: String,
    val hp: Int,
    val attack: Int,
    val defense: Int,
    val specialAttack: Int,
    val specialDefense: Int,
    val speed: Int,
    val types: List<String>,
    val weight: Int
)
