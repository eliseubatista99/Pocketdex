package com.eliseubatista.pocketdex.network.regions

import com.eliseubatista.pocketdex.database.regions.DatabaseRegions
import com.eliseubatista.pocketdex.network.BaseNameAndUrl
import com.squareup.moshi.Json

data class RegionApiData(
    @Json(name = "id") val id: Int,
    @Json(name = "locations") val locations: List<BaseNameAndUrl>,
    @Json(name = "name") val name: String,
)

fun RegionApiData.asDatabaseModel(): DatabaseRegions {

    val locations = this.getLocations()

    return DatabaseRegions(
        this.id,
        this.name,
        locations
    )
}

fun RegionApiData.getLocations(): List<String> {
    val listOfLocations = mutableListOf<String>()

    for (location in this.locations) {
        listOfLocations.add(location.name)
    }

    return listOfLocations
}