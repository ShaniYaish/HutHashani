package com.idz.huthashani.profile

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference

class ProfileViewModel : ViewModel() {
    private val _uploadProfileImageResult = MutableLiveData<Boolean>()
    private val _changeNameResult = MutableLiveData<Boolean>()
    val uploadProfileImageResult: LiveData<Boolean> get() = _uploadProfileImageResult
    val changeNameResult: LiveData<Boolean> get() = _changeNameResult

    private val storage = FirebaseStorage.getInstance()
    private val db = FirebaseFirestore.getInstance()
    private val currentUser = FirebaseAuth.getInstance().currentUser

    fun uploadProfileImage(userProfile: UserProfile, imageUri: Uri?) {
        if (imageUri == null) {
            _uploadProfileImageResult.value = false
            return
        }

        val imageRef: StorageReference = storage.reference.child("profile_images/${userProfile.email}")

        imageRef.putFile(imageUri)
            .addOnSuccessListener {
                _uploadProfileImageResult.setValue(true)
            }
            .addOnFailureListener {
                _uploadProfileImageResult.setValue(false)
            }
    }

    fun changeUserName(userProfile: UserProfile, fullName: String) {
        if (fullName.isEmpty()) {
            _changeNameResult.value = false
            return
        }

        val names = fullName.split(" ")
        val updatedUserProfile = hashMapOf<String, Any>(
            "fullName" to fullName
        )

        db.collection("Users").document(currentUser!!.uid)
            .update(updatedUserProfile)
            .addOnSuccessListener {
                _changeNameResult.value = true
            }
            .addOnFailureListener {
                _changeNameResult.value = false
            }
    }
}
