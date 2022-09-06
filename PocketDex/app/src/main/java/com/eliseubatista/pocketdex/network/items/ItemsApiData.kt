package com.eliseubatista.pocketdex.network.items

import com.eliseubatista.pocketdex.database.items.DatabaseItems
import com.eliseubatista.pocketdex.network.BaseNameAndUrl
import com.squareup.moshi.Json

/*
This is the detailed data of each Item
 */

data class ItemData(
    @Json(name = "attributes") val attributes: List<BaseNameAndUrl>,
    @Json(name = "category") val category: BaseNameAndUrl,
    @Json(name = "cost") val cost: Int,
    @Json(name = "effect_entries") val effectEntries: List<ItemEffectData>,
    @Json(name = "id") val id: Int,
    @Json(name = "flavor_text_entries") val flavorTextEntries: List<ItemFlavorEntriesData>,
    @Json(name = "name") val name: String,
    @Json(name = "sprites") val sprites: ItemSpriteData,
)

/*
This is the item effect data retrieved from the item detailed data
 */
data class ItemEffectData(
    @Json(name = "effect") val effect: String,
    @Json(name = "language") val language: BaseNameAndUrl,
    @Json(name = "short_effect") val shortEffect: String
)


/*
This is the item flavor entries data retrieved from the item detailed data
 */
data class ItemFlavorEntriesData(
    @Json(name = "language") val language: BaseNameAndUrl,
    @Json(name = "text") val text: String,
)

/*
This is the item sprite data retrieved from the item detailed data
 */
data class ItemSpriteData(
    @Json(name = "default") val default: String,
)

fun ItemData.asDatabaseModel(): DatabaseItems {
    val itemAttributes = this.getItemAttributes()
    val itemEffects = this.getItemEffects()
    val flavor = this.getFlavor()


    return DatabaseItems(
        this.id,
        this.cost,
        this.name,
        itemAttributes,
        this.category.name,
        itemEffects,
        flavor,
        this.sprites.default
    )
}

fun ItemData.getItemAttributes(): List<String> {
    val attributesList = mutableListOf<String>()

    for (attribute in this.attributes) {
        val attributeName = attribute.name

        attributesList.add(attributeName)
    }

    return attributesList
}

fun ItemData.getItemEffects(): List<String> {
    val effectsList = mutableListOf<String>()

    for (effect in this.effectEntries) {
        if (effect.language.name == "en") {
            val effectDescription = effect.effect
            effectsList.add(effectDescription)
        }
    }

    return effectsList
}

fun ItemData.getFlavor(): String {
    var flavor = ""

    for (flavorEntrie in this.flavorTextEntries) {
        if (flavorEntrie.language.name == "en") {
            flavor = flavorEntrie.text
        }
    }

    return flavor
}