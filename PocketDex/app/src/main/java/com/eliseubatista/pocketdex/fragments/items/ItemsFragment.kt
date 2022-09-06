package com.eliseubatista.pocketdex.fragments.items

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
import com.eliseubatista.pocketdex.database.items.DatabaseItems
import com.eliseubatista.pocketdex.database.pokemons.DatabasePokemon
import com.eliseubatista.pocketdex.databinding.FragmentItemsBinding
import com.eliseubatista.pocketdex.utils.ProgressBarDialog
import com.eliseubatista.pocketdex.utils.replaceFragment
import com.eliseubatista.pocketdex.views.items.ItemsListAdapter
import com.eliseubatista.pocketdex.views.items.OnItemClickedListener

class ItemsFragment : Fragment() {

    private lateinit var viewModel: ItemsViewModel
    private lateinit var viewModelFactory: ItemsViewModel.Factory
    private lateinit var progressBarDialog: ProgressBarDialog

    private lateinit var binding: FragmentItemsBinding

    private lateinit var itemListAdapter: ItemsListAdapter
    private var isFiltered = false

    private var listOfPokemons = listOf<DatabasePokemon>()
    private var listOfSearchedPokemons = listOf<DatabasePokemon>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_items, container, false)

        viewModelFactory = ItemsViewModel.Factory(requireActivity().application)
        viewModel =
            ViewModelProvider(this, viewModelFactory)[ItemsViewModel::class.java]

        itemListAdapter = ItemsListAdapter()
        progressBarDialog = ProgressBarDialog(inflater, requireContext())

        setupSearchView()
        setupLinearRecyclerView()

        //Create and observer to refresh the list automatically
        viewModel.items.observe(viewLifecycleOwner) {
            updateList(it, false)
        }

        //Create and observer to refresh the list automatically
        viewModel.searchedItems.observe(viewLifecycleOwner) {
            updateList(it, true)
        }

        return binding.root
    }

    private fun setupSearchView() {
        binding.itemsSearch.searchBar.queryHint = "Type Item Name Here"

        binding.itemsSearch.searchBar.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (query.isNullOrEmpty() || query.isNullOrBlank()) {
                    isFiltered = false
                    updateList(viewModel.items.value, false)
                    return false
                }

                isFiltered = true
                viewModel.getItemsLikeName(query, false)

                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if (newText.isNullOrEmpty() || newText.isNullOrBlank()) {
                    isFiltered = false
                    updateList(viewModel.items.value, false)
                    return false
                }

                isFiltered = true
                viewModel.getItemsLikeName(newText, true)

                return true
            }

        })
    }

    private fun setupLinearRecyclerView() {

        //Create and add the layout manager
        val linearLayoutManager = LinearLayoutManager(context)
        linearLayoutManager.orientation = RecyclerView.VERTICAL
        binding.itemsList.layoutManager = linearLayoutManager

        //Bind the list adapter
        binding.itemsList.adapter = itemListAdapter

        //Click listener
        itemListAdapter.onItemClickedListener =
            object : OnItemClickedListener {
                override fun onItemClicked(name: String) {
                    navigateToItemDetails(name)
                }

            }

        viewModel.isLoadingMoreItems.observe(viewLifecycleOwner) {
            if (it) {
                progressBarDialog.startLoading("Loading More Items..")
            } else {
                progressBarDialog.dismiss()
            }
        }

        //Scroll listener
        binding.itemsList.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)

                //If we reach the end of the list, we want to show more pokemons
                //TODO: Check scroll
                if (!recyclerView.canScrollVertically(3) && !isFiltered) {
                    viewModel.getMoreItems()
                }
            }
        })
    }

    private fun updateList(list: List<DatabaseItems>?, isFromSearchBar: Boolean) {

        if (list == null) {
            return
        }

        if (!isFromSearchBar && isFiltered) {
            return
        }

        itemListAdapter.submitList(list)
    }

    private fun navigateToItemDetails(name: String) {

        val bundle = Bundle()
        bundle.putString("ITEM_NAME", name)

        val itemDetailsFragment = ItemDetailsFragment()
        itemDetailsFragment.arguments = bundle

        val fragmentTransaction = requireActivity().supportFragmentManager.beginTransaction()

        fragmentTransaction.replaceFragment(itemDetailsFragment, R.id.control_fragment_container)
    }
}