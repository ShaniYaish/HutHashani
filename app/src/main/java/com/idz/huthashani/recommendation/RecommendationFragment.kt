package com.idz.huthashani.recommendation

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.URLUtil
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.idz.huthashani.R
import com.idz.huthashani.dao.Post
import com.idz.huthashani.utils.RequestStatus

class RecommendationFragment: Fragment() {

    private val newRecommendationViewModel: RecommendationViewModel by activityViewModels()

    private lateinit var view: View
    private lateinit var fullNameRest: EditText
    private lateinit var locationRest: EditText
    private lateinit var description: EditText
    private lateinit var attachPictureButton: ImageButton
    private lateinit var submitButton: LinearLayout
    private lateinit var progressBar: ProgressBar
    private lateinit var attachedPicture: Uri
    private lateinit var resImage: ImageView


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        view = inflater.inflate(
            R.layout.fragment_recommendation, container, false
        )

        initViews(view)
        handleSubmitButton()
        observeCreatePostStatus()
        handleAttachPostPicture()

        return view
    }

    private fun initViews(view: View) {
        val fullNameRestLL = view.findViewById<LinearLayout>(R.id.inputFullNameRest)
        val locationLL = view.findViewById<LinearLayout>(R.id.inputLocationRest)
        val desLL = view.findViewById<LinearLayout>(R.id.inputOpinionRest)

        fullNameRest = fullNameRestLL.findViewById(R.id.inputFullNameRest_text)
        locationRest = locationLL.findViewById(R.id.inputLocationRest_text)
        description = desLL.findViewById(R.id.inputOpinionRest_text)
        attachPictureButton = view.findViewById(R.id.post_attach_picture_button)
        resImage = view.findViewById(R.id.add_picture_image)
        submitButton = view.findViewById(R.id.publish_button)
        progressBar = view.findViewById(R.id.progress_bar_create_new_post)

    }

    private fun handleSubmitButton() {
        submitButton.setOnClickListener {
            createNewPost()
        }
    }

    private fun createNewPost() {
        val validationResponse = validateRecommendation(
            fullNameRest.text.toString(),locationRest.text.toString(), description.text.toString(),
            ::attachedPicture.isInitialized
        )

        if (validationResponse == null) {
            val newRecommendation = Post(
                fullNameRest = fullNameRest.text.toString(),
                locationRest = locationRest.text.toString(),
                description = description.text.toString(),
            )
            newRecommendationViewModel.createNewPost(newRecommendation, attachedPicture)
        }
        else{
            // Show the validation response as a toast message
            Toast.makeText(requireContext(), validationResponse, Toast.LENGTH_SHORT).show()
        }
    }

        private val pickImageContract =
            registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
                uri?.let {
                    attachedPicture = it
                    resImage.setImageURI(it)
                }
            }

        private fun handleAttachPostPicture() {
            attachPictureButton.setOnClickListener {
                pickImageContract.launch("image/*")
            }
        }



    private fun observeCreatePostStatus() {
        newRecommendationViewModel.requestStatus.observe(viewLifecycleOwner) { status: RequestStatus ->
            when(status) {
                //If the new status is RequestStatus.IN_PROGRESS, it hides certain UI elements,
                // and shows a progress bar (progressBar) to indicate that a request is in progress.
                RequestStatus.IN_PROGRESS ->{
                    fullNameRest.visibility = View.GONE
                    locationRest.visibility = View.GONE
                    description.visibility = View.GONE
                    attachPictureButton.visibility = View.GONE
                    submitButton.visibility = View.GONE
                    progressBar.visibility = View.VISIBLE
                }
                RequestStatus.SUCCESS ->
                    findNavController().popBackStack()

                else -> {}
            }
        }
    }

    private fun validateRecommendation(nameOfRest : String,location : String, description :String,pictureInitializeStatus: Boolean ): String? {
        if (nameOfRest.length > 30) {
            return "שם המסעדה צריך להיות עד 30 תווים"
        }
        if (nameOfRest.isEmpty()) {
            return "אנא הכנס את שם המסעדה"
        }
        if (location.isEmpty()) {
            return "אנא הכנס היכן המסעדה נמצאת"
        }
        if (description.length < 6) {
            return "תיאור צריך להיות לפחות 6 תווים"
        }

        if (description.length > 150) {
            return "תיאור צריך להיות עד 150 תווים"
        }

        if (!pictureInitializeStatus)
        {
            return "אנא בחר תמונה"
        }

        return null
    }

    override fun onDestroyView() {
        super.onDestroyView()
        newRecommendationViewModel.clear()
    }

}