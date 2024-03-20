
package com.idz.huthashani

import android.app.ProgressDialog
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import com.idz.huthashani.Model.user.User
import com.idz.huthashani.Model.user.UserModel
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts


class RegisterActivity : AppCompatActivity() {
    var inputEmail: EditText? = null
    var inputFullName: EditText? = null
    var inputPassword: EditText? = null
    var inputConfirmPassword: EditText? = null
    var btnRegister: Button? = null
    var alreadyHaveAccount: TextView? = null
    var btnSelectImage : Button? = null
    var imageAvatar : ImageView? = null
    var emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+"
    var progressDialog: ProgressDialog? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        alreadyHaveAccount = findViewById(R.id.alreadyHaveAccount)
        inputEmail = findViewById(R.id.inputEmail)
        inputFullName = findViewById(R.id.inputFullName)
        inputPassword = findViewById(R.id.inputPassword)
        inputConfirmPassword = findViewById(R.id.inputConfirmPassword)
        btnRegister = findViewById(R.id.btnRegister)
        btnSelectImage = findViewById(R.id.btnSelectImage)
        imageAvatar = findViewById(R.id.imageAvatar)

        val pickImage = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
            if (uri != null) {
                imageAvatar?.setImageURI(uri)
            } else {
                // Handle case when user cancels image selection
                Toast.makeText(this, "Image selection cancelled", Toast.LENGTH_SHORT).show()
            }
        }

        btnSelectImage?.setOnClickListener {
            pickImage.launch("image/*")
        }
        btnRegister!!.setOnClickListener { performAuth() }
    }

    private fun performAuth() {
        val email: String = inputEmail?.text.toString()
        val fullName = inputFullName!!.text.toString()
        val password: String = inputPassword?.text.toString()
        val confirmPassword: String = inputConfirmPassword?.text.toString()
        if (!email.matches(emailPattern.toRegex())) {
            inputEmail?.error = "Enter correct email"
            inputEmail?.requestFocus()
        } else if (password.isEmpty() || password.length < 6) {
            inputPassword?.error = "Enter proper password"
            inputPassword?.requestFocus()
        } else if (fullName.isEmpty()) {
            inputFullName!!.error = "Enter full name"
            inputFullName!!.requestFocus()
        } else if (password != confirmPassword) {
            inputConfirmPassword?.error = "Password not match both fields"
            inputConfirmPassword?.requestFocus()
        } else {
            progressDialog?.setMessage("Please wait while registration...")
            progressDialog?.setTitle("Registration")
            progressDialog?.setCanceledOnTouchOutside(false)
            progressDialog?.show()
            registerUser(User(email ,password,fullName))
        }
    }

    private fun registerUser(user: User) {
        UserModel.instance().registerUser(user) { task ->
            if (task.isSuccessful()) {
                progressDialog?.dismiss()
                sendUserToNextActivity()
                Toast.makeText(this@RegisterActivity, "Registration Successful", Toast.LENGTH_SHORT).show()
            } else {
                progressDialog?.dismiss()
                val errorMessage = task.exception?.message ?: "Unknown error occurred"
                Toast.makeText(this@RegisterActivity, errorMessage, Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun sendUserToNextActivity() {
        val intent: Intent = Intent(this@RegisterActivity, MainActivity::class.java)
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
    }
}