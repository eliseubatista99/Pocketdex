package com.eliseubatista.pocketdex.database

import androidx.room.TypeConverter
import com.eliseubatista.pocketdex.database.profile.DatabaseFavorite

class DatabaseTypeConverters {

    /**
     * Converts a list of string into a single string
     */
    @TypeConverter
    fun fromStringList(listOfStrings: List<String>): String {
        var finalString = ""

        for ((index, s) in listOfStrings.withIndex()) {

            if (index == 0) {
                finalString = s
            } else {
                finalString += ";$s"
            }
        }

        return finalString
    }

    /**
     * Splits a previously converted string into a list of strings
     */
    @TypeConverter
    fun toStringList(storedString: String): List<String> {

        return storedString.split(";")
    }

    /**
     * Converts a list of integers into a single string
     */
    @TypeConverter
    fun fromIntList(listOfInts: List<Int>): String {
        var finalString = ""

        for ((index, i) in listOfInts.withIndex()) {

            if (index == 0) {
                finalString = i.toString()
            } else {
                finalString += ";$i"
            }
        }

        return finalString
    }

    /**
     * Splits a previously converted string into a list of integers
     */
    @TypeConverter
    fun toIntList(storedString: String): List<Int> {
        val stringList = storedString.split(";")

        val intList = mutableListOf<Int>()

        for (s in stringList) {
            intList.add(s.toInt())
        }

        return intList
    }

    /**
     * Converts a list of integers into a single string
     */
    @TypeConverter
    fun fromDatabaseFavoriteList(listOfFavorites: List<DatabaseFavorite>): String {
        var finalString = ""

        for ((index, i) in listOfFavorites.withIndex()) {

            if (index == 0) {
                finalString = "${i.id}/${i.name}/${i.category}"
            } else {
                finalString += ";$i"
            }
        }

        return finalString
    }

    /**
     * Splits a previously converted string into a list of integers
     */
    @TypeConverter
    fun toDatabseFavoriteList(storedString: String): List<DatabaseFavorite> {
        val stringList = storedString.split(";")

        val listOfFavorites = mutableListOf<DatabaseFavorite>()

        for (s in stringList) {

            val splittedFavorite = storedString.split("/")

            val favorite = DatabaseFavorite(
                splittedFavorite[0].toLong(),
                splittedFavorite[1],
                splittedFavorite[2]
            )
            listOfFavorites.add(favorite)
        }

        return listOfFavorites
    }
}