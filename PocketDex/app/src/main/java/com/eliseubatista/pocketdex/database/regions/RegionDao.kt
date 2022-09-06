package com.eliseubatista.pocketdex.database.regions

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface RegionDao {
    @Query("select * from databaseregions")
    fun getRegions(): LiveData<List<DatabaseRegions>>

    @Query("select * from databaseregions where id > :minId and id <= :maxId")
    fun getRegionsInRange(minId: Int, maxId: Int): List<DatabaseRegions>

    @Query("select * from databaseregions where name like '%' || :name || '%'")
    fun getRegionsByName(name: String): List<DatabaseRegions>

    @Query("select * from databaseregions where name = :name")
    fun getRegionByName(name: String): DatabaseRegions?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(vararg region: DatabaseRegions)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(region: List<DatabaseRegions>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(region: DatabaseRegions)


}