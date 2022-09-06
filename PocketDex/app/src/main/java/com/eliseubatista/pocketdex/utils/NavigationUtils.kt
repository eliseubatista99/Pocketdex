package com.eliseubatista.pocketdex.utils

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.eliseubatista.pocketdex.R

fun FragmentTransaction.replaceFragment(fragment: Fragment, layout: Int) {
    this.replace(layout, fragment)
    this.addToBackStack("BACKSTACK")
    this.commit()
}

fun FragmentTransaction.addFragment(fragment: Fragment, layout: Int) {
    this.add(layout, fragment)
    this.addToBackStack("BACKSTACK")
    this.commit()
}