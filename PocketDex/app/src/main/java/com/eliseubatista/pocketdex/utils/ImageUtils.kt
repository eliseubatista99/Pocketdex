package com.eliseubatista.pocketdex.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.Drawable
import android.util.Base64
import androidx.core.content.ContextCompat
import com.eliseubatista.pocketdex.R
import java.io.ByteArrayOutputStream

fun getImageScaleByEvolutionChain(
    pokemonName: String,
    evolutionChains: List<String>
): Float {
    var pokemonIndex = 3

    //for each evolution chain
    for (evolutionChain in evolutionChains) {

        //Split the url to get the chain id
        val splitChain = evolutionChain.split(":")


        for ((index, evolution) in splitChain.withIndex()) {
            if (pokemonName == evolution) {

                pokemonIndex = index
                break
            }
        }
    }

    val imageScale = when (pokemonIndex) {
        0 -> 1.4f
        1 -> 1.15f
        else -> 1.0f
    }

    return imageScale
}

//Function to get a pokemon type logo
fun getPokemonTypeLogoImage(context: Context, pokemonType: String): Drawable? {
    val newLogo = when (pokemonType) {
        "normal" -> ContextCompat.getDrawable(context, R.drawable.img_normal_type_logo)
        "fighting" -> ContextCompat.getDrawable(context, R.drawable.img_fighting_type_logo)
        "flying" -> ContextCompat.getDrawable(context, R.drawable.img_flying_type_logo)
        "poison" -> ContextCompat.getDrawable(context, R.drawable.img_poison_type_logo)
        "ground" -> ContextCompat.getDrawable(context, R.drawable.img_ground_type_logo)
        "rock" -> ContextCompat.getDrawable(context, R.drawable.img_rock_type_logo)
        "bug" -> ContextCompat.getDrawable(context, R.drawable.img_bug_type_logo)
        "ghost" -> ContextCompat.getDrawable(context, R.drawable.img_ghost_type_logo)
        "steel" -> ContextCompat.getDrawable(context, R.drawable.img_steel_type_logo)
        "fire" -> ContextCompat.getDrawable(context, R.drawable.img_fire_type_logo)
        "water" -> ContextCompat.getDrawable(context, R.drawable.img_water_type_logo)
        "grass" -> ContextCompat.getDrawable(context, R.drawable.img_grass_type_logo)
        "electric" -> ContextCompat.getDrawable(context, R.drawable.img_eletric_type_logo)
        "psychic" -> ContextCompat.getDrawable(context, R.drawable.img_psychic_type_logo)
        "ice" -> ContextCompat.getDrawable(context, R.drawable.img_ice_type_logo)
        "dragon" -> ContextCompat.getDrawable(context, R.drawable.img_dragon_type_logo)
        "dark" -> ContextCompat.getDrawable(context, R.drawable.img_dark_type_logo)
        "fairy" -> ContextCompat.getDrawable(context, R.drawable.img_fairy_type_logo)
        "shadow" -> ContextCompat.getDrawable(context, R.drawable.img_dark_type_logo)
        else -> ContextCompat.getDrawable(context, R.drawable.img_normal_type_logo)
    }

    return newLogo
}

//Function to get a pokemon type text image
fun getPokemonTypeTextImage(context: Context, pokemonType: String): Drawable? {
    val newTextImage = when (pokemonType) {
        "normal" -> ContextCompat.getDrawable(context, R.drawable.img_normal_type_text)
        "fighting" -> ContextCompat.getDrawable(context, R.drawable.img_fighting_type_text)
        "flying" -> ContextCompat.getDrawable(context, R.drawable.img_flying_type_text)
        "poison" -> ContextCompat.getDrawable(context, R.drawable.img_poison_type_text)
        "ground" -> ContextCompat.getDrawable(context, R.drawable.img_ground_type_text)
        "rock" -> ContextCompat.getDrawable(context, R.drawable.img_rock_type_text)
        "bug" -> ContextCompat.getDrawable(context, R.drawable.img_bug_type_text)
        "ghost" -> ContextCompat.getDrawable(context, R.drawable.img_ghost_type_text)
        "steel" -> ContextCompat.getDrawable(context, R.drawable.img_steel_type_text)
        "fire" -> ContextCompat.getDrawable(context, R.drawable.img_fire_type_text)
        "water" -> ContextCompat.getDrawable(context, R.drawable.img_water_type_text)
        "grass" -> ContextCompat.getDrawable(context, R.drawable.img_grass_type_text)
        "electric" -> ContextCompat.getDrawable(context, R.drawable.img_eletric_type_text)
        "psychic" -> ContextCompat.getDrawable(context, R.drawable.img_psychic_type_text)
        "ice" -> ContextCompat.getDrawable(context, R.drawable.img_ice_type_text)
        "dragon" -> ContextCompat.getDrawable(context, R.drawable.img_dragon_type_text)
        "dark" -> ContextCompat.getDrawable(context, R.drawable.img_dark_type_text)
        "fairy" -> ContextCompat.getDrawable(context, R.drawable.img_fairy_type_text)
        "shadow" -> ContextCompat.getDrawable(context, R.drawable.img_dark_type_text)
        else -> ContextCompat.getDrawable(context, R.drawable.img_normal_type_text)
    }

    return newTextImage
}

//Function to get a pokemon type logo
fun getLocationRegionImage(context: Context, region: String): Drawable? {
    val newImage = when (region) {
        "kanto" -> ContextCompat.getDrawable(context, R.drawable.region_kanto)
        "johto" -> ContextCompat.getDrawable(context, R.drawable.region_johto)
        "hoenn" -> ContextCompat.getDrawable(context, R.drawable.region_hoenn)
        "sinnoh" -> ContextCompat.getDrawable(context, R.drawable.region_sinnoh)
        "unova" -> ContextCompat.getDrawable(context, R.drawable.region_unova)
        "kalos" -> ContextCompat.getDrawable(context, R.drawable.region_kalos)
        "alola" -> ContextCompat.getDrawable(context, R.drawable.region_alola)
        "galar" -> ContextCompat.getDrawable(context, R.drawable.region_galar)
        else -> ContextCompat.getDrawable(context, R.drawable.region_kanto)
    }

    return newImage
}

fun bitmapToString(bitmap: Bitmap): String {
    val stream = ByteArrayOutputStream()
    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream)

    val byteArray = stream.toByteArray()
    return Base64.encodeToString(byteArray, Base64.DEFAULT)
}

fun stringToBitmap(string: String): Bitmap {
    val byteArray = Base64.decode(string, Base64.DEFAULT)

    return BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size)
}
