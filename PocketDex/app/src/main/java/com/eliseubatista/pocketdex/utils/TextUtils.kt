package com.eliseubatista.pocketdex.utils

import android.util.Log
import java.util.*

fun isStringBlank(string: String): Boolean {
    return string == "" || string.isBlank() || string.isEmpty()
}

//Function used to format pokemon, item and locations names
fun formatPocketdexObjectName(name: String): String {
    if (name.isEmpty()) {
        return name
    }

    //Make sure first char is uppercase
    val upperCaseName = name.toCharArray()
    upperCaseName[0] = upperCaseName[0].uppercaseChar()

    for ((index, letter) in upperCaseName.withIndex()) {
        if (letter == '-' && index < upperCaseName.size - 1) {
            upperCaseName[index] = ' '
            upperCaseName[index + 1] = upperCaseName[index + 1].uppercaseChar()
        }
    }

    var newName = String(upperCaseName)

    /*For names like "nidoran-f" and "nidoran-m"
    if (newName.contains("-f")) {
        newName = newName.replace("-f", " Female")
    } else if (newName.contains("-m")) {
        newName = newName.replace("-m", " Male")
    } else if (newName.contains("-")) {
        newName = newName.replace("-", " ")
    }*/

    return newName
}

fun formatPocketdexObjectDescription(description: String): String {
    if (description.isEmpty()) {
        return description
    }

    val tokenizer = StringTokenizer(description)
    val builder = StringBuilder()

    while (tokenizer.hasMoreTokens()) {
        val string = tokenizer.nextToken()
        builder.append(string).append(" ")
    }

    return builder.toString()
}

fun formatPokemonGenus(genus: String): String {

    Log.i("Genus", genus)

    if (genus.isEmpty()) {
        return genus
    }

    //Make sure first char is uppercase
    val upperCaseGenus = genus.toCharArray()
    upperCaseGenus[0] = upperCaseGenus[0].uppercaseChar()

    var newGenus = String(upperCaseGenus)

    //For types like "Seed-Pokemon"
    if (newGenus.contains("Pokémon")) {
        newGenus = newGenus.replace("Pokémon", "")
    }

    return newGenus
}

fun formatPokemonHeight(height: Int): String {

    return "${height / 10.0f}m"
}

fun formatPokemonWeight(weight: Int): String {

    return "${weight / 10.0f}Kg"
}

