package com.karan.quotify.adapter

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.SystemClock
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.ContentInfoCompat.Flags
import androidx.recyclerview.widget.RecyclerView
import com.karan.quotify.databinding.FavItemBinding
import com.karan.quotify.model.Quote
import com.karan.quotify.ui.DisplayFav
import kotlin.random.Random

class FavAdapter(val context: Context) : RecyclerView.Adapter<FavAdapter.Holder>() {

    private val allFav = ArrayList<Quote>()

    private val displayFav = ArrayList<Quote>()

    inner class Holder(val binding: FavItemBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val binder : FavItemBinding = FavItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return Holder(binder)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        val temp = displayFav[position]
        holder.binding.apply {
            words.text = temp.text
            speaker.text = temp.author ?: "Unknown"

            card.setOnClickListener {
                Intent(context, DisplayFav::class.java).also {
                    it.putExtra("quote", temp)
                    context.startActivity(it)
                }
            }
        }
    }

    override fun getItemCount(): Int = displayFav.size


    fun filterList(search : String) {
        displayFav.clear()
        val searchL = search.lowercase()
        for (item in allFav) {
            if(item.text.lowercase().contains(searchL) || (item.author?.lowercase()?.contains(searchL) == true)) {
                displayFav.add(item)
            }
        }
        notifyDataSetChanged()
    }

    fun updateList(newFav : List<Quote>) {
        displayFav.clear()
        allFav.clear()

        displayFav.addAll(newFav)
        allFav.addAll(newFav)

        notifyDataSetChanged()
    }
}