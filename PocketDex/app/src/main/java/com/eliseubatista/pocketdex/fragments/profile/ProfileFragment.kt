package com.eliseubatista.pocketdex.fragments.profile

import android.app.Activity
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContract
import androidx.activity.result.contract.ActivityResultContracts
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.eliseubatista.pocketdex.R
import com.eliseubatista.pocketdex.activities.ControlActivity
import com.eliseubatista.pocketdex.database.pokemons.DatabasePokemon
import com.eliseubatista.pocketdex.databinding.FragmentPokedexDetailsBinding
import com.eliseubatista.pocketdex.databinding.FragmentProfileBinding
import com.eliseubatista.pocketdex.fragments.pokemon.PokedexDetailsAboutFragment
import com.eliseubatista.pocketdex.fragments.pokemon.PokedexDetailsEvolutionsFragment
import com.eliseubatista.pocketdex.fragments.pokemon.PokedexDetailsStatsFragment
import com.eliseubatista.pocketdex.fragments.pokemon.PokemonDetailsViewModel
import com.eliseubatista.pocketdex.utils.*
import com.eliseubatista.pocketdex.views.SectionPagerAdapter
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class ProfileFragment : Fragment() {

    private var pokemonName = ""
    private lateinit var viewModel: ProfileViewModel
    private lateinit var viewModelFactory: ProfileViewModel.Factory

    private lateinit var progressBarDialog: ProgressBarDialog
    private lateinit var binding: FragmentProfileBinding
    private lateinit var controlActivity: ControlActivity

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_profile, container, false)

        setUpViewPagerAndTabLayout()

        viewModelFactory =
            ProfileViewModel.Factory(requireActivity().application)
        viewModel =
            ViewModelProvider(this, viewModelFactory)[ProfileViewModel::class.java]

        //Store a reference to the control activity to access shared preferences
        controlActivity = requireActivity() as ControlActivity

        getSavedUserImage()

        progressBarDialog = ProgressBarDialog(inflater, requireContext())

        viewModel.isLoadingProfile.observe(viewLifecycleOwner) {
            if (it) {
                progressBarDialog.startLoading("Loading Profile..")
            } else {
                progressBarDialog.dismiss()
            }
        }

        binding.profileAvatar.setOnClickListener {
            pickAvatar()
        }

        return binding.root
    }

    /**
     * Tries to get a previously saved user image from the shared preferences
     */
    private fun getSavedUserImage() {
        //Check for a user image in the shared preferences
        val savedImage = controlActivity.sharedPrefs.getString("USER_PHOTO", "")

        //If we haven't found a image, use the default avatar
        if (savedImage == "") {
            binding.profileAvatar.setImageResource(R.drawable.img_trainer_avatar)

        } else { //Otherwise, convert it to a bitmap and aplly it
            val userImageBitmap = stringToBitmap(savedImage!!)

            binding.profileAvatar.setImageBitmap(userImageBitmap)
        }
    }

    /**
     * Tries to pick an image from the storage
     */
    private fun pickAvatar() {
        /*If we dont have storage permission, we ask for it
        if (!checkStoragePermision(requireContext())) {
            requestStoragePermission(requireActivity())
        }*/

        //Create the pick intent
        val intent = Intent(
            Intent.ACTION_OPEN_DOCUMENT,
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        )

        //Get that image
        getAvatar.launch(intent)
    }

    /**
     * Intent to get the image from the storage
     */
    private val getAvatar =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            //If we got a valid data in return
            if (it.resultCode == Activity.RESULT_OK && it.data != null) {
                val selectedImageUri = it.data!!.data

                //Create a source for the bitmap
                val source =
                    ImageDecoder.createSource(requireActivity().contentResolver, selectedImageUri!!)
                //Create a bitmap from the source
                val bitmap = ImageDecoder.decodeBitmap(source)

                //Set that user image with the bitmap
                binding.profileAvatar.setImageBitmap(bitmap)

                //Convert the bitmap to a string and store it in the sharedPrefs
                controlActivity.sharedPrefs.edit()
                    .putString("USER_PHOTO", bitmapToString(bitmap)).commit()
            }
        }

    /**
     * Sets up the view pager and the tab layout
     */
    private fun setUpViewPagerAndTabLayout() {
        val viewPager = binding.profileViewPager
        val tabLayout = binding.profileTabLayout

        val adapter = SectionPagerAdapter(childFragmentManager, lifecycle)

        //Add the fragments
        adapter.addFragment(ProfilePokemonsFragment(), "Pokemons")
        adapter.addFragment(ProfileItemsFragment(), "Items")
        adapter.addFragment(ProfileRegionsFragment(), "Regions")

        viewPager.adapter = adapter

        //Add the tab clicked callback
        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.text = adapter.getPageTitle(position)
        }.attach()

        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {
            }


        })

    }
}