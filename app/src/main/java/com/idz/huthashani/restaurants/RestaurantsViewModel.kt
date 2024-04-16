package com.idz.huthashani.restaurants

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.firestore.FirebaseFirestore
import com.idz.huthashani.dao.Post
import com.idz.huthashani.dao.getLocalDatabase
import com.idz.huthashani.utils.RequestStatus
import com.google.firebase.firestore.Query


class RestaurantsViewModel(application: Application) : AndroidViewModel(application) {

    private val db = FirebaseFirestore.getInstance()
    private val _requestStatus = MutableLiveData<RequestStatus>()
    private val _posts = MutableLiveData<List<Post>>()
    val requestStatus: LiveData<RequestStatus> get() = _requestStatus
    val posts: LiveData<List<Post>> get() = _posts

    init {
        // Initialize the request status
        _requestStatus.value = RequestStatus.IDLE
    }

    fun getPosts(mode: String, inputFromUser: String) {
        // Clear previous posts
        _posts.value = emptyList()

        // Prepare the Firestore query
        var query: Query = db.collection("Posts")

        // Update the query based on mode and inputFromUser if necessary
        if (mode.isNotEmpty() && inputFromUser.isNotEmpty()) {
            query = query.whereEqualTo(mode, inputFromUser)
        }

        query.get()
            .addOnSuccessListener { documents ->
                val postList = documents.toObjects(Post::class.java)
                _posts.value = postList

                // Update request status
                if (postList.isEmpty()) {
                    _requestStatus.value = RequestStatus.SUCCESS
                } else {
                    _requestStatus.value = RequestStatus.IDLE
                }
            }
            .addOnFailureListener { e ->
                _requestStatus.value = RequestStatus.FAILURE
            }
    }
}
