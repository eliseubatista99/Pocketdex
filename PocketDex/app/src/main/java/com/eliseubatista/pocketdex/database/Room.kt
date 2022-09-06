package com.eliseubatista.pocketdex.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.eliseubatista.pocketdex.database.profile.DatabaseFavorite
import com.eliseubatista.pocketdex.database.profile.FavoritesDao
import com.eliseubatista.pocketdex.database.items.DatabaseItemCategories
import com.eliseubatista.pocketdex.database.items.DatabaseItems
import com.eliseubatista.pocketdex.database.items.ItemCategoryDao
import com.eliseubatista.pocketdex.database.items.ItemDao
import com.eliseubatista.pocketdex.database.regions.DatabaseLocation
import com.eliseubatista.pocketdex.database.regions.DatabaseRegions
import com.eliseubatista.pocketdex.database.regions.LocationDao
import com.eliseubatista.pocketdex.database.regions.RegionDao
import com.eliseubatista.pocketdex.database.pokemons.DatabasePokemon
import com.eliseubatista.pocketdex.database.pokemons.DatabaseTypes
import com.eliseubatista.pocketdex.database.pokemons.PokemonDao
import com.eliseubatista.pocketdex.database.pokemons.TypesDao

@Database(
    entities = [DatabaseFavorite::class, DatabasePokemon::class, DatabaseTypes::class, DatabaseItems::class,
        DatabaseItemCategories::class, DatabaseRegions::class, DatabaseLocation::class],
    version = 1
)
@TypeConverters(DatabaseTypeConverters::class)
abstract class PocketdexDatabase : RoomDatabase() {
    abstract val favoritesDao: FavoritesDao
    abstract val pokemonDao: PokemonDao
    abstract val typesDao: TypesDao
    abstract val itemsDao: ItemDao
    abstract val itemCategoriesDao: ItemCategoryDao
    abstract val regionDao: RegionDao
    abstract val locationDao: LocationDao
}

private lateinit var INSTANCE: PocketdexDatabase


fun getDatabase(context: Context): PocketdexDatabase {
    synchronized(PocketdexDatabase::class.java) {
        if (!::INSTANCE.isInitialized) {
            INSTANCE = Room.databaseBuilder(
                context.applicationContext,
                PocketdexDatabase::class.java,
                "pocketdex"
            ).build()
        }
    }
    return INSTANCE
}