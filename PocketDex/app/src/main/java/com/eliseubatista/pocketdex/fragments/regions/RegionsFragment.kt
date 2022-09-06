package com.eliseubatista.pocketdex.fragments.regions

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.eliseubatista.pocketdex.R
import com.eliseubatista.pocketdex.database.regions.DatabaseRegions
import com.eliseubatista.pocketdex.databinding.FragmentRegionsBinding
import com.eliseubatista.pocketdex.utils.ProgressBarDialog
import com.eliseubatista.pocketdex.utils.replaceFragment
import com.eliseubatista.pocketdex.views.regions.OnRegionClickedListener
import com.eliseubatista.pocketdex.views.regions.RegionAdapter


class RegionsFragment : Fragment() {

    private lateinit var viewModel: RegionsViewModel
    private lateinit var viewModelFactory: RegionsViewModel.Factory
    private lateinit var progressBarDialog: ProgressBarDialog

    private lateinit var binding: FragmentRegionsBinding

    private lateinit var listAdapter: RegionAdapter
    private var isFiltered = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_regions, container, false)

        viewModelFactory = RegionsViewModel.Factory(requireActivity().application)
        viewModel =
            ViewModelProvider(this, viewModelFactory)[RegionsViewModel::class.java]

        listAdapter = RegionAdapter()
        progressBarDialog = ProgressBarDialog(inflater, requireContext())

        setupSearchView()
        setupLinearRecyclerView()

        //Create and observer to refresh the list automatically
        viewModel.regions.observe(viewLifecycleOwner) {
            updateList(it, false)
        }

        //Create and observer to refresh the list automatically
        viewModel.searchedRegions.observe(viewLifecycleOwner) {
            updateList(it, true)
        }

        return binding.root
    }

    private fun setupSearchView() {
        binding.locationsSearch.searchBar.queryHint = "Type Location Name Here"

        binding.locationsSearch.searchBar.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (query.isNullOrEmpty() || query.isNullOrBlank()) {
                    isFiltered = false
                    updateList(viewModel.regions.value, false)
                    return false
                }

                isFiltered = true
                viewModel.getRegionLikeName(query, false)

                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if (newText.isNullOrEmpty() || newText.isNullOrBlank()) {
                    isFiltered = false
                    updateList(viewModel.regions.value, false)
                    return false
                }

                isFiltered = true
                viewModel.getRegionLikeName(newText, true)

                return true
            }

        })
    }

    private fun setupLinearRecyclerView() {

        //Create and add the layout manager
        val linearLayoutManager = LinearLayoutManager(context)
        linearLayoutManager.orientation = RecyclerView.VERTICAL
        binding.locationsList.layoutManager = linearLayoutManager

        //Bind the list adapter
        binding.locationsList.adapter = listAdapter

        //Click listener
        listAdapter.onRegionClickedListener =
            object : OnRegionClickedListener {
                override fun onRegionClicked(name: String) {
                    navigateToPokedexDetails(name)
                }

            }

        viewModel.isLoadingMoreRegions.observe(viewLifecycleOwner) {
            if (it) {
                progressBarDialog.startLoading("Loading More Locations..")
            } else {
                progressBarDialog.dismiss()
            }
        }

        //Scroll listener
        binding.locationsList.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)

                //If we reach the end of the list, we want to show more pokemons
                //TODO: Check scroll
                if (!recyclerView.canScrollVertically(3) && !isFiltered) {
                    viewModel.getMoreRegions()
                }
            }
        })
    }

    private fun updateList(list: List<DatabaseRegions>?, isFromSearchBar: Boolean) {

        if (list == null) {
            return
        }

        if (!isFromSearchBar && isFiltered) {
            return
        }

        listAdapter.submitList(list)
    }

    private fun navigateToPokedexDetails(regionName: String) {

        val bundle = Bundle()
        bundle.putString("REGION_NAME", regionName)

        val regionDetailsFragment = RegionDetailsFragment()
        regionDetailsFragment.arguments = bundle

        val fragmentTransaction = requireActivity().supportFragmentManager.beginTransaction()

        fragmentTransaction.replaceFragment(regionDetailsFragment, R.id.control_fragment_container)
    }
}