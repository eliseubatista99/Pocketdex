package com.eliseubatista.pocketdex.fragments.profile

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
import com.eliseubatista.pocketdex.database.regions.DatabaseRegions
import com.eliseubatista.pocketdex.databinding.FragmentProfileRegionsBinding
import com.eliseubatista.pocketdex.fragments.regions.RegionDetailsFragment
import com.eliseubatista.pocketdex.utils.addFragment
import com.eliseubatista.pocketdex.utils.replaceFragment
import com.eliseubatista.pocketdex.views.regions.OnRegionClickedListener
import com.eliseubatista.pocketdex.views.regions.RegionAdapter

class ProfileRegionsFragment : Fragment() {

    private lateinit var viewModel: ProfileViewModel
    private lateinit var viewModelFactory: ProfileViewModel.Factory

    private lateinit var binding: FragmentProfileRegionsBinding

    private lateinit var regionsListAdapter: RegionAdapter
    private var isFiltered = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_profile_regions, container, false)

        viewModelFactory =
            ProfileViewModel.Factory(requireActivity().application)
        viewModel =
            ViewModelProvider(
                requireParentFragment(),
                viewModelFactory
            )[ProfileViewModel::class.java]

        regionsListAdapter = RegionAdapter()

        setupLinearRecyclerView()

        //Create and observer to refresh the list automatically
        viewModel.favoriteLocationsChanged.observe(viewLifecycleOwner) {
            viewModel.getRegions()
        }

        viewModel.listOfRegions.observe(viewLifecycleOwner) {
            updateList(it)
        }

        return binding.root
    }

    private fun setupLinearRecyclerView() {

        //Create and add the layout manager
        val linearLayoutManager = LinearLayoutManager(context)
        linearLayoutManager.orientation = RecyclerView.VERTICAL
        binding.favoriteRegionsList.layoutManager = linearLayoutManager

        //Bind the list adapter
        binding.favoriteRegionsList.adapter = regionsListAdapter

        //Click listener
        regionsListAdapter.onRegionClickedListener =
            object : OnRegionClickedListener {
                override fun onRegionClicked(name: String) {
                    navigateToRegionDetails(name)
                }

            }

        /*Scroll listener
        binding.pokemonsList.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)

                //If we reach the end of the list, we want to show more pokemons
                //TODO: Check scroll
                if (!recyclerView.canScrollVertically(3) && !isFiltered) {
                    viewModel.getMorePokemons()
                }
            }
        })
        */
    }

    private fun updateList(list: List<DatabaseRegions>?) {

        if (list == null) {
            return
        }

        regionsListAdapter.submitList(list)
    }

    private fun navigateToRegionDetails(name: String) {

        val bundle = Bundle()
        bundle.putString("REGION_NAME", name)

        val regionDetailsFragment = RegionDetailsFragment()
        regionDetailsFragment.arguments = bundle

        val fragmentTransaction = requireActivity().supportFragmentManager.beginTransaction()

        fragmentTransaction.replaceFragment(regionDetailsFragment, R.id.control_fragment_container)
    }
}