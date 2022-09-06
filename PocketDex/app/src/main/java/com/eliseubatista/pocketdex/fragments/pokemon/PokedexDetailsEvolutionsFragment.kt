package com.eliseubatista.pocketdex.fragments.pokemon

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.eliseubatista.pocketdex.R
import com.eliseubatista.pocketdex.database.pokemons.DatabasePokemon
import com.eliseubatista.pocketdex.databinding.FragmentPokedexDetailsEvolutionsBinding
import com.eliseubatista.pocketdex.databinding.ItemPokemonEvolutionListBinding
import com.eliseubatista.pocketdex.utils.getImageScaleByEvolutionChain
import com.eliseubatista.pocketdex.utils.getPokemonBackgroundColor
import com.eliseubatista.pocketdex.utils.loadImageWithGlide

class PokedexDetailsEvolutionsFragment : Fragment() {

    private var pokemonName = ""
    private lateinit var viewModel: PokemonDetailsViewModel
    private lateinit var viewModelFactory: PokemonDetailsViewModel.Factory

    private lateinit var fragmentInflater: LayoutInflater

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding: FragmentPokedexDetailsEvolutionsBinding =
            DataBindingUtil.inflate(
                inflater,
                R.layout.fragment_pokedex_details_evolutions,
                container,
                true
            )

        fragmentInflater = inflater

        viewModelFactory =
            PokemonDetailsViewModel.Factory(requireActivity().application, pokemonName)
        viewModel =
            ViewModelProvider(
                requireParentFragment(),
                viewModelFactory
            )[PokemonDetailsViewModel::class.java]

        viewModel.pokemon.observe(
            viewLifecycleOwner
        ) { pokemon -> refreshPokemonEvolutions(binding, pokemon) }

        return binding.root
    }

    private fun refreshPokemonEvolutions(
        binding: FragmentPokedexDetailsEvolutionsBinding,
        pokemon: DatabasePokemon
    ) {

        val pokemonColor = getPokemonBackgroundColor(requireContext(), pokemon.color)
        binding.pokemonDetailsEvolutions.evolutionFixedText.setTextColor(pokemonColor)

        if (viewModel.pokeEvolutionChain.size < 1) {
            binding.pokemonDetailsEvolutions.evolutionFixedText.visibility = View.GONE
        } else {
            binding.pokemonDetailsEvolutions.evolutionFixedText.visibility = View.VISIBLE
        }

        binding.pokemonDetailsEvolutions.evolutionsLinearLayout.removeAllViews()

        addEvolutions(
            viewModel.pokeEvolutionChain,
            binding.pokemonDetailsEvolutions.evolutionsLinearLayout
        )
    }


    private fun addEvolutions(
        pokeEvolutionChain: List<List<DatabasePokemon>>,
        layout: LinearLayout
    ) {
        for (chain in pokeEvolutionChain) {

            val evolutionBinding: ItemPokemonEvolutionListBinding =
                DataBindingUtil.inflate(
                    fragmentInflater,
                    R.layout.item_pokemon_evolution_list,
                    layout,
                    true
                )

            setupEvolutionChain(evolutionBinding, chain)
        }
    }

    private fun setupEvolutionChain(
        binding: ItemPokemonEvolutionListBinding,
        chain: List<DatabasePokemon>
    ) {
        when (chain.size) {
            1 -> {
                binding.evolutionOfTwoContainer.visibility = View.GONE
                binding.evolutionOfThreeContainer.visibility = View.GONE
            }
            2 -> {
                binding.evolutionOfTwoContainer.visibility = View.VISIBLE
                binding.evolutionOfThreeContainer.visibility = View.GONE
            }
            else -> {
                binding.evolutionOfTwoContainer.visibility = View.GONE
                binding.evolutionOfThreeContainer.visibility = View.VISIBLE
            }
        }

        for ((index, evo) in chain.withIndex()) {
            val imageViewOfTwo = when (index) {
                0 -> binding.evolutionOfTwo.baseForm
                else -> binding.evolutionOfTwo.evolution
            }

            val imageViewOfThree = when (index) {
                0 -> binding.evolutionOfThree.baseForm
                1 -> binding.evolutionOfThree.evolution1
                else -> binding.evolutionOfThree.evolution2
            }

            val imageScale = getImageScaleByEvolutionChain(
                evo.name,
                evo.evolutionChain
            )

            imageViewOfTwo.scaleX = imageScale
            imageViewOfTwo.scaleY = imageScale

            imageViewOfThree.scaleX = imageScale
            imageViewOfThree.scaleY = imageScale

            loadImageWithGlide(evo.spriteUrl, imageViewOfTwo)
            loadImageWithGlide(evo.spriteUrl, imageViewOfThree)
        }
    }
}