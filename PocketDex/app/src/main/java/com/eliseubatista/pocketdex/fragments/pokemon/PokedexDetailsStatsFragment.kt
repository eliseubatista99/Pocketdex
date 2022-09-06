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
import com.eliseubatista.pocketdex.database.pokemons.DatabaseTypes
import com.eliseubatista.pocketdex.databinding.FragmentPokedexDetailsStatsBinding
import com.eliseubatista.pocketdex.databinding.ItemDamageRelationBinding
import com.eliseubatista.pocketdex.databinding.ItemPokemonTypeSmallBinding
import com.eliseubatista.pocketdex.utils.getPokemonBackgroundColor
import com.eliseubatista.pocketdex.utils.getPokemonTypeLogoImage
import com.eliseubatista.pocketdex.utils.isPokemonDamageRelationEmpty

class PokedexDetailsStatsFragment : Fragment() {

    private var pokemonName = ""
    private lateinit var viewModel: PokemonDetailsViewModel
    private lateinit var viewModelFactory: PokemonDetailsViewModel.Factory

    private lateinit var fragmentInflater: LayoutInflater


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding: FragmentPokedexDetailsStatsBinding =
            DataBindingUtil.inflate(
                inflater,
                R.layout.fragment_pokedex_details_stats,
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
        ) { pokemon -> refreshPokemonData(binding, pokemon, viewModel.pokeFirstType) }

        return binding.root
    }

    private fun refreshPokemonData(
        binding: FragmentPokedexDetailsStatsBinding,
        pokemon: DatabasePokemon,
        type: DatabaseTypes
    ) {
        refreshPokemonStats(binding, pokemon)
        refreshDamageRelations(binding, pokemon, type)
    }

    private fun refreshPokemonStats(
        binding: FragmentPokedexDetailsStatsBinding,
        pokemon: DatabasePokemon
    ) {
        val pokemonColor = getPokemonBackgroundColor(requireContext(), pokemon.color)

        binding.pokemonDetailsStats.baseStatsFixedText.setTextColor(pokemonColor)

        binding.pokemonDetailsStats.hpValue.text = pokemon.hp.toString()
        binding.pokemonDetailsStats.attackValue.text = pokemon.attack.toString()
        binding.pokemonDetailsStats.defenseValue.text = pokemon.defense.toString()
        binding.pokemonDetailsStats.spAttackValue.text = pokemon.specialAttack.toString()
        binding.pokemonDetailsStats.spDefenseValue.text = pokemon.specialDefense.toString()
        binding.pokemonDetailsStats.speedValue.text = pokemon.speed.toString()
    }


    private fun refreshDamageRelations(
        binding: FragmentPokedexDetailsStatsBinding,
        pokemon: DatabasePokemon,
        type: DatabaseTypes
    ) {
        val pokemonColor = getPokemonBackgroundColor(requireContext(), pokemon.color)

        binding.pokemonDetailsDamages.attackFixedText.setTextColor(pokemonColor)
        binding.pokemonDetailsDamages.defensesFixedText.setTextColor(pokemonColor)

        binding.pokemonDetailsDamages.defenseLinearLayout.removeAllViews()
        binding.pokemonDetailsDamages.attackLinearLayout.removeAllViews()

        addDamageRelations(
            0,
            type.doubleDamageFrom,
            binding.pokemonDetailsDamages.defenseLinearLayout
        )
        addDamageRelations(
            50,
            type.halfDamageFrom,
            binding.pokemonDetailsDamages.defenseLinearLayout
        )
        addDamageRelations(
            100,
            type.noDamageFrom,
            binding.pokemonDetailsDamages.defenseLinearLayout
        )
        addDamageRelations(
            100,
            type.doubleDamageTo,
            binding.pokemonDetailsDamages.attackLinearLayout
        )
        addDamageRelations(
            50,
            type.halfDamageTo,
            binding.pokemonDetailsDamages.attackLinearLayout
        )
        addDamageRelations(
            0,
            type.noDamageTo,
            binding.pokemonDetailsDamages.attackLinearLayout
        )
    }

    private fun addDamageRelations(
        relationsValue: Int,
        damageRelations: List<String>,
        layout: LinearLayout
    ) {
        val relationBinding: ItemDamageRelationBinding =
            DataBindingUtil.inflate(
                fragmentInflater,
                R.layout.item_damage_relation,
                layout,
                true
            )

        relationBinding.fixedText.text = "${relationsValue}%"

        relationBinding.typesGrid.removeAllViews()

        for (relation in damageRelations) {

            val pokeTypeBinding: ItemPokemonTypeSmallBinding =
                DataBindingUtil.inflate(
                    fragmentInflater,
                    R.layout.item_pokemon_type_small,
                    relationBinding.typesGrid,
                    true
                )

            val pokeTypeLogo = getPokemonTypeLogoImage(requireContext(), relation)
            pokeTypeBinding.typeLogo.setImageDrawable(pokeTypeLogo)
        }

        if (isPokemonDamageRelationEmpty(damageRelations)) {
            relationBinding.damageTypesContainer.visibility = View.GONE
        }
    }
}