package com.eliseubatista.pocketdex.network

import com.squareup.moshi.Json

/*
This is the pokemon name, and the url to its detailed data
 */
data class BaseNameAndUrl(
    @Json(name = "name") val name: String,
    //@Json(name = "url") val url: String,
)

/*
This is the global pokemon data
 */
data class GlobalSearchData(
    @Json(name = "count") val numberOfResults: Int,
    @Json(name = "next") val next: String?,
    @Json(name = "previous") val previous: String?,
    @Json(name = "results") val results: List<BaseNameAndUrl>
)

fun List<BaseNameAndUrl>.getNames(): List<String> {
    val names = mutableListOf<String>()
    names.clear()

    for (element in this) {
        if (element.name.isEmpty() || element.name.isBlank() || element.name == " ") {
            continue
        }
        names.add(element.name)
    }

    return names
}