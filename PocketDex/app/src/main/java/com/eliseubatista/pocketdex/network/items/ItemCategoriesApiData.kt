package com.eliseubatista.pocketdex.network.items

import com.eliseubatista.pocketdex.database.items.DatabaseItemCategories
import com.eliseubatista.pocketdex.database.pokemons.DatabaseTypes
import com.eliseubatista.pocketdex.network.BaseNameAndUrl
import com.squareup.moshi.Json

data class ItemCategoriesApiData(
    @Json(name = "id") val id: Int,
    @Json(name = "name") val name: String,
)

fun ItemCategoriesApiData.asDatabaseModel(): DatabaseItemCategories {

    return DatabaseItemCategories(
        id,
        name
    )
}
