package com.eliseubatista.pocketdex.fragments.pokemon

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.eliseubatista.pocketdex.R
import com.eliseubatista.pocketdex.database.pokemons.DatabasePokemon
import com.eliseubatista.pocketdex.databinding.FragmentPokedexDetailsAboutBinding
import com.eliseubatista.pocketdex.utils.*

class PokedexDetailsAboutFragment : Fragment() {

    private var pokemonName = ""
    private lateinit var viewModel: PokemonDetailsViewModel
    private lateinit var viewModelFactory: PokemonDetailsViewModel.Factory

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding: FragmentPokedexDetailsAboutBinding =
            DataBindingUtil.inflate(
                inflater,
                R.layout.fragment_pokedex_details_about,
                container,
                true
            )

        viewModelFactory =
            PokemonDetailsViewModel.Factory(requireActivity().application, pokemonName)

        viewModel =
            ViewModelProvider(
                requireParentFragment(),
                viewModelFactory
            )[PokemonDetailsViewModel::class.java]

        viewModel.pokemon.observe(
            viewLifecycleOwner
        ) { pokemon -> refreshPokemonAbout(binding, pokemon) }

        return binding.root
    }


    private fun refreshPokemonAbout(
        binding: FragmentPokedexDetailsAboutBinding,
        pokemon: DatabasePokemon
    ) {
        val pokemonColor = getPokemonBackgroundColor(requireContext(), pokemon.color)

        binding.pokemonDetailsDescriptionText.text =
            formatPocketdexObjectDescription(pokemon.flavor)

        binding.pokemonDetailsAbout.pokemonDetailsBar1.setColorFilter(pokemonColor)
        binding.pokemonDetailsAbout.pokemonDetailsBar2.setColorFilter(pokemonColor)

        binding.pokemonDetailsAbout.speciesValue.text = formatPokemonGenus(pokemon.genus)

        binding.pokemonDetailsAbout.heightValue.text = formatPokemonHeight(pokemon.height)

        binding.pokemonDetailsAbout.weightValue.text = formatPokemonWeight(pokemon.weight)

        val typeOneLogo = getPokemonTypeLogoImage(requireContext(), pokemon.types[0])
        val typeOneTextImage = getPokemonTypeTextImage(requireContext(), pokemon.types[0])

        binding.pokemonDetailsTypes.typeOneValue.typeLogo.setImageDrawable(typeOneLogo)
        binding.pokemonDetailsTypes.typeOneValue.typeTextImage.setImageDrawable(typeOneTextImage)

        if (pokemon.types.size > 1) {

            val typeTwoLogo = getPokemonTypeLogoImage(requireContext(), pokemon.types[1])
            val typeTwoTextImage = getPokemonTypeTextImage(requireContext(), pokemon.types[1])

            binding.pokemonDetailsTypes.typeTwoValue.typeLogo.setImageDrawable(typeTwoLogo)
            binding.pokemonDetailsTypes.typeTwoValue.typeTextImage.setImageDrawable(
                typeTwoTextImage
            )

            binding.pokemonDetailsTypes.typeOneContainer.visibility = View.VISIBLE
            binding.pokemonDetailsTypes.typeTwoContainer.visibility = View.VISIBLE
        } else {
            binding.pokemonDetailsTypes.typeOneContainer.visibility = View.VISIBLE
            binding.pokemonDetailsTypes.typeTwoContainer.visibility = View.GONE

        }
    }
}