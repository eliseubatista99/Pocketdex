package com.eliseubatista.pocketdex.fragments.items

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.eliseubatista.pocketdex.R
import com.eliseubatista.pocketdex.database.items.DatabaseItems
import com.eliseubatista.pocketdex.databinding.FragmentItemDetailsAboutBinding
import com.eliseubatista.pocketdex.databinding.FragmentItemDetailsBinding
import com.eliseubatista.pocketdex.fragments.pokemon.PokedexDetailsAboutFragment
import com.eliseubatista.pocketdex.fragments.pokemon.PokedexDetailsEvolutionsFragment
import com.eliseubatista.pocketdex.fragments.pokemon.PokedexDetailsStatsFragment
import com.eliseubatista.pocketdex.utils.*
import com.eliseubatista.pocketdex.views.SectionPagerAdapter
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class ItemDetailsFragment : Fragment() {
    private var itemName = ""
    private lateinit var viewModel: ItemDetailsViewModel
    private lateinit var viewModelFactory: ItemDetailsViewModel.Factory

    private lateinit var progressBarDialog: ProgressBarDialog

    private lateinit var binding: FragmentItemDetailsBinding


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_item_details, container, false)

        setUpViewPagerAndTabLayout()

        //Get the item name from the bundle
        val bundle = requireArguments()
        itemName = bundle.getString("ITEM_NAME", "")

        viewModelFactory =
            ItemDetailsViewModel.Factory(requireActivity().application, itemName)
        viewModel =
            ViewModelProvider(this, viewModelFactory)[ItemDetailsViewModel::class.java]

        progressBarDialog = ProgressBarDialog(inflater, requireContext())

        viewModel.isInFavorites.observe(
            viewLifecycleOwner
        ) { inFavorites ->
            refreshFavorites(inFavorites)
        }

        viewModel.isLoadingItem.observe(viewLifecycleOwner) {
            if (it) {
                progressBarDialog.startLoading("Loading Item..")
            } else {
                progressBarDialog.dismiss()
            }
        }

        viewModel.item.observe(
            viewLifecycleOwner
        ) { item -> refreshItemHeader(item) }

        binding.toolbarItemDetails.favorite.setOnClickListener {
            viewModel.addOrRemoveFavorite()
        }
        binding.toolbarItemDetails.arrowBack.setOnClickListener { parentFragmentManager.popBackStack() }

        return binding.root
    }

    private fun setUpViewPagerAndTabLayout() {
        val viewPager = binding.itemDetailsViewPager
        val tabLayout = binding.itemDetailsTabLayout

        val adapter = SectionPagerAdapter(childFragmentManager, lifecycle)
        adapter.addFragment(ItemDetailsAboutFragment(), "About")

        viewPager.adapter = adapter

        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.text = adapter.getPageTitle(position)
        }.attach()

        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {
            }


        })

    }

    private fun refreshItemHeader(
        item: DatabaseItems
    ) {

        //val imageScale = getImageScaleByEvolutionChain(pokemon.name, pokemon.evolutionChain)
        val imageScale = 1.0f

        binding.itemDetailsAvatar.scaleX = imageScale
        binding.itemDetailsAvatar.scaleY = imageScale

        loadImageWithGlide(item.spriteUrl, binding.itemDetailsAvatar)

        val itemColor = getItemBackgroundColor(requireContext(), item.id)
        val textColor = getTextColorByBackgroundColor(requireContext(), itemColor)

        binding.itemDetailsBackground.setColorFilter(itemColor)

        binding.toolbarItemDetails.detailsName.text =
            formatPocketdexObjectName(formatPocketdexObjectName(item.name))

        binding.toolbarItemDetails.detailsId.text = "#${item.id}"

        binding.toolbarItemDetails.detailsName.setTextColor(
            getTextColorByBackgroundColor(
                requireContext(),
                itemColor
            )
        )

        binding.toolbarItemDetails.detailsId.setTextColor(
            getTextColorByBackgroundColor(
                requireContext(),
                itemColor
            )
        )

        binding.toolbarItemDetails.arrowBack.setColorFilter(textColor)
        binding.toolbarItemDetails.favorite.setColorFilter(textColor)
    }

    private fun refreshFavorites(isInFavorites: Boolean) {
        if (isInFavorites) {
            binding.toolbarItemDetails.favorite.setImageResource(R.drawable.ic_star_fill)
        } else {
            binding.toolbarItemDetails.favorite.setImageResource(R.drawable.ic_star_border)
        }
    }

}