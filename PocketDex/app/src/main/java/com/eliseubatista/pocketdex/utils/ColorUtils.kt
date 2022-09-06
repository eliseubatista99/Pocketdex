package com.eliseubatista.pocketdex.utils

import android.content.Context
import android.graphics.Color
import androidx.core.content.ContextCompat
import com.eliseubatista.pocketdex.R
import kotlin.math.min
import kotlin.math.roundToInt

//Function to get a pokemon color
fun getPokemonBackgroundColor(context: Context, pokemonColorName: String): Int {

    val newColor = when (pokemonColorName) {
        "black" -> ContextCompat.getColor(context, R.color.pokemon_black)
        "blue" -> ContextCompat.getColor(context, R.color.pokemon_blue)
        "brown" -> ContextCompat.getColor(context, R.color.pokemon_brown)
        "gray" -> ContextCompat.getColor(context, R.color.pokemon_gray)
        "green" -> ContextCompat.getColor(context, R.color.pokemon_green)
        "pink" -> ContextCompat.getColor(context, R.color.pokemon_pink)
        "purple" -> ContextCompat.getColor(context, R.color.pokemon_purple)
        "red" -> ContextCompat.getColor(context, R.color.pokemon_red)
        "white" -> ContextCompat.getColor(context, R.color.pokemon_white)
        "yellow" -> ContextCompat.getColor(context, R.color.pokemon_yellow)
        else -> ContextCompat.getColor(context, R.color.pokemon_white)
    }

    return newColor
}

//Function to get a pokemon color
fun getItemBackgroundColor(context: Context, itemId: Int): Int {

    var divider = 0

    //From 9 to 1, get the first divider of this id
    for (i in 10 downTo 1) {
        //If the division remainder is zero, get this divider
        if (itemId % i == 0) {
            divider = i
            break
        }
    }


    val newColor = when (divider) {
        1 -> ContextCompat.getColor(context, R.color.item_black)
        2 -> ContextCompat.getColor(context, R.color.item_blue)
        3 -> ContextCompat.getColor(context, R.color.item_brown)
        4 -> ContextCompat.getColor(context, R.color.item_gray)
        5 -> ContextCompat.getColor(context, R.color.item_green)
        6 -> ContextCompat.getColor(context, R.color.item_pink)
        7 -> ContextCompat.getColor(context, R.color.item_purple)
        8 -> ContextCompat.getColor(context, R.color.item_red)
        9 -> ContextCompat.getColor(context, R.color.item_white)
        10 -> ContextCompat.getColor(context, R.color.item_yellow)
        else -> ContextCompat.getColor(context, R.color.item_white)
    }

    return newColor
}

//Function to calculate if the text on top of backgroundColor should be black or white
fun getTextColorByBackgroundColor(context: Context, backgroundColor: Int): Int {
    //By default, the text color is white
    var newColor = ContextCompat.getColor(context, R.color.white)

    val colorThreshold =
        (Color.red(backgroundColor) * 0.299) + (Color.green(backgroundColor) * 0.587) + (Color.blue(
            backgroundColor
        ) * 0.114)

    //If the color brightness threshold is greater than a specific value, the text should be black
    if (colorThreshold > 186) {
        newColor = ContextCompat.getColor(context, R.color.black)
    }

    return newColor
}

/*
Function to manipulate the color brightness by a specific factor.
If the factor is less than 1, the color would be darker
If the factor is greater than 1, the color would be brighter
If the factor is 1, the color would be the same
 */
fun manipulateColor(color: Int, factor: Float): Int {
    val a = Color.alpha(color)
    val r = (Color.red(color) * factor).roundToInt()
    val g = (Color.green(color) * factor).roundToInt()
    val b = (Color.blue(color) * factor).roundToInt()

    //return the new color, but make sure the values are not above 255
    return Color.argb(a, min(r, 255), min(g, 255), min(b, 255))
}