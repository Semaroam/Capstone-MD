package com.dicoding.semaroam.view.detail

import android.content.res.Resources
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSnapHelper
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.dicoding.semaroam.R
import com.dicoding.semaroam.adapter.RecommendationAdapter
import com.dicoding.semaroam.data.retrofit.ApiConfig
import com.dicoding.semaroam.data.retrofit.RecommendationResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.math.roundToInt

class DetailActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        if (intent.getBooleanExtra("FromRecommendation", false)) {
            findViewById<TextView>(R.id.recommendation_title).visibility = View.GONE
        }
        val backButton = findViewById<ImageButton>(R.id.back_button)
        backButton.setOnClickListener {
            finish()
        }


        // Get the intent data
        val description = intent.getStringExtra("Description")
        val category = intent.getStringExtra("Category")
        val placeRatings = intent.getIntExtra("Place_Ratings", 0)
        val placeName = intent.getStringExtra("Place_Name")
        val city = intent.getStringExtra("City")
        val imageUrl = intent.getStringExtra("Image")
        val placeId = intent.getIntExtra("Place_Id", 0)

        // Set the data to views
        findViewById<TextView>(R.id.description).text = description
        findViewById<TextView>(R.id.category).text = category
        findViewById<TextView>(R.id.place_ratings).text = placeRatings.toString()
        findViewById<TextView>(R.id.place_name).text = placeName
        findViewById<TextView>(R.id.city).text = city

        fun dpToPx(dp: Int): Int {
            val density = Resources.getSystem().displayMetrics.density
            return (dp * density).roundToInt()
        }

        val imageView: ImageView = findViewById(R.id.image)
        Glide.with(this)
            .load(imageUrl)
            .transform(CenterCrop(), RoundedCorners(dpToPx(15)))
            .into(imageView)

        // Initialize RecyclerView
        val recyclerView: RecyclerView = findViewById(R.id.recycler_view)

        // Set the Layout Manager
        val layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        recyclerView.layoutManager = layoutManager

        // Initialize SnapHelper
        val snapHelper = LinearSnapHelper()
        snapHelper.attachToRecyclerView(recyclerView)

        // Initialize AuthService
        val authService = ApiConfig.getApiService()

        // Call getRecommendations API
        authService.getRecommendations(placeId).enqueue(object : Callback<RecommendationResponse> {
            override fun onResponse(call: Call<RecommendationResponse>, response: Response<RecommendationResponse>) {
                if (response.isSuccessful) {
                    val recommendations = response.body()?.data ?: emptyList()
                    Log.d("API_RESPONSE", recommendations.toString())
                    recyclerView.adapter = RecommendationAdapter(recommendations)
                } else {
                    // Handle error
                }
            }

            override fun onFailure(call: Call<RecommendationResponse>, t: Throwable) {
                // Handle failure
            }
        })
    }
}