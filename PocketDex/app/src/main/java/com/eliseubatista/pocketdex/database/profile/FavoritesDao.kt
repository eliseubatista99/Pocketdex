package com.eliseubatista.pocketdex.database.profile

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface FavoritesDao {

    @Query("select * from databasefavorite")
    fun getFavorites(): LiveData<List<DatabaseFavorite>>

    @Query("select * from databasefavorite where category = 'pokemon'")
    fun getFavoritePokemons(): LiveData<List<DatabaseFavorite>>

    @Query("select * from databasefavorite where category = 'item'")
    fun getFavoriteItems(): LiveData<List<DatabaseFavorite>>

    @Query("select * from databasefavorite where category = 'region'")
    fun getFavoriteRegions(): LiveData<List<DatabaseFavorite>>

    @Query("select * from databasefavorite where name = :name")
    fun getFavoriteByName(name: String): DatabaseFavorite?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(vararg favorites: DatabaseFavorite)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(favorites: List<DatabaseFavorite>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(favorite: DatabaseFavorite)

    @Delete
    fun delete(favorite: DatabaseFavorite)
}
