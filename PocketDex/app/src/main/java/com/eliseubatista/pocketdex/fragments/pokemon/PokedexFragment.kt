package com.eliseubatista.pocketdex.fragments.pokemon

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
import com.eliseubatista.pocketdex.database.pokemons.DatabasePokemon
import com.eliseubatista.pocketdex.databinding.FragmentPokedexBinding
import com.eliseubatista.pocketdex.utils.ProgressBarDialog
import com.eliseubatista.pocketdex.utils.replaceFragment
import com.eliseubatista.pocketdex.views.pokemons.OnPokemonClickedListener
import com.eliseubatista.pocketdex.views.pokemons.PokemonAdapter

class PokedexFragment : Fragment() {

    private lateinit var viewModel: PokedexViewModel
    private lateinit var viewModelFactory: PokedexViewModel.Factory
    private lateinit var progressBarDialog: ProgressBarDialog

    private lateinit var binding: FragmentPokedexBinding

    private lateinit var pokemonListAdapter: PokemonAdapter
    private var isFiltered = false

    private var listOfPokemons = listOf<DatabasePokemon>()
    private var listOfSearchedPokemons = listOf<DatabasePokemon>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_pokedex, container, false)

        viewModelFactory = PokedexViewModel.Factory(requireActivity().application)
        viewModel =
            ViewModelProvider(this, viewModelFactory)[PokedexViewModel::class.java]

        pokemonListAdapter = PokemonAdapter()
        progressBarDialog = ProgressBarDialog(inflater, requireContext())

        setupSearchView()
        setupLinearRecyclerView()

        //Create and observer to refresh the list automatically
        viewModel.pokemons.observe(viewLifecycleOwner) {
            updateList(it, false)
        }

        //Create and observer to refresh the list automatically
        viewModel.searchedPokemons.observe(viewLifecycleOwner) {
            updateList(it, true)
/*
            if (it.isEmpty()) {
                isFiltered = false
                updateList(viewModel.pokemons.value, false)
            } else {
            }

 */
        }

        return binding.root
    }

    private fun setupSearchView() {
        binding.pokedexSearch.searchBar.queryHint = "Type Pokémon Name Here"

        binding.pokedexSearch.searchBar.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (query.isNullOrEmpty() || query.isNullOrBlank()) {
                    isFiltered = false
                    updateList(viewModel.pokemons.value, false)
                    return false
                }

                isFiltered = true
                viewModel.getPokemonLikeName(query, false)

                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if (newText.isNullOrEmpty() || newText.isNullOrBlank()) {
                    isFiltered = false
                    updateList(viewModel.pokemons.value, false)
                    return false
                }

                isFiltered = true
                viewModel.getPokemonLikeName(newText, true)

                return true
            }

        })
    }

    private fun setupLinearRecyclerView() {

        //Create and add the layout manager
        val linearLayoutManager = LinearLayoutManager(context)
        linearLayoutManager.orientation = RecyclerView.VERTICAL
        binding.pokemonsList.layoutManager = linearLayoutManager

        //Bind the list adapter
        binding.pokemonsList.adapter = pokemonListAdapter

        //Click listener
        pokemonListAdapter.onPokemonClickedListener =
            object : OnPokemonClickedListener {
                override fun onPokemonClicked(pokemonName: String) {
                    navigateToPokedexDetails(pokemonName)
                }

            }

        viewModel.isLoadingMorePokemons.observe(viewLifecycleOwner) {
            if (it) {
                progressBarDialog.startLoading("Loading More Pokemons..")
            } else {
                progressBarDialog.dismiss()
            }
        }

        //Scroll listener
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
    }

    private fun updateList(list: List<DatabasePokemon>?, isFromSearchBar: Boolean) {

        if (list == null) {
            return
        }

        if (!isFromSearchBar && isFiltered) {
            return
        }

        pokemonListAdapter.submitList(list)
    }

    private fun navigateToPokedexDetails(pokemonName: String) {

        val bundle = Bundle()
        bundle.putString("POKEMON_NAME", pokemonName)

        val pokedexDetailsFragment = PokedexDetailsFragment()
        pokedexDetailsFragment.arguments = bundle

        val fragmentTransaction = requireActivity().supportFragmentManager.beginTransaction()

        fragmentTransaction.replaceFragment(pokedexDetailsFragment, R.id.control_fragment_container)
    }
}