package com.dicoding.semaroam.view.searchresult

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dicoding.semaroam.R
import com.dicoding.semaroam.data.retrofit.ApiConfig
import com.dicoding.semaroam.data.retrofit.PlaceResponse
import com.dicoding.semaroam.view.adapter.CategoryResultsAdapter
import com.dicoding.semaroam.view.start.HomeActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CategoryResultsActivity : AppCompatActivity() {
    private lateinit var categoryResultsAdapter: CategoryResultsAdapter

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_category_results)

        val category = intent.getStringExtra("category")

        val titleTextView: TextView = findViewById(R.id.title_text_view)
        titleTextView.text = category

        val backButton = findViewById<ImageButton>(R.id.back_button)
        backButton.setOnClickListener {
            val intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)
        }

        val recyclerView: RecyclerView = findViewById(R.id.category_results_recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(this)
        categoryResultsAdapter = CategoryResultsAdapter()
        recyclerView.adapter = categoryResultsAdapter

        if (category != null) {
            fetchPlacesByCategory(category)
        }
    }

    private fun fetchPlacesByCategory(category: String) {
        val client = ApiConfig.getApiService().getPlaceByCategory(category)
        client.enqueue(object : Callback<PlaceResponse> {
            override fun onResponse(call: Call<PlaceResponse>, response: Response<PlaceResponse>) {
                if (response.isSuccessful) {
                    val placeList = response.body()?.data
                    if (placeList != null) {
                        categoryResultsAdapter.setPlaces(placeList)
                    }
                }
            }

            override fun onFailure(call: Call<PlaceResponse>, t: Throwable) {
                // Handle error
            }
        })
    }
}