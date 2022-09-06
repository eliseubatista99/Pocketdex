package com.eliseubatista.pocketdex.activities

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.eliseubatista.pocketdex.R
import com.eliseubatista.pocketdex.databinding.ActivityMainBinding
import com.eliseubatista.pocketdex.utils.changeApplicationNightMode

class MainActivity : AppCompatActivity() {

    lateinit var sharedPrefs: SharedPreferences


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding =
            DataBindingUtil.setContentView<ActivityMainBinding>(this, R.layout.activity_main)

        sharedPrefs = getSharedPreferences("POCKETDEX_PREFS", MODE_PRIVATE)

        val savedDarkMode = sharedPrefs.getBoolean("USER_DARK_MODE", false)
        changeApplicationNightMode(savedDarkMode)


        //Make sure the action bar is no visible here
        supportActionBar?.hide()

        setupClickListeners(binding)
    }

    /**
     * If the user clicks on the screen, we should open the control activity
     **/
    private fun setupClickListeners(binding: ActivityMainBinding) {
        binding.titleScreenTouchImage.setOnClickListener {
            openControlActivity()
        }
    }

    private fun openControlActivity() {
        val intent = Intent(this, ControlActivity::class.java)
        startActivity(intent)
    }
}