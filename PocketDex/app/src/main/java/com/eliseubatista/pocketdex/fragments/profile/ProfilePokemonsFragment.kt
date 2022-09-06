package com.eliseubatista.pocketdex.fragments.profile

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.eliseubatista.pocketdex.R
import com.eliseubatista.pocketdex.database.pokemons.DatabasePokemon
import com.eliseubatista.pocketdex.databinding.FragmentProfilePokemonsBinding
import com.eliseubatista.pocketdex.fragments.pokemon.PokedexDetailsFragment
import com.eliseubatista.pocketdex.utils.addFragment
import com.eliseubatista.pocketdex.utils.replaceFragment
import com.eliseubatista.pocketdex.views.pokemons.OnPokemonClickedListener
import com.eliseubatista.pocketdex.views.pokemons.PokemonAdapter

class ProfilePokemonsFragment : Fragment() {

    private lateinit var viewModel: ProfileViewModel
    private lateinit var viewModelFactory: ProfileViewModel.Factory

    private lateinit var binding: FragmentProfilePokemonsBinding

    private lateinit var pokemonListAdapter: PokemonAdapter
    private var isFiltered = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_profile_pokemons, container, false)

        viewModelFactory =
            ProfileViewModel.Factory(requireActivity().application)
        viewModel =
            ViewModelProvider(
                requireParentFragment(),
                viewModelFactory
            )[ProfileViewModel::class.java]

        pokemonListAdapter = PokemonAdapter()

        setupLinearRecyclerView()

        //Create and observer to refresh the list automatically
        viewModel.favoritePokemonsChanged.observe(viewLifecycleOwner) {
            viewModel.getPokemons()
        }

        viewModel.listOfPokemons.observe(viewLifecycleOwner) {
            updateList(it)
        }

        return binding.root
    }

    private fun setupLinearRecyclerView() {

        //Create and add the layout manager
        val linearLayoutManager = LinearLayoutManager(context)
        linearLayoutManager.orientation = RecyclerView.VERTICAL
        binding.favoritePokemonsList.layoutManager = linearLayoutManager

        //Bind the list adapter
        binding.favoritePokemonsList.adapter = pokemonListAdapter

        //Click listener
        pokemonListAdapter.onPokemonClickedListener =
            object : OnPokemonClickedListener {
                override fun onPokemonClicked(pokemonName: String) {
                    navigateToPokedexDetails(pokemonName)
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

    private fun updateList(list: List<DatabasePokemon>?) {

        if (list == null) {
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