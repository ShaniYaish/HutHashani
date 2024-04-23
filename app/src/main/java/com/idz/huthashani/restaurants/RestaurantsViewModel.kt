package com.idz.huthashani.restaurants

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.google.firebase.firestore.FirebaseFirestore
import com.idz.huthashani.dao.Post
import com.idz.huthashani.dao.getLocalDatabase
import com.idz.huthashani.utils.RequestStatus
import com.google.firebase.firestore.Query
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class RestaurantsViewModel(application: Application) : AndroidViewModel(application) {

    private val db = FirebaseFirestore.getInstance()
    private val localDatabase = getLocalDatabase(application.applicationContext)

    private val _requestStatus = MutableLiveData<RequestStatus>()
    private val _posts = MutableLiveData<List<Post>>()
    private val _postDeleted = MutableLiveData<RequestStatus>()

    val requestStatus: LiveData<RequestStatus> get() = _requestStatus
    val posts: LiveData<List<Post>> get() = _posts
    val postDeleted : LiveData<RequestStatus> get() = _postDeleted


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

    fun deletePost(postId: String) {
        val documentReference = db.collection("Posts").document(postId)
        documentReference.delete()
            .addOnSuccessListener {
                // Run Room operation in a coroutine
                viewModelScope.launch(Dispatchers.IO) {
                    localDatabase.postDao().delete(postId)
                    // Notify that the post was deleted successfully
                    _postDeleted.postValue(RequestStatus.SUCCESS)
                }
            }
            .addOnFailureListener { e ->
                _postDeleted.value = RequestStatus.FAILURE
            }
    }
}
