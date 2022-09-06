package com.eliseubatista.pocketdex.network.pokemons

import android.util.Log
import com.eliseubatista.pocketdex.network.BaseNameAndUrl
import com.eliseubatista.pocketdex.utils.isStringBlank
import com.squareup.moshi.Json

/*
This is the data of each evolution chain
 */

data class EvolutionChainData(
    @Json(name = "chain") val chain: EvolutionChainChainData?,
    @Json(name = "id") val id: Int,
)

data class EvolutionChainChainData(
    @Json(name = "evolves_to") val evolvesTo: List<EvolutionChainChainData?>,
    @Json(name = "species") val baseForm: BaseNameAndUrl,
)

fun EvolutionChainData.getEvolutionChain(): List<String> {
    val baseForm = this.chain?.baseForm!!.name
    //evolutionChain.add(this.chain?.baseForm!!.name)

    val listOfChains = getListOfChains(baseForm, this.chain.evolvesTo)

    Log.i("CHAIN", listOfChains.toString())

    return listOfChains
}

private fun getListOfChains(
    baseForm: String,
    evolutionChain: List<EvolutionChainChainData?>
): List<String> {

    val listOfChains = mutableListOf<String>()
    var chainString: String

    for (evolution1 in evolutionChain) {
        if (evolution1 == null) {
            continue
        }
        if (isStringBlank(evolution1.baseForm.name)) {
            continue
        }

        chainString = baseForm + ":" + evolution1.baseForm.name

        if (evolution1.evolvesTo.isEmpty()) {
            listOfChains.add(chainString)
        } else {
            for (evolution2 in evolution1.evolvesTo) {
                if (evolution2 == null) {
                    continue
                }

                if (isStringBlank(evolution2.baseForm.name)) {
                    continue
                }

                chainString =
                    baseForm + ":" + evolution1.baseForm.name + ":" + evolution2.baseForm.name

                listOfChains.add(chainString)
            }
        }
    }

    return listOfChains
}
