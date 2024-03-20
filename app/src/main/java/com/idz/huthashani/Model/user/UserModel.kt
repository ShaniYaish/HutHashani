package com.idz.huthashani.Model.user


import android.graphics.Bitmap
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.idz.huthashani.Model.firebase.FirebaseModel


class UserModel private constructor() {
    private val firebaseModel: FirebaseModel = FirebaseModel()

    fun interface Listener<T> {
        fun onComplete(data: T)
    }

    companion object {
        private val _instance = UserModel()
        fun instance(): UserModel {
            return _instance
        }
    }


    fun registerUser(user: User?, listener: Listener<Task<AuthResult>>? = null) {
        if (user != null) {
            firebaseModel.registerUser(user) { task: Task<AuthResult> ->
                listener?.onComplete(task)
            }
        }
    }
    fun loginUser(user: User?, listener: Listener<Task<AuthResult>>) {
        if (user != null) {
            firebaseModel.loginUser(
                user
            ) { task: Task<AuthResult> ->
                listener.onComplete(task)
            }
        }
    }



    fun isUserLoggedIn(): Boolean {
        return firebaseModel.isUserLoggedIn()
    }

    fun updateUserProfile(user: User?, bitmap: Bitmap?, listener: Listener<Task<Void?>?>) {
        if (user != null) {
            firebaseModel.updateUserProfile(user, bitmap) { task ->
                listener.onComplete(
                    task
                )
            }
        }
    }

    fun logout() {
        firebaseModel.logout()
    }

}