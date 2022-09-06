package com.eliseubatista.pocketdex.views.pokemons

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.eliseubatista.pocketdex.database.pokemons.DatabasePokemon
import com.eliseubatista.pocketdex.databinding.ItemPokemonListBinding
import com.eliseubatista.pocketdex.utils.*

class PokemonAdapter :
    ListAdapter<DatabasePokemon, PokemonAdapter.ViewHolder>(PokemonDiffCallback()) {

    lateinit var onPokemonClickedListener: OnPokemonClickedListener

    //Creates the view holder
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ItemPokemonListBinding.inflate(layoutInflater, parent, false)
        return ViewHolder(binding)
    }

    //Binds a new item to the view holder
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(onPokemonClickedListener, item)
    }

    class ViewHolder(val binding: ItemPokemonListBinding) :
        RecyclerView.ViewHolder(binding.root) {

        //This is used to bind the data to the view holder
        fun bind(clickedListener: OnPokemonClickedListener, pokemon: DatabasePokemon) {

            val imageScale = getImageScaleByEvolutionChain(pokemon.name, pokemon.evolutionChain)

            binding.pokemonListImage.scaleX = imageScale
            binding.pokemonListImage.scaleY = imageScale

            loadImageWithGlide(pokemon.spriteUrl, binding.pokemonListImage)

            val pokemonColor = getPokemonBackgroundColor(itemView.context, pokemon.color)

            binding.pokemonListItemBackground.background.setTint(pokemonColor)

            binding.pokemonListId.text = "#${pokemon.id}"
            binding.pokemonListId.setTextColor(
                getTextColorByBackgroundColor(
                    itemView.context,
                    pokemonColor
                )
            )

            binding.pokemonListName.text = formatPocketdexObjectName(pokemon.name)
            binding.pokemonListName.setTextColor(
                getTextColorByBackgroundColor(
                    itemView.context,
                    pokemonColor
                )
            )

            if (pokemon.types.isNotEmpty()) {

                val typeOneLogo = getPokemonTypeLogoImage(itemView.context, pokemon.types[0])

                binding.pokemonListFirstType.typeLogo.setImageDrawable(typeOneLogo)

                if (pokemon.types.size > 1) {

                    val typeTwoLogo = getPokemonTypeLogoImage(itemView.context, pokemon.types[1])

                    binding.pokemonListSecondType.typeLogo.setImageDrawable(typeTwoLogo)

                } else {
                    binding.pokemonListSecondType.typeContainer.visibility = View.GONE
                }
            } else {
                binding.pokemonListFirstType.typeContainer.visibility = View.GONE
                binding.pokemonListSecondType.typeContainer.visibility = View.GONE
            }

            binding.pokemonListItemBackground.setOnClickListener {
                clickedListener.onPokemonClicked(
                    pokemon.name
                )
            }

            binding.executePendingBindings()
        }
    }
}

//This util is used to avoid redrawing an item that was not changed
class PokemonDiffCallback : DiffUtil.ItemCallback<DatabasePokemon>() {
    override fun areItemsTheSame(
        oldItem: DatabasePokemon,
        newItem: DatabasePokemon
    ): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(
        oldItem: DatabasePokemon,
        newItem: DatabasePokemon
    ): Boolean {
        return oldItem.id == newItem.id
    }

}

interface OnPokemonClickedListener {
    fun onPokemonClicked(pokemonName: String)
}