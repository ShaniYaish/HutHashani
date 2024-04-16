package com.idz.huthashani.restaurants

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.storage.FirebaseStorage
import com.idz.huthashani.R
import com.idz.huthashani.dao.Post
import java.util.Locale

class PostAdapter(private var posts: List<Post>?): RecyclerView.Adapter<PostViewHolder>() {

    private val storage = FirebaseStorage.getInstance()
    private val userEmail = FirebaseAuth.getInstance().currentUser?.email as String
    //private var listener: OnPostClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.post_card, parent, false)
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
    }

}