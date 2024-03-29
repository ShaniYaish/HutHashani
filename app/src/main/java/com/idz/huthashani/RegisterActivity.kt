
package com.idz.huthashani

import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import com.idz.huthashani.Model.user.User
import com.idz.huthashani.Model.user.UserModel
import android.widget.Toast
import android.os.Handler



class RegisterActivity : AppCompatActivity() {
    private var inputEmail: EditText? = null
    private var inputFullName: EditText? = null
    private var inputPassword: EditText? = null
    private var inputConfirmPassword: EditText? = null
    private var btnRegister: Button? = null
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
    }

    private fun initializeViews() {
        alreadyHaveAccount = findViewById(R.id.alreadyHaveAccount)
        inputEmail = findViewById(R.id.inputEmail)
        inputFullName = findViewById(R.id.inputFullName)
        inputPassword = findViewById(R.id.inputPassword)
        inputConfirmPassword = findViewById(R.id.inputConfirmPassword)
        btnRegister = findViewById(R.id.btnRegister)
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
            registerUser(User(email ,password,fullName))
        }
    }

    private fun registerUser(user: User) {
        UserModel.instance().registerUser(user) { task ->
            if (task!!.isSuccessful) {
                sendUserToNextActivity()
                progressDialog?.dismiss()
                Toast.makeText(this@RegisterActivity, "Registration Successful", Toast.LENGTH_SHORT).show()
            } else {
                progressDialog?.dismiss()
                val errorMessage = task.exception?.message ?: "Unknown error occurred"
                Toast.makeText(this@RegisterActivity, errorMessage, Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun sendUserToNextActivity() {
        val intent = Intent(this@RegisterActivity, HomeActivity::class.java)
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
    }

    private fun showProgressDialogWithDelay() {
        progressDialog?.setMessage("Please wait while registration...")
        progressDialog?.show()
        progressDialogDelayHandler.postDelayed(progressDialogDelayRunnable, delayDuration)
    }

}