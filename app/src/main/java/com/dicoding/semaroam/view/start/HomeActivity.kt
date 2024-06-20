package com.dicoding.semaroam.view.start

import android.annotation.SuppressLint
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.SearchView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dicoding.semaroam.R
import com.dicoding.semaroam.data.retrofit.ApiConfig
import com.dicoding.semaroam.data.retrofit.PlaceData
import com.dicoding.semaroam.data.retrofit.PlaceResponse
import com.dicoding.semaroam.view.adapter.CategoryAdapter
import com.dicoding.semaroam.view.searchresult.CategoryResultsActivity
import com.dicoding.semaroam.view.searchresult.SearchResultsActivity
import com.dicoding.semaroam.view.detail.DetailActivity
import com.dicoding.semaroam.view.profile.ProfileActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class HomeActivity : AppCompatActivity() {
    private lateinit var sharedPreferences: SharedPreferences
    private var placeIndex = 0
    private val highlightedPlaces = mutableListOf<PlaceData>()
    private val handler = Handler(Looper.getMainLooper())
    private lateinit var runnable: Runnable

    @SuppressLint("SetTextI18n", "MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        val profileButton: ImageButton = findViewById(R.id.et_profile)
        val hiUserTextView: TextView = findViewById(R.id.tv_hi_user)

        sharedPreferences = getSharedPreferences("user_pref", MODE_PRIVATE)

        profileButton.setOnClickListener {
            val intent = Intent(this@HomeActivity, ProfileActivity::class.java)
            startActivity(intent)
        }

        val userName = sharedPreferences.getString("user_name", "User")
        hiUserTextView.text = getString(R.string.hi) + " " + userName

        val categories = listOf("Bahari", "Budaya", "Taman Hiburan", "Cagar Alam", "Tempat Ibadah")
        val categoryRecyclerView: RecyclerView = findViewById(R.id.category_recycler_view)
        categoryRecyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        categoryRecyclerView.adapter = CategoryAdapter(categories) { category ->
            openCategoryResultsActivity(category)
        }

        val searchView: SearchView = findViewById(R.id.search_view)
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (!query.isNullOrEmpty()) {
                    val intent = Intent(this@HomeActivity, SearchResultsActivity::class.java)
                    val categories = listOf("Bahari", "Budaya", "Taman Hiburan", "Cagar Alam", "Tempat Ibadah")
                    try {
                        val placeId = query.toInt()
                        intent.putExtra("placeId", placeId)
                    } catch (e: NumberFormatException) {
                        if (categories.contains(query)) {
                            intent.putExtra("category", query)
                        } else {
                            intent.putExtra("keyword", query)
                        }
                    }
                    startActivity(intent)
                }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }
        })

        fetchHighlightedPlaces()
    }

    private fun fetchHighlightedPlaces() {
        val client = ApiConfig.getApiService().getAllPlaces()
        client.enqueue(object : Callback<PlaceResponse> {
            override fun onResponse(call: Call<PlaceResponse>, response: Response<PlaceResponse>) {
                if (response.isSuccessful) {
                    val placeList = response.body()?.data
                    if (placeList != null) {
                        highlightedPlaces.clear()
                        highlightedPlaces.addAll(placeList.filter { it.Place_Ratings == 5 })
                        displayHighlightedPlace()
                    } else {
                        Log.e("HomeActivity", "No data available")
                        Toast.makeText(this@HomeActivity, "No data available", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Log.e("HomeActivity", "Failed to fetch data: ${response.message()}")
                    Toast.makeText(this@HomeActivity, "Failed to fetch data", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<PlaceResponse>, t: Throwable) {
                Log.e("HomeActivity", "Network error: ${t.message}")
                Toast.makeText(this@HomeActivity, "Network error", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun displayHighlightedPlace() {
        if (highlightedPlaces.isNotEmpty()) {
            val place = highlightedPlaces[placeIndex]
            val container: LinearLayout = findViewById(R.id.highlight_container)
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
                val intent = Intent(this@HomeActivity, DetailActivity::class.java).apply {
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

            // Update placeIndex and make sure it doesn't exceed the size of highlightedPlaces
            placeIndex = (placeIndex + 1) % highlightedPlaces.size

            // Schedule the next display
            runnable = Runnable { displayHighlightedPlace() }
            handler.postDelayed(runnable, 3000)
        }
    }

    private fun openCategoryResultsActivity(category: String) {
        val intent = Intent(this@HomeActivity, CategoryResultsActivity::class.java)
        intent.putExtra("category", category)
        startActivity(intent)
    }

    @Deprecated("Deprecated in Java")
    @SuppressLint("MissingSuperCall")
    override fun onBackPressed() {
        AlertDialog.Builder(this)
            .setTitle("Konfirmasi")
            .setMessage("Apakah Anda yakin ingin keluar?")
            .setPositiveButton("Ya") { _, _ ->
                // Hapus data pengguna dari SharedPreferences
                val editor = sharedPreferences.edit()
                editor.clear()
                editor.apply()

                // Arahkan pengguna ke MainActivity
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                finish()
            }
            .setNegativeButton("Tidak") { dialog, _ ->
                // Tutup dialog
                dialog.dismiss()
            }
            .create()
            .show()
    }

    override fun onDestroy() {
        super.onDestroy()
        handler.removeCallbacks(runnable)
    }
}