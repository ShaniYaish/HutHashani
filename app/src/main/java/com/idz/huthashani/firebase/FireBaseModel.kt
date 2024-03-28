package com.idz.huthashani.firebase


import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreSettings
import com.idz.huthashani.user.User
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.AuthResult

class FirebaseModel {
    private val _registerResult = MutableLiveData<String>()
    val registerResult: LiveData<String> get() = _registerResult
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

    fun registerUser(user: User, listener: (Task<AuthResult?>) -> Unit) {
        firebaseAuth.createUserWithEmailAndPassword(user.email.toString(), user.password.toString())
            .addOnCompleteListener { registrationTask ->
                if (registrationTask.isSuccessful) {
                    val userId = registrationTask.result?.user?.uid
                    if (userId != null) {
                        db.collection("Users").document(user.email.toString())
                            .set(user)
                            .addOnSuccessListener {
                                _registerResult.value = "Success"
                            }
                            .addOnFailureListener {
                                _registerResult.value = "Cannot upload user"
                            }
                    }
                    listener(registrationTask)
                } else {
                    // Notify listener of failure if user registration failed
                    listener(registrationTask)
                }
            }
    }



    fun loginUser(user: User, listener: (Task<AuthResult?>) -> Unit) {
        firebaseAuth.signInWithEmailAndPassword(user.email.toString(), user.password.toString())
            .addOnCompleteListener { task ->
                firebaseUser = firebaseAuth.currentUser
                listener(task)
            }
    }


    fun logout() {
        firebaseAuth.signOut()
        firebaseUser = null
    }

}