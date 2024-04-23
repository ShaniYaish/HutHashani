package com.idz.huthashani.respage.edit

import android.net.Uri
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.idz.huthashani.profile.UserProfile
import com.idz.huthashani.utils.RequestStatus

class EditPostViewModel : ViewModel() {

    private val _changeResult = MutableLiveData<RequestStatus>()
    val changeResult: LiveData<RequestStatus> get() = _changeResult

    private val storage = FirebaseStorage.getInstance()
    private val db = FirebaseFirestore.getInstance()
    private val currentUser = FirebaseAuth.getInstance().currentUser

    fun editPost(postId : String , resName : String , resLocation : String , resDes : String , resImg : Uri?) {
        Log.i("EditPostFragment", "Editing post with ID: $postId")

        val updates = mutableMapOf<String, Any>(
            "fullNameRest" to resName,
            "locationRest" to resLocation,
            "description" to resDes
        )

        db.collection("Posts").document(postId)
            .update(updates)
            .addOnSuccessListener {
                _changeResult.value = RequestStatus.SUCCESS
            }
            .addOnFailureListener {
                _changeResult.value = RequestStatus.FAILURE
            }
    }

    fun clearChanges() {
        _changeResult.value = RequestStatus.IDLE
    }

}