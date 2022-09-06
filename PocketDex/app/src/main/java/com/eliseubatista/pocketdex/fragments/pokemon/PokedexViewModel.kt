package com.eliseubatista.pocketdex.fragments.pokemon

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.eliseubatista.pocketdex.database.getDatabase
import com.eliseubatista.pocketdex.database.pokemons.DatabasePokemon
import com.eliseubatista.pocketdex.repository.PocketdexRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class PokedexViewModel(val application: Application) : ViewModel() {

    private val database = getDatabase(application)
    private val pocketdexRepository = PocketdexRepository(database)

    val types = pocketdexRepository.pokedexRepository.pokemonTypes
    val pokemons = pocketdexRepository.pokedexRepository.pokemons

    private var _searchedPokemons = MutableLiveData<List<DatabasePokemon>>()
    val searchedPokemons: LiveData<List<DatabasePokemon>>
        get() = _searchedPokemons

    private var _isLoadingMorePokemons = MutableLiveData<Boolean>()
    val isLoadingMorePokemons: LiveData<Boolean>
        get() = _isLoadingMorePokemons

    private var coroutineJob = Job()
    private val coroutineScope = CoroutineScope(coroutineJob + Dispatchers.Main)

    var loadedEverything = false


    init {
        getMoreTypes()
        getMorePokemons()
    }

    override fun onCleared() {
        super.onCleared()
        coroutineJob.cancel()
    }

    private fun getMoreTypes() {
        coroutineScope.launch {
            pocketdexRepository.pokedexRepository.getTypes(application.applicationContext)
        }
    }

    fun getMorePokemons() {

        //If we are already loading and waiting for more pokemons, do nothing
        if (_isLoadingMorePokemons.value == true) {
            return
        }

        _isLoadingMorePokemons.value = true

        coroutineScope.launch {

            val pokemonsLoaded = pokemons.value?.size ?: 0

            //pocketdexRepository.refreshPokemons(application.applicationContext, 10, pokemonsLoaded)
            val resultsFound = pocketdexRepository.pokedexRepository.getPokemons(
                application.applicationContext,
                10,
                pokemonsLoaded
            )

            loadedEverything = !resultsFound

            _isLoadingMorePokemons.value = false
        }
    }

    fun getPokemonLikeName(name: String, locallyOnly: Boolean) {
        //If we are already loading and waiting for more pokemons, do nothing
        if (_isLoadingMorePokemons.value == true) {
            return
        }

        if (!locallyOnly) {
            _isLoadingMorePokemons.value = true
        }

        _searchedPokemons.value = listOf()

        coroutineScope.launch {

            val pokemonsLoaded = pokemons.value?.size ?: 0

            //pocketdexRepository.refreshPokemons(application.applicationContext, 10, pokemonsLoaded)
            _searchedPokemons.value = pocketdexRepository.pokedexRepository.getPokemonsLikeName(
                application.applicationContext,
                name,
                locallyOnly
            )

            _isLoadingMorePokemons.value = false
        }
    }

    @Suppress("UNCHECKED_CAST")
    class Factory(val application: Application) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(PokedexViewModel::class.java)) {
                return PokedexViewModel(application) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}