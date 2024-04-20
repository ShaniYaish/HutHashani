package com.idz.huthashani.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.idz.huthashani.R


class HomeFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: WishCardAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.fragment_home, container, false)

        // Initialize RecyclerView
        recyclerView = rootView.findViewById(R.id.recyclerView)
        recyclerView.layoutManager = GridLayoutManager(requireContext(), 2)
        adapter = WishCardAdapter(generateDummyData()) // Pass your data list here
        recyclerView.adapter = adapter

        return rootView
    }

    // Generate dummy data for testing
    private fun generateDummyData(): List<String> {
        val data = mutableListOf<String>()
        for (i in 1..20) {
            data.add("Item $i")
        }
        return data
    }
}
