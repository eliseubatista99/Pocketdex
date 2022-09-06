package com.eliseubatista.pocketdex.fragments.regions

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.eliseubatista.pocketdex.R
import com.eliseubatista.pocketdex.database.regions.DatabaseRegions
import com.eliseubatista.pocketdex.databinding.FragmentRegionDetailsBinding
import com.eliseubatista.pocketdex.fragments.pokemon.PokedexDetailsAboutFragment
import com.eliseubatista.pocketdex.utils.*
import com.eliseubatista.pocketdex.views.SectionPagerAdapter
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class RegionDetailsFragment : Fragment() {
    private var pokemonName = ""
    private lateinit var viewModel: RegionDetailsViewModel
    private lateinit var viewModelFactory: RegionDetailsViewModel.Factory

    private lateinit var progressBarDialog: ProgressBarDialog


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding: FragmentRegionDetailsBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_region_details, container, false)

        setUpViewPagerAndTabLayout(binding)

        val bundle = requireArguments()
        pokemonName = bundle.getString("REGION_NAME", "")

        viewModelFactory =
            RegionDetailsViewModel.Factory(requireActivity().application, pokemonName)
        viewModel =
            ViewModelProvider(this, viewModelFactory)[RegionDetailsViewModel::class.java]

        progressBarDialog = ProgressBarDialog(inflater, requireContext())

        viewModel.isInFavorites.observe(
            viewLifecycleOwner
        ) { inFavorites ->
            refreshFavorites(binding, inFavorites)
        }

        viewModel.isLoadingRegion.observe(viewLifecycleOwner) {
            if (it) {
                progressBarDialog.startLoading("Loading Region..")
            } else {
                progressBarDialog.dismiss()
            }
        }

        viewModel.region.observe(
            viewLifecycleOwner
        ) { region -> refreshRegionHeader(binding, region) }

        binding.toolbarRegionDetails.favorite.setOnClickListener {
            viewModel.addOrRemoveFavorite()
        }
        binding.toolbarRegionDetails.arrowBack.setOnClickListener { parentFragmentManager.popBackStack() }

        return binding.root
    }

    private fun setUpViewPagerAndTabLayout(binding: FragmentRegionDetailsBinding) {
        val viewPager = binding.regionDetailsViewPager
        val tabLayout = binding.regionDetailsTabLayout

        val adapter = SectionPagerAdapter(childFragmentManager, lifecycle)
        adapter.addFragment(RegionLocationsFragment(), "Locations")

        viewPager.adapter = adapter

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

    private fun refreshRegionHeader(
        binding: FragmentRegionDetailsBinding,
        region: DatabaseRegions
    ) {
        val textColor = ContextCompat.getColor(requireContext(), R.color.white)

        binding.toolbarRegionDetails.detailsName.text =
            formatPocketdexObjectName(region.name)

        binding.toolbarRegionDetails.detailsId.text = "#${region.id}"

        binding.toolbarRegionDetails.detailsName.setTextColor(textColor)

        binding.toolbarRegionDetails.detailsId.setTextColor(textColor)

        binding.toolbarRegionDetails.arrowBack.setColorFilter(textColor)
        binding.toolbarRegionDetails.favorite.setColorFilter(textColor)

        val regionImage = getLocationRegionImage(requireContext(), region.name)
        binding.regionDetailsBackground.setImageDrawable(regionImage)
    }

    private fun refreshFavorites(binding: FragmentRegionDetailsBinding, isInFavorites: Boolean) {
        if (isInFavorites) {
            binding.toolbarRegionDetails.favorite.setImageResource(R.drawable.ic_star_fill)
        } else {
            binding.toolbarRegionDetails.favorite.setImageResource(R.drawable.ic_star_border)
        }
    }

}