package com.idz.huthashani.restaurants

import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.idz.huthashani.R
import com.idz.huthashani.dao.Post

class PostViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    var image: ImageView? = null
    var postCardTitle: TextView? = null
    var postCardLocation: TextView? = null
    var postCardButton: LinearLayout? = null

    init {
        postCardTitle= itemView.findViewById(R.id.postCardTitleTextView)
        postCardLocation= itemView.findViewById(R.id.postCardLocationTextView)
        image = itemView.findViewById(R.id.postCardImageView)
        postCardButton = itemView.findViewById(R.id.postCardButton)

    }
}