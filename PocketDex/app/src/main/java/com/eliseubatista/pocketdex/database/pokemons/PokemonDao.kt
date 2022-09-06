package com.eliseubatista.pocketdex.database.pokemons

import androidx.lifecycle.LiveData
import androidx.room.*
import com.eliseubatista.pocketdex.database.pokemons.DatabasePokemon
import com.eliseubatista.pocketdex.database.pokemons.DatabaseTypes

@Dao
interface PokemonDao {

    @Query("select * from databasepokemon")
    fun getPokemons(): LiveData<List<DatabasePokemon>>

    @Query("select * from databasepokemon where id > :minId and id <= :maxId")
    fun getPokemonsInRange(minId: Int, maxId: Int): List<DatabasePokemon>

    @Query("select * from databasepokemon where name like '%' || :name || '%'")
    fun getPokemonsByName(name: String): List<DatabasePokemon>

    @Query("select * from databasepokemon where name = :name")
    fun getPokemonByName(name: String): DatabasePokemon?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(vararg pokemons: DatabasePokemon)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(pokemons: List<DatabasePokemon>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(pokemons: DatabasePokemon)
}




