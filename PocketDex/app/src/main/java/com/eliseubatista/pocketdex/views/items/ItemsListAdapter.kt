package com.eliseubatista.pocketdex.views.items

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.eliseubatista.pocketdex.database.items.DatabaseItems
import com.eliseubatista.pocketdex.databinding.ItemItemsListBinding
import com.eliseubatista.pocketdex.utils.*

class ItemsListAdapter :
    ListAdapter<DatabaseItems, ItemsListAdapter.ViewHolder>(ItemDiffCallback()) {

    lateinit var onItemClickedListener: OnItemClickedListener

    //Creates the view holder
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ItemItemsListBinding.inflate(layoutInflater, parent, false)
        return ViewHolder(binding)
    }

    //Binds a new item to the view holder
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(onItemClickedListener, item)
    }

    class ViewHolder(val binding: ItemItemsListBinding) :
        RecyclerView.ViewHolder(binding.root) {

        //This is used to bind the data to the view holder
        fun bind(clickedListener: OnItemClickedListener, item: DatabaseItems) {

            //val imageScale = getImageScaleByEvolutionChain(item.name, item.evolutionChain)
            val imageScale = 1.0f

            binding.itemListImage.scaleX = imageScale
            binding.itemListImage.scaleY = imageScale

            loadImageWithGlide(item.spriteUrl, binding.itemListImage)

            val itemColor = getItemBackgroundColor(itemView.context, item.id)
            val itemDarkerColor = manipulateColor(itemColor, 0.6f)

            binding.itemListItemBackground.background.setTint(itemColor)

            binding.itemListId.text = "#${item.id}"
            binding.itemListId.setTextColor(
                getTextColorByBackgroundColor(
                    itemView.context,
                    itemColor
                )
            )

            binding.itemListName.text = formatPocketdexObjectName(item.name)
            binding.itemListName.setTextColor(
                getTextColorByBackgroundColor(
                    itemView.context,
                    itemColor
                )
            )


            binding.itemCategory.background.setColorFilter(itemColor)
            binding.itemCategory.background.alpha = 1.0f

            binding.itemCategory.text.text = item.category

            binding.itemListItemBackground.setOnClickListener {
                clickedListener.onItemClicked(
                    item.name
                )
            }

            binding.executePendingBindings()
        }
    }
}

//This util is used to avoid redrawing an item that was not changed
class ItemDiffCallback : DiffUtil.ItemCallback<DatabaseItems>() {
    override fun areItemsTheSame(
        oldItem: DatabaseItems,
        newItem: DatabaseItems
    ): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(
        oldItem: DatabaseItems,
        newItem: DatabaseItems
    ): Boolean {
        return oldItem.id == newItem.id
    }

}

interface OnItemClickedListener {
    fun onItemClicked(name: String)
}