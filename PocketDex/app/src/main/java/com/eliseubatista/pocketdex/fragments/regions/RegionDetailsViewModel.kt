package com.eliseubatista.pocketdex.fragments.regions

import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.eliseubatista.pocketdex.database.profile.DatabaseFavorite
import com.eliseubatista.pocketdex.database.getDatabase
import com.eliseubatista.pocketdex.database.regions.DatabaseLocation
import com.eliseubatista.pocketdex.database.regions.DatabaseRegions
import com.eliseubatista.pocketdex.repository.PocketdexRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class RegionDetailsViewModel(val application: Application, private val regionName: String) :
    ViewModel() {

    private val database = getDatabase(application)
    private val pocketdexRepository = PocketdexRepository(database)

    private var _locations = MutableLiveData<List<DatabaseLocation>>()
    val locations: LiveData<List<DatabaseLocation>>
        get() = _locations

    private var _region = MutableLiveData<DatabaseRegions>()
    val region: LiveData<DatabaseRegions>
        get() = _region

    private var _isInFavorites = MutableLiveData<Boolean>()
    val isInFavorites: LiveData<Boolean>
        get() = _isInFavorites

    private var _isLoadingRegion = MutableLiveData<Boolean>()
    val isLoadingRegion: LiveData<Boolean>
        get() = _isLoadingRegion

    private var coroutineJob = Job()
    private val coroutineScope = CoroutineScope(coroutineJob + Dispatchers.Main)

    init {
        getRegionData()
    }

    override fun onCleared() {
        super.onCleared()
        coroutineJob.cancel()
    }

    private fun getRegionData() {

        //If we are already loading and waiting for the pokemon data, do nothing
        if (_isLoadingRegion.value == true) {
            return
        }

        _isLoadingRegion.value = true

        coroutineScope.launch {

            val regionData =
                pocketdexRepository.regionsRepository.getRegionByName(
                    application.applicationContext,
                    regionName
                )

            val regionLocations = mutableListOf<DatabaseLocation>()

            for (location in regionData!!.locations) {

                val locationData = pocketdexRepository.regionsRepository.getLocationByName(
                    application.applicationContext,
                    location
                )

                regionLocations.add(locationData!!)
            }

            val favorite = pocketdexRepository.favoritesRepository.getFavoriteByName(regionName)

            _isInFavorites.value = favorite != null

            _locations.value = regionLocations.toList()

            _region.value = regionData

            _isLoadingRegion.value = false
        }
    }

    fun addOrRemoveFavorite() {

        coroutineScope.launch {
            val favoriteInDatabase =
                pocketdexRepository.favoritesRepository.getFavoriteByName(regionName)

            if (favoriteInDatabase == null) {
                Log.i("FAV", "Adding $regionName to favorites")

                val favoriteData = DatabaseFavorite(
                    0,
                    regionName,
                    "region"
                )

                pocketdexRepository.favoritesRepository.addToFavorites(favoriteData)
                _isInFavorites.value = true
            } else {
                Log.i("FAV", "Removing $regionName from favorites")

                pocketdexRepository.favoritesRepository.removeFromFavorites(favoriteInDatabase)
                _isInFavorites.value = false
            }
        }


    }

    @Suppress("UNCHECKED_CAST")
    class Factory(val application: Application, private val regionName: String) :
        ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(RegionDetailsViewModel::class.java)) {
                return RegionDetailsViewModel(application, regionName) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}