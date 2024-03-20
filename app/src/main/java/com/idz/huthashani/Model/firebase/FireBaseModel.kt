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
    private var mAuth: FirebaseAuth
    private var mUser: FirebaseUser?

    init {
        db = FirebaseFirestore.getInstance()
        val settings: FirebaseFirestoreSettings = FirebaseFirestoreSettings.Builder()
            .setPersistenceEnabled(true)
            .build()
        db.firestoreSettings = settings
        storage = FirebaseStorage.getInstance()
        mAuth = FirebaseAuth.getInstance()
        mUser = mAuth.currentUser
    }

    fun registerUser(user: User?, listener: UserModel.Listener<Task<AuthResult>>? = null) {
        if (user != null) {
            mAuth.createUserWithEmailAndPassword(user.email!!, user.password!!)
                .addOnCompleteListener { task ->
                    listener?.onComplete(task)
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
        mUser!!.updateProfile(profileUpdates)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    listener.onComplete(task)
                    Log.d("TAG", "User profile updated.")
                }
            }
    }

    private fun getImageUri(inContext: Context, inImage: Bitmap?, fileName: String?): Uri? {
        if (inImage == null) return null
        val bytes = ByteArrayOutputStream()
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
        val path =
            MediaStore.Images.Media.insertImage(inContext.contentResolver, inImage, fileName, null)
        return Uri.parse(path)
    }

    fun loginUser(user: User, listener: UserModel.Listener<Task<AuthResult>>) {
        mAuth.signInWithEmailAndPassword(user.email.toString(), user.password.toString())
            .addOnCompleteListener { task ->
                mUser = mAuth.currentUser
                listener.onComplete(task)
            }
    }

    fun isUserLoggedIn(): Boolean {
        return mAuth.currentUser != null
    }



    fun logout() {
        mAuth.signOut()
        mUser = null
    }

    fun uploadImage(name: String, bitmap: Bitmap, listener: (Any) -> Unit) {
        val storageRef = storage.reference
        val imagesRef = storageRef.child("images/$name.jpg")
        val baos = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
        val data = baos.toByteArray()
        val uploadTask = imagesRef.putBytes(data)
        uploadTask.addOnFailureListener { listener.invoke(true) }.addOnSuccessListener {
            imagesRef.downloadUrl.addOnSuccessListener { uri ->
                listener.invoke(
                    uri.toString()
                )
            }
        }
    }

}