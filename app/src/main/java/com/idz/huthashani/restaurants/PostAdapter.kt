package com.idz.huthashani.restaurants

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.NavController
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.idz.huthashani.R
import com.idz.huthashani.dao.Post

class PostAdapter(private var posts: List<Post>? , private val navController: NavController): RecyclerView.Adapter<PostViewHolder>() {

    private val storage = FirebaseStorage.getInstance()
    private val db = FirebaseFirestore.getInstance()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.res_post_card, parent, false)
        return PostViewHolder(view)
    }

    override fun getItemCount(): Int {
        Log.i("COUNT", (posts?.size ?: 0).toString())
        return posts?.size ?: 0
    }

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        val post = posts!![position]
        storage.reference.child(post.picture).downloadUrl.addOnSuccessListener {
            Glide.with(holder.itemView)
                .load(it)
                .into(holder.image!!)
        }

        holder.postCardLocation!!.text = post.locationRest
        holder.postCardTitle!!.text = post.fullNameRest

        holder.postCardButton!!.setOnClickListener {
            // Create a Bundle to hold the data
            val bundle = Bundle().apply {
                putString("restaurantId" , post.id)
                putString("restaurantName", post.fullNameRest)
                putString("restaurantLocation", post.locationRest)
                putString("restaurantImage", post.picture)
                putString("restaurantUserEmail", post.userEmail)
                putString("restaurantRec", post.description)
            }

            // Navigate to ResPageFragment with the bundle
            try {
                navController.navigate(
                    R.id.action_restaurantsFragment_to_resPageFragment,
                    bundle
                )
            }catch (e: IllegalArgumentException){
                navController.navigate(
                    R.id.action_userProfileFragment_to_resPageFragment,
                    bundle
                )
            }
        }

    }
}