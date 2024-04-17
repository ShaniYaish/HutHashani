package com.idz.huthashani.profile

import android.net.Uri
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.idz.huthashani.utils.RequestStatus

class ProfileViewModel : ViewModel() {
    private val _uploadProfileImageResult = MutableLiveData<RequestStatus>()
    private val _changeNameResult = MutableLiveData<RequestStatus>()
    val uploadProfileImageResult: LiveData<RequestStatus> get() = _uploadProfileImageResult
    val changeNameResult: LiveData<RequestStatus> get() = _changeNameResult

    private val storage = FirebaseStorage.getInstance()
    private val db = FirebaseFirestore.getInstance()
    private val currentUser = FirebaseAuth.getInstance().currentUser

    fun uploadProfileImage(userProfile: UserProfile, imageUri: Uri?) {

        val imageRef: StorageReference = storage.reference.child("profile_images/${userProfile.email}")
        _uploadProfileImageResult.value = RequestStatus.IN_PROGRESS

        imageRef.putFile(imageUri!!)
            .addOnSuccessListener {
                _uploadProfileImageResult.value = RequestStatus.SUCCESS
            }
            .addOnFailureListener {
                _uploadProfileImageResult.value = RequestStatus.FAILURE
            }
    }

    fun changeUserName(userProfile: UserProfile, fullName: String) {

        val updatedUserProfile = hashMapOf<String, Any>(
            "fullName" to fullName
        )

        _changeNameResult.value = RequestStatus.IN_PROGRESS

        db.collection("Users").document(currentUser!!.uid)
            .update(updatedUserProfile)
            .addOnSuccessListener {
                _changeNameResult.value = RequestStatus.SUCCESS
            }
            .addOnFailureListener {
                _changeNameResult.value = RequestStatus.FAILURE
            }

    }
}
