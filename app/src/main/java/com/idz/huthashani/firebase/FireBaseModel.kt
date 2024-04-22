package com.idz.huthashani.firebase


import android.util.Log
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreSettings
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.AuthResult
import com.idz.huthashani.login.UserLogin
import com.idz.huthashani.register.UserRegister

class FirebaseModel {
    private val _registerResult = MutableLiveData<String>()
    val registerResult: LiveData<String> get() = _registerResult


    private var db: FirebaseFirestore = FirebaseFirestore.getInstance()
    private var storage: FirebaseStorage
    private var firebaseAuth: FirebaseAuth
    private var firebaseUser: FirebaseUser?

    init {
        val settings: FirebaseFirestoreSettings = FirebaseFirestoreSettings.Builder()
            .setPersistenceEnabled(true)
            .build()
        db.firestoreSettings = settings
        storage = FirebaseStorage.getInstance()
        firebaseAuth = FirebaseAuth.getInstance()
        firebaseUser = firebaseAuth.currentUser
    }

    fun registerUser(userLogin: UserLogin, userRegister: UserRegister, listener: (Task<AuthResult?>) -> Unit) {
        Log.i("Tag2" , userRegister.fullName.toString())

        firebaseAuth.createUserWithEmailAndPassword(userLogin.email.toString(), userLogin.password.toString())
            .addOnCompleteListener { registrationTask ->
                if (registrationTask.isSuccessful) {
                    val userId = firebaseAuth.currentUser!!.uid
                    val user = returnUserAsMap(userId,userRegister)

                    db.collection("Users").document(userLogin.email.toString())
                        .set(user)
                        .addOnSuccessListener {
                            _registerResult.value = "Success"
                        }
                        .addOnFailureListener {
                            _registerResult.value = "Cannot create user"
                        }
                    listener(registrationTask)
                } else {
                    // Notify listener of failure if user registration failed
                    _registerResult.value = "The email is already in use"
                    listener(registrationTask)
                }
            }
    }


    private fun returnUserAsMap(userId:String , userRegister: UserRegister): Map<String, Any> {
        val user = HashMap<String, Any>()
        user["fullName"] = userRegister.fullName ?: ""
        user["userId"] = userId
        Log.i("Tag" , user["fullName"].toString())
        return user
    }




    fun loginUser(userLogin: UserLogin, listener: (Task<AuthResult?>) -> Unit) {
        firebaseAuth.signInWithEmailAndPassword(userLogin.email.toString(), userLogin.password.toString())
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