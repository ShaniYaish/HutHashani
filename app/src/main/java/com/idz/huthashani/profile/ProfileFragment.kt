package com.idz.huthashani.profile

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.storage.FirebaseStorage
import com.idz.huthashani.R
import com.bumptech.glide.Glide
import com.google.firebase.firestore.FirebaseFirestore
import com.idz.huthashani.dao.Post
import com.idz.huthashani.restaurants.PostAdapter
import com.idz.huthashani.restaurants.RestaurantsViewModel
import com.idz.huthashani.utils.RequestStatus


class ProfileFragment : Fragment() {
    private val restaurantsViewModel: RestaurantsViewModel by viewModels()
    private lateinit var viewModel: ProfileViewModel

    private lateinit var fireBaseAuth: FirebaseAuth
    private lateinit var currentUser: FirebaseUser
    private lateinit var db: FirebaseFirestore

    private var selectedImageUri: Uri? = null
    private lateinit var galleryButton: ImageView
    private lateinit var updateBtn: LinearLayout
    private lateinit var fullName: EditText
    private lateinit var profileImage: ImageView
    private lateinit var progressBar: ProgressBar
    private lateinit var recyclerView: RecyclerView



    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view: View? = inflater.inflate(R.layout.fragment_profile, container, false)
        if (view != null) {
            initializeViews(view)
        }

        // Load user profile image if available
        loadProfileImage()
        initUserName()
        setupRecyclerView()
        setupPosts()
        observePostViewModel()
        observeRequestStatus()


        return view
    }

    private fun initializeViews(view: View) {
        galleryButton = view.findViewById(R.id.edit_button)
        updateBtn = view.findViewById(R.id.update_button)
        profileImage = view.findViewById(R.id.profile_image)
        fullName = view.findViewById(R.id.text)
        progressBar = view.findViewById(R.id.progress_bar)
        recyclerView = view.findViewById(R.id.posts_recycler_view)

        db = FirebaseFirestore.getInstance()
        fireBaseAuth = FirebaseAuth.getInstance()
        currentUser = fireBaseAuth.currentUser!!

        // Initialize ViewModel
        viewModel = ViewModelProvider(this).get(ProfileViewModel::class.java)
    }

    private fun initUserName() {
        val userId = currentUser.uid
        val usersRef = db.collection("Users")

        userId.let { uid ->
            usersRef.document(uid).get()
                .addOnSuccessListener { document ->
                    if (document != null) {
                        // Retrieve the full name of the user from the Firestore document
                        val name = document.getString("fullName")
                        // Set the text of the EditText
                        fullName.setText(" $name שלום , ")
                    }
                }
                .addOnFailureListener { exception ->
                    Log.e("TAG", "Error getting user document", exception)
                }
        }
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
            // Show progress bar
            progressBar.visibility = View.VISIBLE
            // Call ViewModel function to update profile
            //viewModel.changeUserName(userProfile, name)
            viewModel.uploadProfileImage(userProfile, selectedImageUri)
        }
    }


    private fun loadProfileImage() {
        val storageRef = FirebaseStorage.getInstance().reference
        val profileImageRef = storageRef.child("profile_images/${currentUser.email}")

        profileImageRef.downloadUrl.addOnSuccessListener { uri ->
            // Load image using Glide or any other image loading library
            Glide.with(requireContext())
                .load(uri)
                .placeholder(R.drawable.default_avatar) // Placeholder in case image loading fails
                .error(R.drawable.default_avatar) // Placeholder in case of error
                .into(profileImage)
        }.addOnFailureListener {
            // Handle failure to load image
            profileImage.setImageResource(R.drawable.default_avatar)
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

    private fun setupRecyclerView() {
        recyclerView.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        restaurantsViewModel.getPosts("", "")
    }


    private fun observePostViewModel() {
        restaurantsViewModel.posts.observe(viewLifecycleOwner) { posts: List<Post> ->
            // Filter posts to include only those created by the current user
            val currentUserPosts = posts.filter { it.userEmail == currentUser.email.toString() }
            Log.i("NUMBER", currentUserPosts.size.toString())

            // Create and set up the adapter with the filtered list of posts
            val postAdapter = PostAdapter(currentUserPosts)
            recyclerView.adapter = postAdapter
        }
    }

    private fun setupPosts(){
            restaurantsViewModel.getPosts("", "")
    }

    private fun observeRequestStatus() {
        restaurantsViewModel.requestStatus.observe(viewLifecycleOwner) { result ->
            if (result == RequestStatus.FAILURE) {
                Log.i("tag","Something went wrong while processing your request. " +
                        "Please try again later.")
            }
        }
    }
}

