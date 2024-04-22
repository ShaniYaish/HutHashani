package com.idz.huthashani.register


import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import android.os.Handler
import android.widget.LinearLayout
import com.idz.huthashani.NavActivity
import com.idz.huthashani.firebase.FirebaseModel
import com.idz.huthashani.login.LoginActivity
import com.idz.huthashani.R
import com.idz.huthashani.login.UserLogin


class RegisterActivity : AppCompatActivity() {

    private val firebaseModel: FirebaseModel = FirebaseModel()

    private var inputEmail: EditText? = null
    private var inputFullName: EditText? = null
    private var inputPassword: EditText? = null
    private var inputConfirmPassword: EditText? = null
    private var btnRegister: LinearLayout? = null
    private var alreadyHaveAccount: TextView? = null
    private var emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+"
    private var progressDialog: ProgressDialog? = null
    private val progressDialogDelayHandler = Handler()
    private val progressDialogDelayRunnable = Runnable {
        progressDialog?.dismiss()
    }
    private val delayDuration = 5000L

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        supportActionBar?.hide()

        // Initialize views
        initializeViews()

        btnRegister!!.setOnClickListener { performAuth() }

        alreadyHaveAccount!!.setOnClickListener {
            startActivity(
                Intent(
                    this@RegisterActivity,
                    LoginActivity::class.java
                )
            )
        }
    }

    private fun initializeViews() {
        alreadyHaveAccount = findViewById(R.id.toLoginActivity)
        inputEmail = findViewById(R.id.inputEmailET)
        inputFullName = findViewById(R.id.inputFullNameET)
        inputPassword = findViewById(R.id.inputPasswordET)
        inputConfirmPassword = findViewById(R.id.inputConfirmPasswordET)
        btnRegister = findViewById(R.id.btnRegisterLL)
        progressDialog = ProgressDialog(this)
    }


    private fun performAuth() {
        val email: String = inputEmail?.text.toString()
        val fullName = inputFullName!!.text.toString()
        val password: String = inputPassword?.text.toString()
        val confirmPassword: String = inputConfirmPassword?.text.toString()

        if (!email.matches(emailPattern.toRegex())) {
            inputEmail?.error = "Enter correct email"
            inputEmail?.requestFocus()
        }else if (fullName.isEmpty()) {
            inputFullName!!.error = "Enter full name"
            inputFullName!!.requestFocus()
        } else if (password.isEmpty() || password.length < 6) {
            inputPassword?.error = "Password need more than 6 digits"
            inputPassword?.requestFocus()
        } else if (password != confirmPassword) {
            inputConfirmPassword?.error = "Password not match"
            inputConfirmPassword?.requestFocus()
        } else {
            showProgressDialogWithDelay()
            registerUser(UserLogin(email,password) , UserRegister(fullName))
        }
    }

    private fun registerUser(userLogin: UserLogin ,userRegister: UserRegister ) {
        firebaseModel.registerUser(userLogin, userRegister) { registrationTask ->
            if (registrationTask.isSuccessful) {
                // Registration successful
                sendUserToNextActivity()
                progressDialog?.dismiss()
                Toast.makeText(this@RegisterActivity, "Registration Successful", Toast.LENGTH_SHORT).show()
            } else {
                // Registration failed
                progressDialog?.dismiss()
                val errorMessage = registrationTask.exception?.message ?: "Unknown error occurred"
                Toast.makeText(this@RegisterActivity, errorMessage, Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun sendUserToNextActivity() {
        val intent = Intent(this@RegisterActivity, NavActivity::class.java)
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
    }

    private fun showProgressDialogWithDelay() {
        progressDialog?.setMessage("Please wait while registration...")
        progressDialog?.show()
        progressDialogDelayHandler.postDelayed(progressDialogDelayRunnable, delayDuration)
    }

}