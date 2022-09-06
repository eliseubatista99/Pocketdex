package com.eliseubatista.pocketdex.database.items

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface ItemCategoryDao {
    @Query("select * from databaseitemcategories")
    fun getItemCategories(): LiveData<List<DatabaseItemCategories>>

    @Query("select * from databaseitemcategories where id > :minId and id <= :maxId")
    fun getItemCategoriesInRange(minId: Int, maxId: Int): List<DatabaseItemCategories>

    @Query("select * from databaseitemcategories where name = :name")
    fun getItemCategoryByName(name: String): DatabaseItemCategories?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(vararg itemCategories: DatabaseItemCategories)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(itemCategories: List<DatabaseItemCategories>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(itemCategories: DatabaseItemCategories)
}