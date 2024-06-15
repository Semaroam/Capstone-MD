package com.dicoding.semaroam.view.searchresult

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.dicoding.semaroam.R
import com.dicoding.semaroam.data.retrofit.ApiConfig
import com.dicoding.semaroam.data.retrofit.PlaceData
import com.dicoding.semaroam.data.retrofit.PlaceDetailResponse
import com.dicoding.semaroam.data.retrofit.PlaceResponse
import com.dicoding.semaroam.view.detail.DetailActivity
import com.dicoding.semaroam.view.start.HomeActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SearchResultsActivity : AppCompatActivity() {
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search_results)

        val category = intent.getStringExtra("category")
        val keyword = intent.getStringExtra("keyword")
        val placeId = intent.getIntExtra("placeId", -1)

        val titleTextView: TextView = findViewById(R.id.title_text_view)
        titleTextView.text = category ?: keyword ?: placeId.toString()

        val backButton = findViewById<ImageButton>(R.id.back_button)
        backButton.setOnClickListener {
            val intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)
        }

        if (category != null) {
            searchPlacesByCategory(category)
        } else if (keyword != null) {
            searchPlacesByKeyword(keyword)
        } else if (placeId != -1) {
            searchPlaceById(placeId)
        }
    }

    private fun searchPlacesByCategory(category: String) {
        val client = ApiConfig.getApiService().getPlaceByCategory(category)
        client.enqueue(object : Callback<PlaceResponse> {
            override fun onResponse(call: Call<PlaceResponse>, response: Response<PlaceResponse>) {
                handleResponse(response, R.id.search_results_container)
            }

            override fun onFailure(call: Call<PlaceResponse>, t: Throwable) {
                handleFailure(t)
            }
        })
    }

    private fun searchPlacesByKeyword(keyword: String) {
        val client = ApiConfig.getApiService().getPlaceByKeyword(keyword)
        client.enqueue(object : Callback<PlaceResponse> {
            override fun onResponse(call: Call<PlaceResponse>, response: Response<PlaceResponse>) {
                handleResponse(response, R.id.search_results_container)
            }

            override fun onFailure(call: Call<PlaceResponse>, t: Throwable) {
                handleFailure(t)
            }
        })
    }

    private fun searchPlaceById(placeId: Int) {
        val client = ApiConfig.getApiService().getPlaceById(placeId)
        client.enqueue(object : Callback<PlaceDetailResponse> {
            override fun onResponse(call: Call<PlaceDetailResponse>, response: Response<PlaceDetailResponse>) {
                if (response.isSuccessful) {
                    val place = response.body()?.data
                    if (place != null) {
                        // Update the UI with the search result
                        displayPlace(place)
                    } else {
                        Log.e("SearchResultsActivity", "No data available")
                        Toast.makeText(this@SearchResultsActivity, "No data available", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Log.e("SearchResultsActivity", "Failed to fetch data: ${response.message()}")
                    Toast.makeText(this@SearchResultsActivity, "Failed to fetch data", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<PlaceDetailResponse>, t: Throwable) {
                Log.e("SearchResultsActivity", "Network error: ${t.message}")
                Toast.makeText(this@SearchResultsActivity, "Network error", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun handleResponse(response: Response<PlaceResponse>, containerId: Int) {
        if (response.isSuccessful) {
            val placeList = response.body()?.data
            if (placeList != null) {
                // Update the UI with the search results
                displayPlaces(placeList, containerId)
            } else {
                Log.e("SearchResultsActivity", "No data available")
                Toast.makeText(this@SearchResultsActivity, "No data available", Toast.LENGTH_SHORT).show()
            }
        } else {
            Log.e("SearchResultsActivity", "Places not found: ${response.message()}")
            Toast.makeText(this@SearchResultsActivity, "Places not found", Toast.LENGTH_SHORT).show()
        }
    }

    private fun handleFailure(t: Throwable) {
        Log.e("SearchResultsActivity", "Network error: ${t.message}")
        Toast.makeText(this@SearchResultsActivity, "Network error", Toast.LENGTH_SHORT).show()
    }

    private fun displayPlaces(places: List<PlaceData>, containerId: Int) {
        val container: LinearLayout = findViewById(containerId)
        container.removeAllViews() // Clear previous views if any

        places.forEach { place ->
            val cardView = layoutInflater.inflate(R.layout.item_place_card, container, false)

            val image: ImageView = cardView.findViewById(R.id.image)
            val placeName: TextView = cardView.findViewById(R.id.place_name)
            val city: TextView = cardView.findViewById(R.id.city)
            val placeRatings: TextView = cardView.findViewById(R.id.place_ratings)

            // Load image using Glide
            Glide.with(this)
                .load(place.Image)
                .into(image)

            placeName.text = place.Place_Name
            city.text = place.City
            placeRatings.text = place.Place_Ratings.toString()

            // Set click listener on card
            cardView.setOnClickListener {
                val intent = Intent(this@SearchResultsActivity, DetailActivity::class.java).apply {
                    putExtra("Description", place.Description)
                    putExtra("Category", place.Category)
                    putExtra("Place_Id", place.Place_Id)
                    putExtra("Place_Ratings", place.Place_Ratings)
                    putExtra("Place_Name", place.Place_Name)
                    putExtra("City", place.City)
                    putExtra("Image", place.Image)
                }
                startActivity(intent)
            }

            container.addView(cardView)
        }
    }

    private fun displayPlace(place: PlaceData) {
        val container: LinearLayout = findViewById(R.id.search_results_container)
        container.removeAllViews() // Clear previous views if any

        val cardView = layoutInflater.inflate(R.layout.item_place_card, container, false)

        val image: ImageView = cardView.findViewById(R.id.image)
        val placeName: TextView = cardView.findViewById(R.id.place_name)
        val city: TextView = cardView.findViewById(R.id.city)
        val placeRatings: TextView = cardView.findViewById(R.id.place_ratings)

        // Load image using Glide
        Glide.with(this)
            .load(place.Image)
            .into(image)

        placeName.text = place.Place_Name
        city.text = place.City
        placeRatings.text = place.Place_Ratings.toString()

        // Set click listener on card
        cardView.setOnClickListener {
            val intent = Intent(this@SearchResultsActivity, DetailActivity::class.java).apply {
                putExtra("Description", place.Description)
                putExtra("Category", place.Category)
                putExtra("Place_Id", place.Place_Id)
                putExtra("Place_Ratings", place.Place_Ratings)
                putExtra("Place_Name", place.Place_Name)
                putExtra("City", place.City)
                putExtra("Image", place.Image)
            }
            startActivity(intent)
        }

        container.addView(cardView)
    }
}