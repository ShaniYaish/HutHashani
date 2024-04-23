package com.idz.huthashani.respage

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.idz.huthashani.NavActivity
import com.idz.huthashani.R
import com.idz.huthashani.profile.ProfileViewModel
import com.idz.huthashani.restaurants.RestaurantsViewModel
import com.idz.huthashani.utils.RequestStatus

class ResPageFragment : Fragment() {

    private val restaurantsViewModel : RestaurantsViewModel by viewModels()


    private lateinit var resNameTextView : TextView
    private lateinit var resLocationTextView : TextView
    private lateinit var resAutherTextView : TextView
    private lateinit var resRecTextView : TextView
    private lateinit var resPhotoImageView : ImageView
    private lateinit var deleteButton : Button
    private lateinit var editButton : Button

    private lateinit var db: FirebaseFirestore
    private lateinit var fireBaseAuth: FirebaseAuth
    private val storage = FirebaseStorage.getInstance()
    private val currentUser = FirebaseAuth.getInstance().currentUser



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_res_page, container, false)


        initView(view)
        initArguments()

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        observesDeletePost()

        deleteButton.setOnClickListener {
            val postId = arguments?.getString("restaurantId")
            postId?.let {
                restaurantsViewModel.deletePost(it) // Using the postId safely
            }
        }

        editButton.setOnClickListener {
            val postId = arguments?.getString("restaurantId")
            val restaurantImage = arguments?.getString("restaurantImage")
            val navController = (requireActivity() as NavActivity).navController!!

            val bundle = Bundle().apply {
                putString("restaurantId", postId)
                putString("restaurantName", resNameTextView.text.toString())
                putString("restaurantLocation", resLocationTextView.text.toString())
                putString("restaurantImage", restaurantImage)
                putString("restaurantRec", resRecTextView.text.toString())
            }

            // Navigate to ResPageFragment with the bundle
            navController.navigate(
                R.id.action_resPageFragment_to_editPostFragment,
                bundle
            )
        }
    }

    private fun initView(view: View){
        resNameTextView = view.findViewById(R.id.res_name)
        resLocationTextView = view.findViewById(R.id.res_location)
        resPhotoImageView = view.findViewById(R.id.res_photo)
        resAutherTextView = view.findViewById(R.id.res_author_name)
        resRecTextView = view.findViewById(R.id.res_rec_text)
        deleteButton = view.findViewById(R.id.delete_post_button)
        editButton = view.findViewById(R.id.edit_post_button)

        db = FirebaseFirestore.getInstance()
        fireBaseAuth = FirebaseAuth.getInstance()

    }

    private fun initArguments() {
        resNameTextView.text = arguments?.getString("restaurantName")
        resLocationTextView.text = arguments?.getString("restaurantLocation")
        resRecTextView.text = arguments?.getString("restaurantRec")

        val userEmail = arguments?.getString("restaurantUserEmail")
        val usersRef = db.collection("Users")
        val currentUserEmail = fireBaseAuth.currentUser?.email

        userEmail?.let { email ->
            usersRef.document(email.toString()).get()
                .addOnSuccessListener { document ->
                    if (document != null) {
                        val currentUserName = document.getString("fullName")
                        resAutherTextView.text = currentUserName

                        // Show edit and delete buttons only if the current user is the author
                        if (email == currentUserEmail) {
                            deleteButton.visibility = requireView().visibility
                            editButton.visibility = requireView().visibility
                        }
                    }
                }
        }


        val restaurantImage = arguments?.getString("restaurantImage")
        storage.reference.child(restaurantImage.toString()).downloadUrl.addOnSuccessListener {
            Glide.with(this)
                .load(it)
                .into(resPhotoImageView)
        }
    }

    private fun observesDeletePost(){
        restaurantsViewModel.postDeleted.observe(viewLifecycleOwner){ result ->
            when (result) {
                RequestStatus.SUCCESS -> {
                    val navController = (requireActivity() as NavActivity).navController
                    navController!!.navigate(
                        R.id.action_resPageFragment_to_restaurantsFragment
                    )
                    Toast.makeText(requireContext(), "הפוסט נמחק בהצלחה", Toast.LENGTH_SHORT).show()
                }
                RequestStatus.FAILURE -> {
                    // Handle failure case if profile image upload fails (optional)
                    Toast.makeText(requireContext(), "מצטערים , לא הצלחנו למחוק את הפוסט", Toast.LENGTH_SHORT).show()
                }
                else -> {
                    // Handle other cases if needed (optional)
                }
            }
        }
    }

    private fun observesEditPost(){

    }
}
