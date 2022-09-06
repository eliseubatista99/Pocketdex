package com.eliseubatista.pocketdex.network.regions

import com.eliseubatista.pocketdex.network.BaseNameAndUrl
import com.squareup.moshi.Json

/*
This is the detailed data of each pokemon specie
 */

data class LocationAreaApiData(
    @Json(name = "encounter_method_rates") val encounterMethods: List<LocationAreaEncounterMethod>,
    @Json(name = "id") val id: Int,
    @Json(name = "name") val name: String,
    @Json(name = "pokemon_encounters") val pokemonEncounters: List<LocationAreaPokemonEncounters>
)

data class LocationAreaEncounterMethod(
    @Json(name = "encounter_method") val method: BaseNameAndUrl
)

data class LocationAreaPokemonEncounters(
    @Json(name = "pokemon") val pokemon: BaseNameAndUrl
)

fun LocationAreaApiData.getPokemonEncounters(): List<String> {
    val pokemonEncounters = mutableListOf<String>()

    for (encounter in this.pokemonEncounters) {
        pokemonEncounters.add(encounter.pokemon.name)
    }

    return pokemonEncounters
}
