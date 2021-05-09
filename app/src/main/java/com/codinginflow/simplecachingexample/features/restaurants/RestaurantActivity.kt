package com.codinginflow.simplecachingexample.features.restaurants

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import androidx.activity.viewModels
import androidx.core.view.isVisible
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.codinginflow.simplecachingexample.R
import com.codinginflow.simplecachingexample.databinding.ActivityRestaurantBinding
import com.codinginflow.simplecachingexample.util.Resource
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RestaurantActivity : AppCompatActivity() {

    private val restaurantViewModel: RestaurantViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityRestaurantBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val restaurantAdapter = RestaurantsAdapter()

        binding.apply {
            recyclerViewRestaurants.apply {
                adapter = restaurantAdapter
                layoutManager = LinearLayoutManager(this@RestaurantActivity)
            }

            restaurantViewModel.restaurants.observe(this@RestaurantActivity) { result ->
                restaurantAdapter.submitList(result.data)

                progressBar.isVisible = result is Resource.Loading && result.data.isNullOrEmpty()

                textViewError.apply {
                    isVisible = result is Resource.Error && result.data.isNullOrEmpty()
                    text = result.error?.localizedMessage
                }
            }
        }
    }
}