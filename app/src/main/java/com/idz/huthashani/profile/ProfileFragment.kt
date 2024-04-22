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
import android.widget.Toast
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
import com.idz.huthashani.NavActivity
import com.idz.huthashani.dao.Post
import com.idz.huthashani.restaurants.PostAdapter
import com.idz.huthashani.restaurants.RestaurantsViewModel
import com.idz.huthashani.utils.RequestStatus
import kotlinx.coroutines.*


class ProfileFragment : Fragment() {
    private val restaurantsViewModel: RestaurantsViewModel by viewModels()
    private lateinit var profileViewModel: ProfileViewModel

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

    private var currentUserName : String? = null



    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view: View? = inflater.inflate(R.layout.fragment_profile, container, false)
        if (view != null) {
            initializeViews(view)
        }

        initUserName()
        handleUpdateButton()
        loadProfileImage()

        setupRecyclerView()
        setupPosts()

        observePostViewModel()
        observeRequestStatus()
        observeChangeNameResult()
        observeUploadProfileImageResult()


        return view
    }

    private fun initializeViews(view: View) {
        galleryButton = view.findViewById(R.id.edit_button)
        updateBtn = view.findViewById(R.id.update_button)
        profileImage = view.findViewById(R.id.profile_image)
        fullName = view.findViewById(R.id.nameEditText)
        progressBar = view.findViewById(R.id.progress_bar)
        recyclerView = view.findViewById(R.id.posts_recycler_view)

        db = FirebaseFirestore.getInstance()
        fireBaseAuth = FirebaseAuth.getInstance()
        currentUser = fireBaseAuth.currentUser!!

        // Initialize ViewModel
        profileViewModel = ViewModelProvider(this).get(ProfileViewModel::class.java)
    }

    private fun initUserName() {
        val userEmail = currentUser.email
        val usersRef = db.collection("Users")


        userEmail.let { email ->
            usersRef.document(email.toString()).get()
                .addOnSuccessListener { document ->
                    if (document != null) {
                        // Retrieve the full name of the user from the Firestore document
                        currentUserName = document.getString("fullName")
                        // Set the text of the EditText
                        fullName.setText(currentUserName)
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

    private fun handleUpdateButton(){
        updateBtn.setOnClickListener {
            val name = fullName.text.toString()
            val userProfile = UserProfile(name, currentUser.email ?: "", selectedImageUri.toString())

            // Show progress bar
            progressBar.visibility = View.VISIBLE

            // Start a coroutine to delay the execution
            CoroutineScope(Dispatchers.Main).launch {
                delay(3000) // Delay for 3 seconds

                // Call ViewModel function to update profile
                if(selectedImageUri == null && currentUserName == name){
                    Toast.makeText(requireContext(), "אין מה לעדכן", Toast.LENGTH_SHORT).show()
                }else if (selectedImageUri == null &&  currentUserName != name){
                    profileViewModel.changeUserName(userProfile, name)
                }else if (selectedImageUri != null &&  currentUserName == name){
                    profileViewModel.uploadProfileImage(userProfile, selectedImageUri)
                    selectedImageUri = null
                }else{
                    profileViewModel.changeUserName(userProfile, name)
                    profileViewModel.uploadProfileImage(userProfile, selectedImageUri)
                }

                // Hide progress bar after 3 seconds
                progressBar.visibility = View.GONE
                onDestroyView()
            }
        }
    }


    private fun observePostViewModel() {
        val navController = (requireActivity() as NavActivity).navController!!

        restaurantsViewModel.posts.observe(viewLifecycleOwner) { posts: List<Post> ->
            // Filter posts to include only those created by the current user
            val currentUserPosts = posts.filter { it.userEmail == currentUser.email.toString() }
            // Create and set up the adapter with the filtered list of posts
            val postAdapter = PostAdapter(currentUserPosts,navController)
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


    private fun observeChangeNameResult() {
        profileViewModel.changeNameResult.observe(viewLifecycleOwner) { result ->
            when (result) {
                RequestStatus.SUCCESS -> {
                    // Hide progress bar when profile image upload is successful
                    progressBar.visibility = View.GONE
                    Toast.makeText(requireContext(), "השם התעדכן בהצלחה", Toast.LENGTH_SHORT).show()
                    initUserName()
                }
                RequestStatus.IN_PROGRESS -> {
                    progressBar.visibility = View.VISIBLE
                }
                RequestStatus.FAILURE -> {
                    // Handle failure case if profile image upload fails (optional)
                    progressBar.visibility = View.GONE
                    Toast.makeText(requireContext(), "מצטערים , לא הצלחנו לעדכן את שמך", Toast.LENGTH_SHORT).show()
                }
                else -> {
                    // Handle other cases if needed (optional)
                }
            }
        }
    }

    private fun observeUploadProfileImageResult() {
        profileViewModel.uploadProfileImageResult.observe(viewLifecycleOwner) { result ->
            when (result) {
                RequestStatus.SUCCESS -> {
                    // Hide progress bar when profile image upload is successful
                    progressBar.visibility = View.GONE
                    Toast.makeText(requireContext(), "תמונת הפרופיל התעדכנה בהצלחה", Toast.LENGTH_SHORT).show()
                }
                RequestStatus.IN_PROGRESS -> {
                    progressBar.visibility = View.VISIBLE
                }
                RequestStatus.FAILURE -> {
                    // Handle failure case if profile image upload fails (optional)
                    progressBar.visibility = View.GONE
                    Toast.makeText(requireContext(), "מצטערים , לא הצלחנו לעדכן את תמונת הפרופיל שלך", Toast.LENGTH_SHORT).show()
                }
                else -> {
                    // Handle other cases if needed (optional)
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        // Clear the value of LiveData
        profileViewModel.clearChanges()

    }

}

