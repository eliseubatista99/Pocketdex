package com.eliseubatista.pocketdex.activities

import android.content.SharedPreferences
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.eliseubatista.pocketdex.R
import com.eliseubatista.pocketdex.databinding.ActivityControlBinding
import com.eliseubatista.pocketdex.fragments.items.ItemsFragment
import com.eliseubatista.pocketdex.fragments.regions.RegionsFragment
import com.eliseubatista.pocketdex.fragments.pokemon.PokedexFragment
import com.eliseubatista.pocketdex.fragments.profile.ProfileFragment
import com.eliseubatista.pocketdex.fragments.settings.SettingsFragment
import com.eliseubatista.pocketdex.utils.replaceFragment

class ControlActivity : AppCompatActivity() {

    private lateinit var binding: ActivityControlBinding
    lateinit var sharedPrefs: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding =
            DataBindingUtil.setContentView(this, R.layout.activity_control)

        //Create the bottom navigation click listener
        binding.bottomNavigationView.setOnItemSelectedListener { item ->
            onBottomNavViewItemSelected(
                item
            )
        }

        sharedPrefs = getSharedPreferences("POCKETDEX_PREFS", MODE_PRIVATE)

        //By default, we want to make sure we open the pokedex
        navigateToPokedex()
    }

    /**
     * Handle the bottom bar clicks
     */
    private fun onBottomNavViewItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.nav_locations -> navigateToLocations()
            R.id.nav_items -> navigateToItems()
            R.id.nav_pokedex -> navigateToPokedex()
            R.id.nav_profile -> navigateToProfile()
            R.id.nav_settings -> navigateToSettings()
        }

        return true
    }

    /**
     * Open the locations fragment
     */
    private fun navigateToLocations() {
        val locationFragment = RegionsFragment()

        val fragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.replaceFragment(locationFragment, R.id.control_fragment_container)

    }

    /**
     * Open the items fragment
     */
    private fun navigateToItems() {
        binding.bottomNavigationView.menu.findItem(R.id.nav_items).isChecked = true

        val itemsFragment = ItemsFragment()

        val fragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.replaceFragment(itemsFragment, R.id.control_fragment_container)
    }

    /**
     * Open the pokedex fragment
     */
    private fun navigateToPokedex() {

        binding.bottomNavigationView.menu.findItem(R.id.nav_pokedex).isChecked = true

        val pokedexFragment = PokedexFragment()

        val fragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.replaceFragment(pokedexFragment, R.id.control_fragment_container)
    }

    /**
     * Open the profile fragment
     */
    private fun navigateToProfile() {
        binding.bottomNavigationView.menu.findItem(R.id.nav_profile).isChecked = true

        val profileFragment = ProfileFragment()

        val fragmentTransaction = supportFragmentManager.beginTransaction()

        fragmentTransaction.replaceFragment(profileFragment, R.id.control_fragment_container)
    }

    /**
     * Open the settings fragment
     */
    private fun navigateToSettings() {
        binding.bottomNavigationView.menu.findItem(R.id.nav_settings).isChecked = true

        val settingsFragment = SettingsFragment()

        val fragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.replaceFragment(settingsFragment, R.id.control_fragment_container)

    }
}