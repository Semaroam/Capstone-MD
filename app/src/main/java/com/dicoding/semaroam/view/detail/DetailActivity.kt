package com.dicoding.semaroam.view.detail

import android.content.Intent
import android.content.res.Resources
import android.os.Bundle
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.dicoding.semaroam.R
import com.dicoding.semaroam.view.start.HomeActivity
import kotlin.math.roundToInt

class DetailActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        val backButton = findViewById<ImageButton>(R.id.back_button)
        backButton.setOnClickListener {
            val intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)
        }

        // Get the intent data
        val description = intent.getStringExtra("Description")
        val category = intent.getStringExtra("Category")
        val placeId = intent.getIntExtra("Place_Id", 0)
        val placeRatings = intent.getIntExtra("Place_Ratings", 0)
        val placeName = intent.getStringExtra("Place_Name")
        val city = intent.getStringExtra("City")
        val imageUrl = intent.getStringExtra("Image")

        // Set the data to views
        findViewById<TextView>(R.id.description).text = description
        findViewById<TextView>(R.id.category).text = category
        findViewById<TextView>(R.id.place_id).text = placeId.toString()
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
    }
}
