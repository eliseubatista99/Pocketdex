package com.eliseubatista.pocketdex.fragments.profile

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.eliseubatista.pocketdex.database.getDatabase
import com.eliseubatista.pocketdex.database.items.DatabaseItems
import com.eliseubatista.pocketdex.database.pokemons.DatabasePokemon
import com.eliseubatista.pocketdex.database.regions.DatabaseRegions
import com.eliseubatista.pocketdex.repository.PocketdexRepository
import kotlinx.coroutines.*

class ProfileViewModel(val application: Application) :
    ViewModel() {

    private val database = getDatabase(application)
    private val pocketdexRepository = PocketdexRepository(database)

    val favoritePokemonsChanged = pocketdexRepository.favoritesRepository.favoritePokemons
    val favoriteItemsChanged = pocketdexRepository.favoritesRepository.favoriteItems
    val favoriteLocationsChanged = pocketdexRepository.favoritesRepository.favoriteLocations

    private var _listOfPokemons = MutableLiveData<List<DatabasePokemon>>()
    val listOfPokemons: LiveData<List<DatabasePokemon>>
        get() = _listOfPokemons

    private var _listOfItems = MutableLiveData<List<DatabaseItems>>()
    val listOfItems: LiveData<List<DatabaseItems>>
        get() = _listOfItems


    private var _listOfRegions = MutableLiveData<List<DatabaseRegions>>()
    val listOfRegions: LiveData<List<DatabaseRegions>>
        get() = _listOfRegions

    private var _isLoadingProfile = MutableLiveData<Boolean>()
    val isLoadingProfile: LiveData<Boolean>
        get() = _isLoadingProfile

    private var coroutineJob = Job()
    private val coroutineScope = CoroutineScope(coroutineJob + Dispatchers.Main)

    init {
        getAllFavorites()
    }

    override fun onCleared() {
        super.onCleared()
        coroutineJob.cancel()
    }

    private fun getAllFavorites() {
        coroutineScope.launch {
            getPokemons()
            getItems()
            getRegions()
        }
    }

    fun getPokemons() {
        val finalList = mutableListOf<DatabasePokemon>()
        val favList = favoritePokemonsChanged.value

        if (favList == null) {
            return
        }

        coroutineScope.launch {
            _isLoadingProfile.value = true

            coroutineScope.launch {

                for (fav in favList) {
                    val favDatabase = pocketdexRepository.pokedexRepository.getPokemonByName(
                        application.applicationContext,
                        fav.name
                    )
                    finalList.add(favDatabase!!)
                }

                _listOfPokemons.value = finalList
                _isLoadingProfile.value = false
            }
        }
    }

    fun getItems() {
        val finalList = mutableListOf<DatabaseItems>()
        val favList = favoriteItemsChanged.value

        if (favList == null) {
            return
        }

        coroutineScope.launch {
            _isLoadingProfile.value = true

            coroutineScope.launch {

                for (fav in favList) {
                    val favDatabase = pocketdexRepository.itemsRepository.getItemByName(
                        application.applicationContext,
                        fav.name
                    )
                    finalList.add(favDatabase!!)
                }

                _listOfItems.value = finalList
                _isLoadingProfile.value = false
            }
        }
    }

    fun getRegions() {
        val finalList = mutableListOf<DatabaseRegions>()
        val favList = favoriteLocationsChanged.value

        if (favList == null) {
            return
        }

        coroutineScope.launch {
            _isLoadingProfile.value = true

            coroutineScope.launch {

                for (fav in favList) {
                    val favDatabase = pocketdexRepository.regionsRepository.getRegionByName(
                        application.applicationContext,
                        fav.name
                    )
                    finalList.add(favDatabase!!)
                }

                _listOfRegions.value = finalList
                _isLoadingProfile.value = false
            }
        }
    }

    @Suppress("UNCHECKED_CAST")
    class Factory(val application: Application) :
        ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(ProfileViewModel::class.java)) {
                return ProfileViewModel(application) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}