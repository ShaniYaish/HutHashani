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
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.util.query
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.textfield.TextInputEditText
import com.idz.huthashani.MainActivity
import com.idz.huthashani.NavActivity
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
        val navController = (requireActivity() as NavActivity).navController
        if (view != null) {
            initViews(view)
        }
        setupRecyclerView()
        setupSwipeRefresh()
        observePostViewModel()
        observeRequestStatus()
        observeSearchPost()

        restaurantsViewModel.getPosts("", "")

        return view
    }

    private fun initViews(view: View) {
        recyclerView = view.findViewById(R.id.posts_recycler_view)
        searchInput = view.findViewById(R.id.search_input)
        swipeRefreshLayout = view.findViewById(R.id.swipeRefreshLayout)
    }

    private fun setupRecyclerView() {
        recyclerView.layoutManager = GridLayoutManager(requireContext(), 2)
    }


    private fun setupSwipeRefresh(){
        swipeRefreshLayout.setOnRefreshListener {
            searchInput.text = Editable.Factory.getInstance().newEditable("")
            restaurantsViewModel.getPosts("", "")
            swipeRefreshLayout.isRefreshing = false
        }
    }

    private fun observePostViewModel() {
        val navController = (requireActivity() as NavActivity).navController

        if (navController != null) {
            restaurantsViewModel.posts.observe(viewLifecycleOwner) { posts: List<Post> ->
                var displayedPost: List<Post> = posts
                val query = searchInput.text.toString()

                if (query.isNotEmpty()) {
                    displayedPost = posts.filter { post ->
                        post.locationRest.contains(query, ignoreCase = true)
                    }
                }

                val postAdapter = PostAdapter(displayedPost, navController) // Now navController is non-null
                recyclerView.adapter = postAdapter
            }
        } else {
            Log.e("RestaurantsFragment", "NavController is null. Cannot set up PostAdapter.")
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

    private fun observeSearchPost() {
        val navController = (requireActivity() as NavActivity).navController
        searchInput.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(editable: Editable?) {
                val query = editable?.toString() ?: ""
                restaurantsViewModel.posts.value?.let { posts ->
                    val filteredPosts = if (query.isNotEmpty()) {
                        posts.filter { it.locationRest.contains(query, ignoreCase = true) }
                    } else {
                        posts
                    }

                    val postAdapter = PostAdapter(filteredPosts, navController!!)
                    recyclerView.adapter = postAdapter
                }
            }

            override fun beforeTextChanged(charSequence: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(charSequence: CharSequence?, start: Int, before: Int, count: Int) {}
        })
    }



}