package com.dicoding.semaroam.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dicoding.semaroam.R
import com.dicoding.semaroam.data.retrofit.RecommendationData
import com.dicoding.semaroam.view.detail.DetailActivity

class RecommendationAdapter(private val recommendations: List<RecommendationData>) : RecyclerView.Adapter<RecommendationAdapter.ViewHolder>() {

    private var onItemClickListener: ((RecommendationData) -> Unit)? = null

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val placeName: TextView = view.findViewById(R.id.place_name)
        val placeImage: ImageView = view.findViewById(R.id.place_image)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_recommendation, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val recommendation = recommendations[position]
        holder.placeName.text = recommendation.Place_Name
        Glide.with(holder.itemView.context)
            .load(recommendation.Image)
            .into(holder.placeImage)

        holder.itemView.setOnClickListener {
            onItemClickListener?.invoke(recommendation)
            val intent = Intent(holder.itemView.context, DetailActivity::class.java)
            intent.putExtra("Description", recommendation.Description)
            intent.putExtra("Category", recommendation.Category)
            intent.putExtra("Place_Id", recommendation.Place_Id)
            intent.putExtra("Place_Ratings", recommendation.Place_Ratings)
            intent.putExtra("Place_Name", recommendation.Place_Name)
            intent.putExtra("City", recommendation.City)
            intent.putExtra("Image", recommendation.Image)
            intent.putExtra("Score", recommendation.Score)
            intent.putExtra("FromRecommendation", true) // Add this line
            holder.itemView.context.startActivity(intent)
        }
    }

    override fun getItemCount() = recommendations.size
}