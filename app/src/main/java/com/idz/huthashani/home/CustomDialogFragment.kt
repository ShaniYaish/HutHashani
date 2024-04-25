package com.idz.huthashani.home

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import com.idz.huthashani.R

class CustomDialogFragment : DialogFragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.dialog_fragment_layout, container, false)

        val closeButton = view.findViewById<ImageView>(R.id.close_button) // X button to close
        closeButton.setOnClickListener {
            dismiss() // Close the dialog
        }

        // Additional setup for your view, like setting text or images
        val textView = view.findViewById<TextView>(R.id.dialog_text)
        textView.text = arguments?.getString("dialog_text") // Get text from arguments

        return view
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)
        dialog.setCancelable(true) // Enables closing when clicking outside
        dialog.setCanceledOnTouchOutside(true) // Enables closing when touching outside
        return dialog
    }
}