package com.eliseubatista.pocketdex.network.regions

import com.eliseubatista.pocketdex.database.regions.DatabaseLocation
import com.squareup.moshi.Json

data class LocationApiData(
    //@Json(name = "areas") val areas: List<BaseNameAndUrl>,
    @Json(name = "id") val id: Int,
    @Json(name = "name") val name: String,
)

suspend fun LocationApiData.asDatabaseModel(): DatabaseLocation {
    //val locationAreas = this.getAreas()
    //val pokemonEncounters = mutableListOf<String>()

    /*
    for (area in this.areas) {
        val locationAreasData =
            PokeApi.retrofitService.getLocationAreaByName(area.name)

        pokemonEncounters.addAll(locationAreasData.getPokemonEncounters())
    }*/

    return DatabaseLocation(
        this.id,
        this.name,
        //locationAreas,
        //pokemonEncounters
    )
}

/*
fun LocationApiData.getAreas(): List<String> {
    val locationAreas = mutableListOf<String>()

    for (area in this.areas) {
        locationAreas.add(area.name)
    }

    return locationAreas
}
*/