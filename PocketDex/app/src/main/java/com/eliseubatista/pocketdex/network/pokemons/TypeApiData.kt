package com.eliseubatista.pocketdex.network.pokemons

import com.eliseubatista.pocketdex.database.pokemons.DatabaseTypes
import com.eliseubatista.pocketdex.network.BaseNameAndUrl
import com.eliseubatista.pocketdex.network.getNames
import com.squareup.moshi.Json

/*
This is the detailed data of each pokemon type
 */

data class TypeData(
    @Json(name = "damage_relations") val damageRelations: TypeDamageRelationsData,
    @Json(name = "id") val id: Int,
    @Json(name = "name") val name: String,
)

/*
This is the type damage relations data retrieved from the type detailed data
 */
data class TypeDamageRelationsData(
    @Json(name = "double_damage_from") val doubleDamageFrom: List<BaseNameAndUrl>,
    @Json(name = "double_damage_to") val doubleDamageTo: List<BaseNameAndUrl>,
    @Json(name = "half_damage_from") val halfDamageFrom: List<BaseNameAndUrl>,
    @Json(name = "half_damage_to") val halfDamageTo: List<BaseNameAndUrl>,
    @Json(name = "no_damage_from") val noDamageFrom: List<BaseNameAndUrl>,
    @Json(name = "no_damage_to") val noDamageTo: List<BaseNameAndUrl>,
)

fun TypeData.asDatabaseModel(): DatabaseTypes {

    val doubleDamageFrom = this.damageRelations.doubleDamageFrom.getNames()
    val doubleDamageTo = this.damageRelations.doubleDamageTo.getNames()
    val halfDamageFrom = this.damageRelations.halfDamageFrom.getNames()
    val halfDamageTo = this.damageRelations.halfDamageTo.getNames()
    val noDamageFrom = this.damageRelations.noDamageFrom.getNames()
    val noDamageTo = this.damageRelations.noDamageTo.getNames()

    return DatabaseTypes(
        id,
        doubleDamageFrom,
        doubleDamageTo,
        halfDamageFrom,
        halfDamageTo,
        noDamageFrom,
        noDamageTo,
        name
    )
}