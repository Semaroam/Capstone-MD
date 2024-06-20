package com.dicoding.semaroam.view.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dicoding.semaroam.R
import com.dicoding.semaroam.data.retrofit.PlaceData
import com.dicoding.semaroam.view.detail.DetailActivity
import android.text.SpannableString
import android.text.Spanned
import android.text.style.ForegroundColorSpan
import android.graphics.Color


class CategoryResultsAdapter : RecyclerView.Adapter<CategoryResultsAdapter.ViewHolder>() {
    private val places = mutableListOf<PlaceData>()

    fun setPlaces(places: List<PlaceData>) {
        this.places.clear()
        this.places.addAll(places)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_category_result, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(places[position])
    }

    override fun getItemCount(): Int {
        return places.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val placeImage: ImageView = itemView.findViewById(R.id.place_image)
        private val placeName: TextView = itemView.findViewById(R.id.place_name)
        private val placeCity: TextView = itemView.findViewById(R.id.place_city)
        private val placeRatings: TextView = itemView.findViewById(R.id.place_ratings)

        fun bind(place: PlaceData) {
            Glide.with(itemView.context)
                .load(place.Image)
                .into(placeImage)

            placeName.text = place.Place_Name
            placeCity.text = place.City
            // Format the ratings
            val star = "\u2605"
            val ratingText = String.format("%s%d / 5", star, place.Place_Ratings)

            // Create a SpannableString
            val spannableString = SpannableString(ratingText)

            // Create a ForegroundColorSpan
            val colorSpan = ForegroundColorSpan(Color.YELLOW)

            // Apply the ForegroundColorSpan to the SpannableString
            spannableString.setSpan(colorSpan, 0, 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)

            // Set the text to the TextView
            placeRatings.text = spannableString

            itemView.setOnClickListener {
                val intent = Intent(itemView.context, DetailActivity::class.java).apply {
                    putExtra("Description", place.Description)
                    putExtra("Category", place.Category)
                    putExtra("Place_Id", place.Place_Id)
                    putExtra("Place_Ratings", place.Place_Ratings)
                    putExtra("Place_Name", place.Place_Name)
                    putExtra("City", place.City)
                    putExtra("Image", place.Image)
                }
                itemView.context.startActivity(intent)

            }
        }
    }
}