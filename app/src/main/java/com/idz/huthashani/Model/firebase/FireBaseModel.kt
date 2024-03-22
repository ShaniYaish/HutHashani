package com.idz.huthashani.Model.firebase

import android.graphics.Bitmap
import android.util.Log
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreSettings
import com.idz.huthashani.Model.user.User
import com.idz.huthashani.Model.user.UserModel
import com.idz.huthashani.base.MyApplication
import java.io.ByteArrayOutputStream
import android.content.Context
import android.net.Uri
import android.provider.MediaStore
import com.google.firebase.auth.AuthResult

class FirebaseModel {
    private var db: FirebaseFirestore
    private var storage: FirebaseStorage
    private var firebaseAuth: FirebaseAuth
    private var firebaseUser: FirebaseUser?

    init {
        db = FirebaseFirestore.getInstance()
        val settings: FirebaseFirestoreSettings = FirebaseFirestoreSettings.Builder()
            .setPersistenceEnabled(true)
            .build()
        db.firestoreSettings = settings
        storage = FirebaseStorage.getInstance()
        firebaseAuth = FirebaseAuth.getInstance()
        firebaseUser = firebaseAuth.currentUser
    }

    fun registerUser(user: User, listener: UserModel.Listener<Task<Void?>?>) {
        firebaseAuth.createUserWithEmailAndPassword(user.email.toString(), user.password.toString())
            .addOnCompleteListener {
                firebaseUser = firebaseAuth.currentUser;
                if (firebaseUser != null) {
                    updateUserProfile(user, null, listener);
                } else {
                    listener.onComplete(null);
                }
            }
    }

    fun updateUserProfile(user: User, bitmap: Bitmap?, listener: UserModel.Listener<Task<Void?>?>) {
        val userProfileImageUri =
            MyApplication.getAppContext().let { getImageUri(it, bitmap, user.email) }
        val profileUpdates = UserProfileChangeRequest.Builder()
            .setDisplayName(user.fullName)
            .setPhotoUri(userProfileImageUri)
            .build()
        firebaseUser!!.updateProfile(profileUpdates)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    listener.onComplete(task)
                }
            }
    }

    fun getImageUri(inContext: Context, inImage: Bitmap?, fileName: String?): Uri? {
        if (inImage == null) return null
        val bytes = ByteArrayOutputStream()
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
        val path =
            MediaStore.Images.Media.insertImage(inContext.contentResolver, inImage, fileName, null)
        return Uri.parse(path)
    }


    fun loginUser(user: User, listener: UserModel.Listener<Task<AuthResult>>) {
        firebaseAuth.signInWithEmailAndPassword(user.email.toString(), user.password.toString())
            .addOnCompleteListener { task ->
                firebaseUser = firebaseAuth.currentUser
                listener.onComplete(task)
            }
    }


    fun logout() {
        firebaseAuth.signOut()
        firebaseUser = null
    }

}