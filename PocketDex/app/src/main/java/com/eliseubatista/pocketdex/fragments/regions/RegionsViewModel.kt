package com.eliseubatista.pocketdex.fragments.regions

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.eliseubatista.pocketdex.database.getDatabase
import com.eliseubatista.pocketdex.database.regions.DatabaseRegions
import com.eliseubatista.pocketdex.repository.PocketdexRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class RegionsViewModel(val application: Application) : ViewModel() {

    private val database = getDatabase(application)
    private val pocketdexRepository = PocketdexRepository(database)

    val regions = pocketdexRepository.regionsRepository.regions

    private var _searchedRegions = MutableLiveData<List<DatabaseRegions>>()
    val searchedRegions: LiveData<List<DatabaseRegions>>
        get() = _searchedRegions

    private var _isLoadingMoreRegions = MutableLiveData<Boolean>()
    val isLoadingMoreRegions: LiveData<Boolean>
        get() = _isLoadingMoreRegions

    private var coroutineJob = Job()
    private val coroutineScope = CoroutineScope(coroutineJob + Dispatchers.Main)

    var loadedEverything = false

    init {
        getMoreRegions()
    }

    override fun onCleared() {
        super.onCleared()
        coroutineJob.cancel()
    }


    fun getMoreRegions() {

        //If we are already loading and waiting for more pokemons, do nothing
        if (_isLoadingMoreRegions.value == true) {
            return
        }

        _isLoadingMoreRegions.value = true

        coroutineScope.launch {

            val regionsLoaded = regions.value?.size ?: 0

            //pocketdexRepository.refreshPokemons(application.applicationContext, 10, pokemonsLoaded)
            val resultsFound = pocketdexRepository.regionsRepository.getRegions(
                application.applicationContext,
                10,
                regionsLoaded
            )

            loadedEverything = !resultsFound

            _isLoadingMoreRegions.value = false
        }
    }

    fun getRegionLikeName(name: String, locallyOnly: Boolean) {
        //If we are already loading and waiting for more pokemons, do nothing
        if (_isLoadingMoreRegions.value == true) {
            return
        }

        if (!locallyOnly) {
            _isLoadingMoreRegions.value = true
        }

        _searchedRegions.value = listOf()

        coroutineScope.launch {

            _searchedRegions.value = pocketdexRepository.regionsRepository.getRegionsLikeName(
                application.applicationContext,
                name,
                locallyOnly
            )

            _isLoadingMoreRegions.value = false
        }
    }

    @Suppress("UNCHECKED_CAST")
    class Factory(val application: Application) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(RegionsViewModel::class.java)) {
                return RegionsViewModel(application) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}