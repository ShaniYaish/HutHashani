package com.idz.huthashani.restaurants

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.textfield.TextInputEditText
import com.idz.huthashani.MainActivity
import com.idz.huthashani.R
import com.idz.huthashani.dao.Post
import com.idz.huthashani.profile.UserProfile
import com.idz.huthashani.utils.RequestStatus

class RestaurantsFragment: Fragment() {

    private val restaurantsViewModel: RestaurantsViewModel by viewModels()
    private lateinit var swipeRefreshLayout: SwipeRefreshLayout
    private lateinit var recyclerView: RecyclerView
    private lateinit var searchInput: TextInputEditText

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view: View?= inflater.inflate(R.layout.fragment_restaurants, container, false)
        if (view != null) {
            initViews(view)
        }
        setupRecyclerView()
        setupSwipeRefresh()
        observePostViewModel()
        observeRequestStatus()

        restaurantsViewModel.getPosts("", "")

        return view
    }

    private fun initViews(view: View) {
        recyclerView = view.findViewById(R.id.posts_recycler_view)
        searchInput = view.findViewById(R.id.search_input)
        swipeRefreshLayout = view.findViewById(R.id.swipeRefreshLayout)
    }

    private fun setupRecyclerView() {
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
    }


    private fun setupSwipeRefresh(){
        swipeRefreshLayout.setOnRefreshListener {
            searchInput.text = Editable.Factory.getInstance().newEditable("")
            restaurantsViewModel.getPosts("", "")
            swipeRefreshLayout.isRefreshing = false
        }
    }

    private fun observePostViewModel() {
        restaurantsViewModel.posts.observe(viewLifecycleOwner) { posts: List<Post> ->
            var displayedPost: List<Post> = posts;
            val query = searchInput.text.toString()
            if(query.isNotEmpty()) {
                displayedPost = posts.filter { post ->
                    post.locationRest.contains(query, ignoreCase = true)
                }
            }

            val postAdapter = PostAdapter(displayedPost)
            recyclerView.adapter = postAdapter
        }
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