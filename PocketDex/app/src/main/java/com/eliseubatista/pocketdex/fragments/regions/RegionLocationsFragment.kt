package com.eliseubatista.pocketdex.fragments.regions

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.eliseubatista.pocketdex.R
import com.eliseubatista.pocketdex.database.regions.DatabaseLocation
import com.eliseubatista.pocketdex.databinding.FragmentRegionLocationsBinding
import com.eliseubatista.pocketdex.views.regions.LocationsAdapter
import com.eliseubatista.pocketdex.views.regions.OnLocationClickedListener

class RegionLocationsFragment : Fragment() {

    private var regionName = ""
    private lateinit var viewModel: RegionDetailsViewModel
    private lateinit var viewModelFactory: RegionDetailsViewModel.Factory

    private lateinit var binding: FragmentRegionLocationsBinding

    private lateinit var locationListAdapter: LocationsAdapter
    private var isFiltered = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_region_locations, container, false)


        viewModelFactory =
            RegionDetailsViewModel.Factory(requireActivity().application, regionName)
        viewModel =
            ViewModelProvider(
                requireParentFragment(),
                viewModelFactory
            )[RegionDetailsViewModel::class.java]

        locationListAdapter = LocationsAdapter()

        setupLinearRecyclerView()

        //Create and observer to refresh the list automatically
        viewModel.locations.observe(viewLifecycleOwner) {
            updateList(it)
        }

        return binding.root
    }

    private fun setupLinearRecyclerView() {

        //Create and add the layout manager
        val linearLayoutManager = LinearLayoutManager(context)
        linearLayoutManager.orientation = RecyclerView.VERTICAL
        binding.regionLocationsList.layoutManager = linearLayoutManager

        //Bind the list adapter
        binding.regionLocationsList.adapter = locationListAdapter

        //Click listener
        locationListAdapter.onLocationClickedListener =
            object : OnLocationClickedListener {
                override fun onLocationClicked(name: String) {
                }

            }
    }

    private fun updateList(list: List<DatabaseLocation>?) {

        if (list == null) {
            return
        }

        locationListAdapter.submitList(list)
    }
}