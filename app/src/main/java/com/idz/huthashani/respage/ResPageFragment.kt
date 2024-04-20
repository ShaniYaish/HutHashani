package com.idz.huthashani.respage

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.idz.huthashani.R

class ResPageFragment : Fragment() {

    companion object {
        fun newInstance() = ResPageFragment()
    }

    private lateinit var viewModel: ResPageViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_res_page, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(ResPageViewModel::class.java)
        // TODO: Use the ViewModel
    }

}