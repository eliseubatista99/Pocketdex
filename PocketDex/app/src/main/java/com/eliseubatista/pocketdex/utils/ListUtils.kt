package com.eliseubatista.pocketdex.utils

fun isPokemonDamageRelationEmpty(damageRelation: List<String>): Boolean {
    if (damageRelation.isEmpty()) {
        return true
    }

    if (damageRelation.size == 1) {
        if (damageRelation[0] == "" || damageRelation[0] == " ") {
            return true
        }
    }

    return false
}