package com.idz.huthashani.login


import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.idz.huthashani.HomeActivity
import com.idz.huthashani.firebase.FirebaseModel
import com.idz.huthashani.R
import com.idz.huthashani.register.RegisterActivity

class LoginActivity : AppCompatActivity() {

    private var createNewAccount: TextView? = null
    private var inputEmail: EditText? = null
    private var inputPassword: EditText? = null
    private var btnLogin: Button? = null
    private var emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+"
    private var progressDialog: ProgressDialog? = null
    private val progressDialogDelayHandler = Handler()
    private val progressDialogDelayRunnable = Runnable {
        progressDialog?.dismiss()
    }
    private val delayDuration = 5000L
    private val firebaseModel: FirebaseModel = FirebaseModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        supportActionBar?.hide()

        initializeViews()

        btnLogin!!.setOnClickListener { performLogin() }
        createNewAccount!!.setOnClickListener {
            startActivity(
                Intent(
                    this@LoginActivity,
                    RegisterActivity::class.java
                )
            )
        }

    }

    private fun initializeViews() {
        createNewAccount = findViewById(R.id.createNewAccount)
        inputEmail = findViewById(R.id.inputEmail)
        inputPassword = findViewById(R.id.inputPassword)
        btnLogin = findViewById(R.id.btnLogin)
        progressDialog = ProgressDialog(this)
    }

    private fun performLogin() {
        val email: String = inputEmail?.text.toString()
        val password: String = inputPassword?.text.toString()
        if (!email.matches(emailPattern.toRegex())) {
            inputEmail?.error = "Enter correct email"
            inputEmail?.requestFocus()
        } else if (password.isEmpty() || password.length < 6) {
            inputPassword?.error = "Enter proper password"
            inputPassword?.requestFocus()
        } else {
            showProgressDialogWithDelay()
            loginUser(UserLogin(email, password))
        }
    }

    private fun loginUser(userLogin: UserLogin) {
        firebaseModel.loginUser(userLogin) { task ->
            if (task.isSuccessful) {
                sendUserToNextActivity()
                progressDialog?.dismiss()
                Toast.makeText(this@LoginActivity, "Login Successful", Toast.LENGTH_SHORT).show()
            } else {
                progressDialog?.dismiss()
                Toast.makeText(this@LoginActivity, "Invalid username and password", Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }

    private fun sendUserToNextActivity() {
        val intent = Intent(this@LoginActivity, HomeActivity::class.java)
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
    }

    private fun showProgressDialogWithDelay() {
        progressDialog?.setMessage("Please wait while registration...")
        progressDialog?.show()
        progressDialogDelayHandler.postDelayed(progressDialogDelayRunnable, delayDuration)
    }


}