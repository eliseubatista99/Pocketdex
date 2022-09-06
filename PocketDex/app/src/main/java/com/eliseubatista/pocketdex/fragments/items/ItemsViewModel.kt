package com.eliseubatista.pocketdex.fragments.items

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.eliseubatista.pocketdex.database.getDatabase
import com.eliseubatista.pocketdex.database.items.DatabaseItems
import com.eliseubatista.pocketdex.repository.PocketdexRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class ItemsViewModel(val application: Application) : ViewModel() {

    private val database = getDatabase(application)
    private val pocketdexRepository = PocketdexRepository(database)

    val categories = pocketdexRepository.itemsRepository.itemCategories
    val items = pocketdexRepository.itemsRepository.items

    private var _searchedItems = MutableLiveData<List<DatabaseItems>>()
    val searchedItems: LiveData<List<DatabaseItems>>
        get() = _searchedItems

    private var _isLoadingMoreItems = MutableLiveData<Boolean>()
    val isLoadingMoreItems: LiveData<Boolean>
        get() = _isLoadingMoreItems

    private var coroutineJob = Job()
    private val coroutineScope = CoroutineScope(coroutineJob + Dispatchers.Main)

    var loadedEverything = false


    init {
        getMoreCategories()
        getMoreItems()
    }

    override fun onCleared() {
        super.onCleared()
        coroutineJob.cancel()
    }

    private fun getMoreCategories() {
        coroutineScope.launch {
            pocketdexRepository.itemsRepository.getItemCategories(application.applicationContext)
        }
    }

    fun getMoreItems() {

        //If we are already loading and waiting for more pokemons, do nothing
        if (_isLoadingMoreItems.value == true) {
            return
        }

        _isLoadingMoreItems.value = true

        coroutineScope.launch {

            val itemsLoaded = items.value?.size ?: 0

            //pocketdexRepository.refreshPokemons(application.applicationContext, 10, pokemonsLoaded)
            val resultsFound = pocketdexRepository.itemsRepository.getItems(
                application.applicationContext,
                10,
                itemsLoaded
            )

            loadedEverything = !resultsFound

            _isLoadingMoreItems.value = false
        }
    }

    fun getItemsLikeName(name: String, locallyOnly: Boolean) {
        //If we are already loading and waiting for more pokemons, do nothing
        if (_isLoadingMoreItems.value == true) {
            return
        }

        if(!locallyOnly) {
            _isLoadingMoreItems.value = true
        }

        _searchedItems.value = listOf()

        coroutineScope.launch {

            val itemsLoaded = items.value?.size ?: 0

            _searchedItems.value = pocketdexRepository.itemsRepository.getItemsLikeName(
                application.applicationContext,
                name,
                locallyOnly
            )

            _isLoadingMoreItems.value = false
        }
    }

    @Suppress("UNCHECKED_CAST")
    class Factory(val application: Application) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(ItemsViewModel::class.java)) {
                return ItemsViewModel(application) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}