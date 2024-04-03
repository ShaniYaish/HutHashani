package com.idz.huthashani.profile

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import com.idz.huthashani.R


class ProfileFragment : Fragment() {
    private lateinit var fireBaseAuth: FirebaseAuth
    private lateinit var currentUser: FirebaseUser
    private lateinit var viewModel: ProfileViewModel

    private var selectedImageUri: Uri? = null
    private lateinit var galleryButton: ImageButton
    private lateinit var updateBtn: Button
    private lateinit var fullNameInputLayout: TextInputLayout
    private lateinit var fullName: TextInputEditText
    private lateinit var profileImage: ImageView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view: View? = inflater.inflate(R.layout.fragment_profile, container, false)
        if (view != null) {
            initializeViews(view)
        }

        return view
    }

    private fun initializeViews(view: View) {
        galleryButton = view.findViewById(R.id.galleryButton)
        updateBtn = view.findViewById(R.id.update_profile_btn)
        profileImage = view.findViewById(R.id.profile_image)
        fullNameInputLayout = view.findViewById(R.id.full_name_profile)
        fullName = fullNameInputLayout.findViewById(R.id.fullNameInput)

        fireBaseAuth = FirebaseAuth.getInstance()
        currentUser = fireBaseAuth.currentUser!!

        // Initialize ViewModel
        viewModel = ViewModelProvider(this).get(ProfileViewModel::class.java)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Set onClickListener for the gallery button
        galleryButton.setOnClickListener {
            openGallery()
        }

        // Set onClickListener for the update button
        updateBtn.setOnClickListener {
            val name = fullName.text.toString()
            val userProfile = UserProfile(name, currentUser.email ?: "", selectedImageUri.toString())
            // Call ViewModel function to update profile
            viewModel.changeUserName(userProfile, name)
            viewModel.uploadProfileImage(userProfile, selectedImageUri)
        }
    }

    private val galleryLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val data: Intent? = result.data
            selectedImageUri = data?.data
            profileImage.setImageURI(selectedImageUri)
        }
    }

    private fun openGallery() {
        val galleryIntent = Intent(Intent.ACTION_PICK)
        galleryIntent.type = "image/*"
        galleryLauncher.launch(galleryIntent)
    }
}

