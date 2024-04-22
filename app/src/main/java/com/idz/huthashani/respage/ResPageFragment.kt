package com.idz.huthashani.respage

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.idz.huthashani.R

class ResPageFragment : Fragment() {

    private lateinit var resNameTextView : TextView
    private lateinit var resLocationTextView : TextView
    private lateinit var resAutherTextView : TextView
    private lateinit var resRecTextView : TextView
    private lateinit var resPhotoImageView : ImageView

    private lateinit var db: FirebaseFirestore
    private lateinit var fireBaseAuth: FirebaseAuth
    private val storage = FirebaseStorage.getInstance()



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_res_page, container, false)

        initView(view)
        initArguments()

        return view
    }

    private fun initView(view: View){
        resNameTextView = view.findViewById(R.id.res_name)
        resLocationTextView = view.findViewById(R.id.res_location)
        resPhotoImageView = view.findViewById(R.id.res_photo)
        resAutherTextView = view.findViewById(R.id.res_author_name)
        resRecTextView = view.findViewById(R.id.res_rec_text)

        db = FirebaseFirestore.getInstance()
        fireBaseAuth = FirebaseAuth.getInstance()
    }

    private fun initArguments() {
        resNameTextView.text = arguments?.getString("restaurantName")
        resLocationTextView.text = arguments?.getString("restaurantLocation")
        resRecTextView.text = arguments?.getString("restaurantRec")

        val userEmail = arguments?.getString("restaurantUserEmail")
        val usersRef = db.collection("Users")
        userEmail.let { email ->
            usersRef.document(email.toString()).get()
                .addOnSuccessListener { document ->
                    if (document != null) {
                        // Retrieve the full name of the user from the Firestore document
                        val currentUserName = document.getString("fullName")
                        // Set the text of the EditText
                        resAutherTextView.text = currentUserName
                    }
                }
                .addOnFailureListener { exception ->
                    Log.e("TAG", "Error getting user document", exception)
                }
        }


        val restaurantImage = arguments?.getString("restaurantImage")
        //Glide.with(this).load(restaurantImage.toString()).into(resPhotoImageView)
        storage.reference.child(restaurantImage.toString()).downloadUrl.addOnSuccessListener {
            Glide.with(this)
                .load(it)
                .into(resPhotoImageView)
        }
    }
}
