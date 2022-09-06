package com.eliseubatista.pocketdex.database.regions

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface LocationDao {

    @Query("select * from databaselocation")
    fun getLocations(): LiveData<List<DatabaseLocation>>

    @Query("select * from databaselocation where id > :minId and id <= :maxId")
    fun getLocationsInRange(minId: Int, maxId: Int): List<DatabaseLocation>

    @Query("select * from databaselocation where name = :name")
    fun getLocationByName(name: String): DatabaseLocation?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(vararg locations: DatabaseLocation)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(locations: List<DatabaseLocation>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(location: DatabaseLocation)
}
