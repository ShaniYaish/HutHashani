package com.idz.huthashani.respage.edit

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.google.firebase.storage.FirebaseStorage
import com.idz.huthashani.R

class EditPostFragment : Fragment() {


    private lateinit var postId: String
    private lateinit var resLocation: TextView
    private lateinit var resName: TextView
    private lateinit var resDescription: TextView
    private lateinit var resPhoto: ImageView
    private lateinit var galleryButton: ImageView
    private lateinit var saveButton: Button

    private val storage = FirebaseStorage.getInstance()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_edit_post, container, false)

        initView(view)
        initArguments()

        return view
    }

    private fun initView(view : View){
        postId = arguments?.getString("postId").toString()
        resPhoto=view.findViewById(R.id.res_photo)
        galleryButton=view.findViewById(R.id.edit_button)
        resDescription=view.findViewById(R.id.res_rec_text)
        resLocation=view.findViewById(R.id.res_location)
        resName=view.findViewById(R.id.res_name)
    }

    private fun initArguments() {
        resName.text = arguments?.getString("restaurantName")
        resLocation.text = arguments?.getString("restaurantLocation")
        resDescription.text = arguments?.getString("restaurantRec")


        val restaurantImage = arguments?.getString("restaurantImage")
        storage.reference.child(restaurantImage.toString()).downloadUrl.addOnSuccessListener {
            Glide.with(this)
                .load(it)
                .into(resPhoto)
        }
    }


}