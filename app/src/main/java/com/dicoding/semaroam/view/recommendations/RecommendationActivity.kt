package com.dicoding.semaroam.view.recommendations

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.dicoding.semaroam.R
import com.bumptech.glide.Glide
import android.widget.ImageView

class RecommendationActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recommendation)

        // Get recommendation data from Intent
        val description = intent.getStringExtra("Description")
        val category = intent.getStringExtra("Category")
        val placeId = intent.getStringExtra("Place_Id")
        val placeRatings = intent.getIntExtra("Place_Ratings", 0)
        val placeName = intent.getStringExtra("Place_Name")
        val city = intent.getStringExtra("City")
        val image = intent.getStringExtra("Image")
        val score = intent.getDoubleExtra("Score", 0.0)

        // Set the data to views
        findViewById<TextView>(R.id.description).text = description
        findViewById<TextView>(R.id.category).text = category
        findViewById<TextView>(R.id.place_id).text = placeId
        findViewById<TextView>(R.id.place_ratings).text = placeRatings.toString()
        findViewById<TextView>(R.id.place_name).text = placeName
        findViewById<TextView>(R.id.city).text = city
        Glide.with(this)
            .load(image)
            .into(findViewById<ImageView>(R.id.image))
        findViewById<TextView>(R.id.score).text = score.toString()
    }
}