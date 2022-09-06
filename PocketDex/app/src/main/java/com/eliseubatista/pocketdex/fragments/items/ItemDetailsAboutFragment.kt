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
import com.eliseubatista.pocketdex.databinding.FragmentRegionsBinding
import com.eliseubatista.pocketdex.utils.*

class ItemDetailsAboutFragment : Fragment() {

    private var itemName = ""
    private lateinit var viewModel: ItemDetailsViewModel
    private lateinit var viewModelFactory: ItemDetailsViewModel.Factory
    private lateinit var binding: FragmentItemDetailsAboutBinding


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_item_details_about,
            container,
            true
        )

        viewModelFactory =
            ItemDetailsViewModel.Factory(requireActivity().application, itemName)
        viewModel =
            ViewModelProvider(
                requireParentFragment(),
                viewModelFactory
            )[ItemDetailsViewModel::class.java]

        //Once we get the needed information, we refresh the section
        viewModel.item.observe(
            viewLifecycleOwner
        ) { item -> refreshItemAbout(item) }

        return binding.root
    }


    private fun refreshItemAbout(
        item: DatabaseItems
    ) {
        //Get the item color by its id
        val itemColor = getItemBackgroundColor(requireContext(), item.id)

        binding.itemDetailsDescriptionText.text =
            formatPocketdexObjectDescription(item.flavor)

        binding.itemDetailsCategory.text.text = formatPocketdexObjectName(item.category)
        binding.itemDetailsCategory.background.setColorFilter(itemColor)
        binding.itemDetailsCategory.background.alpha = 1.0f

    }
}