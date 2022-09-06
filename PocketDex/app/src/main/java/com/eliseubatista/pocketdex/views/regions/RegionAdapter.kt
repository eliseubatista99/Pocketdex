package com.eliseubatista.pocketdex.views.regions

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.eliseubatista.pocketdex.database.regions.DatabaseRegions
import com.eliseubatista.pocketdex.databinding.ItemRegionListBinding
import com.eliseubatista.pocketdex.utils.formatPocketdexObjectName
import com.eliseubatista.pocketdex.utils.getLocationRegionImage

class RegionAdapter :
    ListAdapter<DatabaseRegions, RegionAdapter.ViewHolder>(RegionDiffCallback()) {

    lateinit var onRegionClickedListener: OnRegionClickedListener

    //Creates the view holder
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ItemRegionListBinding.inflate(layoutInflater, parent, false)
        return ViewHolder(binding)
    }

    //Binds a new item to the view holder
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(onRegionClickedListener, item)
    }

    class ViewHolder(val binding: ItemRegionListBinding) :
        RecyclerView.ViewHolder(binding.root) {

        //This is used to bind the data to the view holder
        fun bind(clickedListener: OnRegionClickedListener, region: DatabaseRegions) {

            val regionImage = getLocationRegionImage(itemView.context, region.name)

            binding.locationRegionImage.setImageDrawable(regionImage)

            binding.locationListId.text.text = "#${region.id}"

            binding.locationListName.text.text = formatPocketdexObjectName(region.name)

            binding.locationRegionImage.setOnClickListener {
                clickedListener.onRegionClicked(
                    region.name
                )
            }

            binding.executePendingBindings()
        }
    }
}

//This util is used to avoid redrawing an item that was not changed
class RegionDiffCallback : DiffUtil.ItemCallback<DatabaseRegions>() {
    override fun areItemsTheSame(
        oldItem: DatabaseRegions,
        newItem: DatabaseRegions
    ): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(
        oldItem: DatabaseRegions,
        newItem: DatabaseRegions
    ): Boolean {
        return oldItem.id == newItem.id
    }

}

interface OnRegionClickedListener {
    fun onRegionClicked(name: String)
}