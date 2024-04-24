package com.idz.huthashani.respage.edit

import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import com.bumptech.glide.Glide
import com.google.firebase.storage.FirebaseStorage
import com.idz.huthashani.NavActivity
import com.idz.huthashani.R
import com.idz.huthashani.utils.RequestStatus
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class EditPostFragment : Fragment() {

    private lateinit var editViewModel : EditPostViewModel

    private lateinit var postId: String
    private var selectedImageUri: Uri? = null
    private lateinit var resLocation: EditText
    private lateinit var resName: EditText
    private lateinit var resDescription: EditText
    private lateinit var resPhoto: ImageView
    private lateinit var galleryButton: ImageView
    private lateinit var saveButton: LinearLayout
    private lateinit var progressBar : ProgressBar

    private val storage = FirebaseStorage.getInstance()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_edit_post, container, false)

        initView(view)
        initArguments()
        handleSaveButton()

        observeChangeResult()

        return view
    }

    private fun initView(view : View){
        resPhoto=view.findViewById(R.id.res_photo)
        galleryButton=view.findViewById(R.id.edit_button)
        resDescription=view.findViewById(R.id.res_rec_text)
        resLocation=view.findViewById(R.id.res_location)
        resName=view.findViewById(R.id.res_name)
        saveButton=view.findViewById(R.id.save_button)
        progressBar=view.findViewById(R.id.progress_bar)

        // Initialize ViewModel
        editViewModel = ViewModelProvider(this).get(EditPostViewModel::class.java)
    }

    private fun initArguments() {
        postId = arguments?.getString("postId").toString()
        resName.setText(arguments?.getString("restaurantName"))
        resLocation.setText(arguments?.getString("restaurantLocation"))
        resDescription.setText(arguments?.getString("restaurantRec"))

        val restaurantImage = arguments?.getString("restaurantImage")
        storage.reference.child(restaurantImage.toString()).downloadUrl.addOnSuccessListener {
            Glide.with(this)
                .load(it)
                .into(resPhoto)
        }

    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Set onClickListener for the gallery button
        galleryButton.setOnClickListener {
            openGallery()
        }
    }

    private val galleryLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val data: Intent? = result.data
            selectedImageUri = data?.data
            resPhoto.setImageURI(selectedImageUri)
        }
    }
    private fun openGallery() {
        val galleryIntent = Intent(Intent.ACTION_PICK)
        galleryIntent.type = "image/*"
        galleryLauncher.launch(galleryIntent)
    }

    private fun handleSaveButton(){
        saveButton.setOnClickListener {
            // Show progress bar
            progressBar.visibility = View.VISIBLE
            // Start a coroutine to delay the execution
            CoroutineScope(Dispatchers.Main).launch {
                delay(3000) // Delay for 3 seconds
                saveChanges()
                // Hide progress bar after 3 seconds
                progressBar.visibility = View.GONE
            }
        }
    }
    private fun saveChanges() {
        // Logic to save the changes
        val newResName = resName.text.toString()
        val newResLocation = resLocation.text.toString()
        val newResDescription = resDescription.text.toString()

        editViewModel.editPost(postId , newResName , newResLocation , newResDescription , selectedImageUri)
    }

    private fun observeChangeResult() {
        editViewModel.changeResult.observe(viewLifecycleOwner) { result ->
            when (result) {
                RequestStatus.SUCCESS -> {
                    // Hide progress bar when profile image upload is successful
                    progressBar.visibility = View.GONE
                    Toast.makeText(requireContext(), "השינויים נשמרו בהצלחה", Toast.LENGTH_SHORT).show()
                    navigate()
                }
                RequestStatus.FAILURE -> {
                    // Handle failure case if profile image upload fails (optional)
                    progressBar.visibility = View.GONE
                    Toast.makeText(requireContext(), "מצטערים , לא הצלחנו לעדכן את השינויים", Toast.LENGTH_SHORT).show()
                }
                else -> {
                    // Handle other cases if needed (optional)
                }
            }
        }
    }

    private fun navigate(){
        val navController = (requireActivity() as NavActivity).navController
        navController!!.navigate(R.id.action_editPostFragment_to_restaurantsFragment2)
    }
}