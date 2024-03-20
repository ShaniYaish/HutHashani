package com.idz.huthashani

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log

import com.google.firebase.auth.FirebaseAuth
import com.idz.huthashani.Model.firebase.FirebaseModel


class MainActivity : AppCompatActivity() {

    private lateinit var mAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mAuth = FirebaseAuth.getInstance()

        supportActionBar?.hide()

        Handler(Looper.getMainLooper()).postDelayed({
            val user = mAuth.currentUser
            Log.i("TAG" , user.toString())
            if( user != null){
                val intent = Intent(this, HomeActivity::class.java)
                startActivity(intent)
                finish()
            }else{
                val intent = Intent(this, RegisterActivity::class.java)
                startActivity(intent)
                finish()
            }
        },3000)

    }
}