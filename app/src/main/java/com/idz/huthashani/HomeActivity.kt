package com.idz.huthashani


import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.idz.huthashani.firebase.FirebaseModel
import com.idz.huthashani.login.LoginActivity

class HomeActivity : AppCompatActivity() {

    private var btnLogout: Button? = null
    private val firebaseModel: FirebaseModel = FirebaseModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        btnLogout = findViewById(R.id.btnLogout)

        this.btnLogout!!.setOnClickListener {
            firebaseModel.logout()
            val intent = Intent(this@HomeActivity, LoginActivity::class.java)
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
        }

    }
}