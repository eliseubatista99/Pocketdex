package com.eliseubatista.pocketdex.fragments.items

import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.eliseubatista.pocketdex.database.profile.DatabaseFavorite
import com.eliseubatista.pocketdex.database.getDatabase
import com.eliseubatista.pocketdex.database.items.DatabaseItemCategories
import com.eliseubatista.pocketdex.database.items.DatabaseItems
import com.eliseubatista.pocketdex.repository.PocketdexRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class ItemDetailsViewModel(val application: Application, private val itemName: String) :
    ViewModel() {

    private val database = getDatabase(application)
    private val pocketdexRepository = PocketdexRepository(database)

    lateinit var itemCategory: DatabaseItemCategories

    private var _item = MutableLiveData<DatabaseItems>()
    val item: LiveData<DatabaseItems>
        get() = _item

    private var _isInFavorites = MutableLiveData<Boolean>()
    val isInFavorites: LiveData<Boolean>
        get() = _isInFavorites

    private var _isLoadingItem = MutableLiveData<Boolean>()
    val isLoadingItem: LiveData<Boolean>
        get() = _isLoadingItem

    private var coroutineJob = Job()
    private val coroutineScope = CoroutineScope(coroutineJob + Dispatchers.Main)

    init {
        getItemData()
    }

    override fun onCleared() {
        super.onCleared()
        coroutineJob.cancel()
    }

    private fun getItemData() {

        //If we are already loading and waiting for the pokemon data, do nothing
        if (_isLoadingItem.value == true) {
            return
        }

        _isLoadingItem.value = true

        coroutineScope.launch {

            val itemData =
                pocketdexRepository.itemsRepository.getItemByName(
                    application.applicationContext,
                    itemName
                )

            itemCategory =
                pocketdexRepository.itemsRepository.getItemCategoryByName(
                    application.applicationContext,
                    itemData!!.category
                )!!

            val favorite = pocketdexRepository.favoritesRepository.getFavoriteByName(itemName)

            _isInFavorites.value = favorite != null

            _item.value = itemData

            _isLoadingItem.value = false
        }
    }

    fun addOrRemoveFavorite() {

        coroutineScope.launch {
            val favoriteInDatabase =
                pocketdexRepository.favoritesRepository.getFavoriteByName(itemName)

            if (favoriteInDatabase == null) {
                Log.i("FAV", "Adding $itemName to favorites")

                val favoriteData = DatabaseFavorite(
                    0,
                    itemName,
                    "item"
                )

                pocketdexRepository.favoritesRepository.addToFavorites(favoriteData)
                _isInFavorites.value = true
            } else {
                Log.i("FAV", "Removing $itemName from favorites")

                pocketdexRepository.favoritesRepository.removeFromFavorites(favoriteInDatabase)
                _isInFavorites.value = false
            }
        }


    }

    @Suppress("UNCHECKED_CAST")
    class Factory(val application: Application, private val itemName: String) :
        ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(ItemDetailsViewModel::class.java)) {
                return ItemDetailsViewModel(application, itemName) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}