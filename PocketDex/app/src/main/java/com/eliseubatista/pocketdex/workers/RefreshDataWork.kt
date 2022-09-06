package com.eliseubatista.pocketdex.workers

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.eliseubatista.pocketdex.database.getDatabase
import com.eliseubatista.pocketdex.repository.PocketdexRepository

class RefreshDataWorker(
    private val appContext: Context,
    params: WorkerParameters
) :
    CoroutineWorker(appContext, params) {

    override suspend fun doWork(): Result {
        val database = getDatabase(applicationContext)
        val repository = PocketdexRepository(database)

        return try {
            repository.pokedexRepository.refreshTypes(appContext)
            repository.itemsRepository.refreshItemCategories(appContext)
            repository.regionsRepository.refreshRegions(appContext)

            repository.pokedexRepository.refreshPokemons(appContext)
            repository.itemsRepository.refreshItems(appContext)
            repository.regionsRepository.refreshLocations(appContext)


            Result.success()
        } catch (exception: Exception) {
            Result.retry()
        }
    }

}