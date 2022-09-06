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
import com.eliseubatista.pocketdex.databinding.FragmentPokedexDetailsBinding
import com.eliseubatista.pocketdex.utils.*
import com.eliseubatista.pocketdex.views.SectionPagerAdapter
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayout.OnTabSelectedListener
import com.google.android.material.tabs.TabLayoutMediator

class PokedexDetailsFragment : Fragment() {
    private var pokemonName = ""
    private lateinit var viewModel: PokemonDetailsViewModel
    private lateinit var viewModelFactory: PokemonDetailsViewModel.Factory

    private lateinit var progressBarDialog: ProgressBarDialog


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding: FragmentPokedexDetailsBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_pokedex_details, container, false)

        setUpViewPagerAndTabLayout(binding)

        val bundle = requireArguments()
        pokemonName = bundle.getString("POKEMON_NAME", "")

        viewModelFactory =
            PokemonDetailsViewModel.Factory(requireActivity().application, pokemonName)
        viewModel =
            ViewModelProvider(this, viewModelFactory)[PokemonDetailsViewModel::class.java]

        progressBarDialog = ProgressBarDialog(inflater, requireContext())

        viewModel.isInFavorites.observe(
            viewLifecycleOwner
        ) { inFavorites ->
            refreshFavorites(binding, inFavorites)
        }

        viewModel.isLoadingPokemon.observe(viewLifecycleOwner) {
            if (it) {
                progressBarDialog.startLoading("Loading Pokemon..")
            } else {
                progressBarDialog.dismiss()
            }
        }

        viewModel.pokemon.observe(
            viewLifecycleOwner
        ) { pokemon -> refreshPokemonHeader(binding, pokemon) }

        binding.toolbarPokedexDetails.favorite.setOnClickListener {
            viewModel.addOrRemoveFavorite()
        }
        binding.toolbarPokedexDetails.arrowBack.setOnClickListener { parentFragmentManager.popBackStack() }

        return binding.root
    }

    private fun setUpViewPagerAndTabLayout(binding: FragmentPokedexDetailsBinding) {
        val viewPager = binding.pokedexDetailsViewPager
        val tabLayout = binding.pokedexDetailsTabLayout

        val adapter = SectionPagerAdapter(childFragmentManager, lifecycle)
        adapter.addFragment(PokedexDetailsAboutFragment(), "About")
        adapter.addFragment(PokedexDetailsStatsFragment(), "Stats")
        adapter.addFragment(PokedexDetailsEvolutionsFragment(), "Evolutions")

        viewPager.adapter = adapter

        TabLayoutMediator(tabLayout.tabLayout, viewPager) { tab, position ->
            tab.text = adapter.getPageTitle(position)
        }.attach()

        tabLayout.tabLayout.addOnTabSelectedListener(object : OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {
            }


        })

    }

    private fun refreshPokemonHeader(
        binding: FragmentPokedexDetailsBinding,
        pokemon: DatabasePokemon
    ) {

        val imageScale = getImageScaleByEvolutionChain(pokemon.name, pokemon.evolutionChain)

        binding.pokemonDetailsAvatar.scaleX = imageScale
        binding.pokemonDetailsAvatar.scaleY = imageScale

        loadImageWithGlide(pokemon.spriteUrl, binding.pokemonDetailsAvatar)

        val pokemonColor = getPokemonBackgroundColor(requireContext(), pokemon.color)
        val textColor = getTextColorByBackgroundColor(requireContext(), pokemonColor)

        binding.pokemonDetailsBackground.setColorFilter(pokemonColor)

        binding.toolbarPokedexDetails.detailsName.text =
            formatPocketdexObjectName(pokemon.name)

        binding.toolbarPokedexDetails.detailsId.text = "#${pokemon.id}"

        binding.toolbarPokedexDetails.detailsName.setTextColor(
            getTextColorByBackgroundColor(
                requireContext(),
                pokemonColor
            )
        )

        binding.toolbarPokedexDetails.detailsId.setTextColor(
            getTextColorByBackgroundColor(
                requireContext(),
                pokemonColor
            )
        )

        binding.toolbarPokedexDetails.arrowBack.setColorFilter(textColor)
        binding.toolbarPokedexDetails.favorite.setColorFilter(textColor)
    }

    private fun refreshFavorites(binding: FragmentPokedexDetailsBinding, isInFavorites: Boolean) {
        if (isInFavorites) {
            binding.toolbarPokedexDetails.favorite.setImageResource(R.drawable.ic_star_fill)
        } else {
            binding.toolbarPokedexDetails.favorite.setImageResource(R.drawable.ic_star_border)
        }
    }

}