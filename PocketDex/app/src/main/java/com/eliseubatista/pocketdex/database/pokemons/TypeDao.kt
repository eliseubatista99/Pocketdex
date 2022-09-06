package com.eliseubatista.pocketdex.database.pokemons

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface TypesDao {

    @Query("select * from databasetypes")
    fun getTypes(): LiveData<List<DatabaseTypes>>

    @Query("select * from databasetypes where id > :minId and id <= :maxId")
    fun getTypesInRange(minId: Int, maxId: Int): List<DatabaseTypes>

    @Query("select * from databasetypes where name = :name")
    fun getTypeByName(name: String): DatabaseTypes?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(vararg types: DatabaseTypes)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(types: List<DatabaseTypes>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(type: DatabaseTypes)
}