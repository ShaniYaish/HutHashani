package com.idz.huthashani.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import android.widget.ImageView
import android.widget.TextView
import com.idz.huthashani.R

class WishCardAdapter(
    private val dataList: List<String>,
    private val imageList: List<Int>, // List of image resources
    private val onCardClick: (position: Int) -> Unit // Click handler
) : RecyclerView.Adapter<WishCardAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageView: ImageView = itemView.findViewById(R.id.postCardImageView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.wish_post_card, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        // Set the image for the current position
        holder.imageView.setImageResource(imageList[position])

        // Set click listener
        holder.itemView.setOnClickListener {
            onCardClick(position)
        }
    }

    override fun getItemCount(): Int = dataList.size
}

