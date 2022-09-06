package com.eliseubatista.pocketdex.network.pokemons

import com.eliseubatista.pocketdex.database.pokemons.DatabasePokemon
import com.eliseubatista.pocketdex.network.BaseNameAndUrl
import com.eliseubatista.pocketdex.network.PokeApi
import com.squareup.moshi.Json

data class PokemonData(
    @Json(name = "height") val height: Int,
    @Json(name = "id") val id: Int,
    @Json(name = "name") val name: String,
    @Json(name = "species") val species: BaseNameAndUrl,
    @Json(name = "sprites") val sprites: PokemonSpritesData,
    @Json(name = "stats") val stats: List<PokemonStatData>,
    @Json(name = "types") val types: List<PokemonTypeData>,
    @Json(name = "weight") val weight: Int,
)

/*
This is the pokemon moves data retrieved from the pokemon detailed data
 */
data class PokemonSpritesData(
    @Json(name = "front_default") val frontDefault: String?,
    @Json(name = "front_female") val frontFemale: String?,
)

/*
This is the pokemon stat data retrieved from the pokemon detailed data
 */
data class PokemonStatData(
    @Json(name = "base_stat") val statValue: Int,
    @Json(name = "stat") val stat: BaseNameAndUrl,
)

/*
This is the pokemon type data retrieved from the pokemon detailed data
 */
data class PokemonTypeData(
    @Json(name = "type") val type: BaseNameAndUrl,
)

suspend fun PokemonData.asDatabaseModel(): DatabasePokemon {
    val pokemonStats = this.getPokemonStats()
    val pokemonTypes = this.getPokemonTypes()

    val speciesData =
        PokeApi.retrofitService.getSpeciesByName(this.species.name)

    //Split the url to get the chain id
    val splitUrl = speciesData.evolutionChain.url.split("/")
    val chainID = splitUrl[splitUrl.size - 2]

    val evolutionChainData = PokeApi.retrofitService.getEvolutionChainById(chainID)

    val evolutionChain = evolutionChainData.getEvolutionChain()

    val flavor = speciesData.getFlavor()

    val genus = speciesData.getGenus()

    val sprite = this.sprites.frontDefault ?: ""

    return DatabasePokemon(
        id,
        height,
        name,
        speciesData.color.name,
        evolutionChain,
        flavor,
        genus,
        sprite,
        pokemonStats[0],
        pokemonStats[1],
        pokemonStats[2],
        pokemonStats[3],
        pokemonStats[4],
        pokemonStats[5],
        pokemonTypes,
        weight
    )
}

fun PokemonData.getPokemonStats(): List<Int> {
    val statsList = mutableListOf<Int>()

    for (stat in this.stats) {
        val statValue = stat.statValue

        statsList.add(statValue)
    }

    return statsList
}

fun PokemonData.getPokemonTypes(): List<String> {

    val pokemonTypesList = mutableListOf<String>()
    pokemonTypesList.clear()

    for (typeData in this.types) {

        if (typeData.type.name.isEmpty() || typeData.type.name.isBlank() || typeData.type.name == "") {
            continue
        }

        val type = typeData.type.name
        pokemonTypesList.add(type)
    }

    return pokemonTypesList
}