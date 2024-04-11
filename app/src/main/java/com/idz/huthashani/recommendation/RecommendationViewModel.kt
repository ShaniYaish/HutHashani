package com.idz.huthashani.recommendation

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.idz.huthashani.dao.Post
import com.idz.huthashani.utils.RequestStatus


class RecommendationViewModel: ViewModel() {

    private val _requestStatus = MutableLiveData<RequestStatus>()
    val requestStatus: LiveData<RequestStatus> get() = _requestStatus

    private val db = FirebaseFirestore.getInstance()
    private val storage = FirebaseStorage.getInstance()

    fun createNewPost(post: Post, attachedPictureUri: Uri) {
        val currentUser = FirebaseAuth.getInstance().currentUser
        val imageRefLocation = "post_images/${currentUser?.uid}/${attachedPictureUri.lastPathSegment}"
        val imageRef: StorageReference = storage.getReference(imageRefLocation)

        imageRef.putFile(attachedPictureUri)
            .addOnSuccessListener {
                if (currentUser != null) {
                    currentUser.email?.let {
                        post.userEmail = it
                        post.picture = imageRef.path
                        savePost(post)
                    }
                } else {
                    _requestStatus.value = RequestStatus.FAILURE
                }
            }
            .addOnFailureListener {
                _requestStatus.value = RequestStatus.FAILURE
            }

        _requestStatus.value = RequestStatus.IN_PROGRESS
    }

    private fun savePost(post: Post) {
        db.collection("Posts")
            .add(post)
            .addOnSuccessListener { documentReference ->
                val postId = documentReference.id

                db.collection("Posts")
                    .document(postId)
                    .update("id", postId)
                    .addOnSuccessListener {
                        _requestStatus.value = RequestStatus.SUCCESS
                    }
                    .addOnFailureListener {
                        _requestStatus.value = RequestStatus.FAILURE
                    }
            }
            .addOnFailureListener {
                _requestStatus.value = RequestStatus.FAILURE
            }
    }

    fun clear() {
        _requestStatus.value = RequestStatus.IDLE
    }

}