package com.idz.huthashani.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import android.widget.ImageView
import android.widget.TextView
import com.idz.huthashani.R

class WishCardAdapter(private val dataList: List<String>) : RecyclerView.Adapter<WishCardAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        // Initialize views from wish_post_card.xml here
        val imageView: ImageView = itemView.findViewById(R.id.postCardImageView)
        val titleTextView: TextView = itemView.findViewById(R.id.postCardTitleTextView)
        //val buttonTextView: TextView = itemView.findViewById(R.id.postCardButtonTextView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        // Inflate the item layout and create ViewHolder
        val view = LayoutInflater.from(parent.context).inflate(R.layout.wish_post_card, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.imageView.setImageResource(R.drawable.no_image) // Example image
        holder.titleTextView.text = "Title ${position + 1}" // Example title
        //holder.buttonTextView.text = "Button" // Example button text
    }

    override fun getItemCount(): Int {
        return dataList.size
    }
}
