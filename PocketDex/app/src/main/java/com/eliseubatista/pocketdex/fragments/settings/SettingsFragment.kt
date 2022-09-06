package com.eliseubatista.pocketdex.fragments.settings

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.app.AppCompatDelegate.*
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.eliseubatista.pocketdex.R
import com.eliseubatista.pocketdex.activities.ControlActivity
import com.eliseubatista.pocketdex.databinding.FragmentProfileBinding
import com.eliseubatista.pocketdex.databinding.FragmentSettingsBinding
import com.eliseubatista.pocketdex.utils.bitmapToString
import com.eliseubatista.pocketdex.utils.changeApplicationNightMode
import com.eliseubatista.pocketdex.utils.isInDarkMode
import com.eliseubatista.pocketdex.utils.stringToBitmap

class SettingsFragment : Fragment() {

    private lateinit var binding: FragmentSettingsBinding
    private lateinit var controlActivity: ControlActivity

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_settings, container, false)

        //Store a reference to the control activity to access shared preferences
        controlActivity = requireActivity() as ControlActivity

        getSavedUserDarkMode()

        binding.nightModeSwitch.setOnCheckedChangeListener { buttonView, isChecked ->
            changeApplicationNightMode(isChecked)
            controlActivity.sharedPrefs.edit().putBoolean("USER_DARK_MODE", isChecked).commit()
        }

        return binding.root
    }

    /**
     * Tries to get a previously saved user dark mode from the shared preferences
     */
    private fun getSavedUserDarkMode() {
        //Check for a user image in the shared preferences
        val savedDarkMode = controlActivity.sharedPrefs.getBoolean("USER_DARK_MODE", false)

        binding.nightModeSwitch.isChecked = savedDarkMode
    }


}