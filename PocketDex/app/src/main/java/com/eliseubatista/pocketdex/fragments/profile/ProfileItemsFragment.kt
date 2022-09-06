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
import com.eliseubatista.pocketdex.database.items.DatabaseItems
import com.eliseubatista.pocketdex.databinding.FragmentProfileItemsBinding
import com.eliseubatista.pocketdex.fragments.items.ItemDetailsFragment
import com.eliseubatista.pocketdex.utils.ProgressBarDialog
import com.eliseubatista.pocketdex.utils.addFragment
import com.eliseubatista.pocketdex.utils.replaceFragment
import com.eliseubatista.pocketdex.views.items.ItemsListAdapter
import com.eliseubatista.pocketdex.views.items.OnItemClickedListener

class ProfileItemsFragment : Fragment() {

    private lateinit var viewModel: ProfileViewModel
    private lateinit var viewModelFactory: ProfileViewModel.Factory
    private lateinit var progressBarDialog: ProgressBarDialog

    private lateinit var binding: FragmentProfileItemsBinding

    private lateinit var itemListAdapter: ItemsListAdapter
    private var isFiltered = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_profile_items, container, false)


        viewModelFactory =
            ProfileViewModel.Factory(requireActivity().application)
        viewModel =
            ViewModelProvider(
                requireParentFragment(),
                viewModelFactory
            )[ProfileViewModel::class.java]

        itemListAdapter = ItemsListAdapter()
        progressBarDialog = ProgressBarDialog(inflater, requireContext())

        setupLinearRecyclerView()

        //Create and observer to refresh the list automatically
        viewModel.favoriteItemsChanged.observe(viewLifecycleOwner) {
            viewModel.getItems()
        }

        viewModel.listOfItems.observe(viewLifecycleOwner) {
            updateList(it)
        }

        return binding.root
    }

    private fun setupLinearRecyclerView() {

        //Create and add the layout manager
        val linearLayoutManager = LinearLayoutManager(context)
        linearLayoutManager.orientation = RecyclerView.VERTICAL
        binding.favoriteItemsList.layoutManager = linearLayoutManager

        //Bind the list adapter
        binding.favoriteItemsList.adapter = itemListAdapter

        //Click listener
        itemListAdapter.onItemClickedListener =
            object : OnItemClickedListener {
                override fun onItemClicked(name: String) {
                    navigateToItemDetails(name)
                }

            }
    }

    private fun updateList(list: List<DatabaseItems>?) {

        if (list == null) {
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