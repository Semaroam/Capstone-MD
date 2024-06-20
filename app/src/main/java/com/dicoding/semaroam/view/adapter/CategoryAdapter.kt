package com.dicoding.semaroam.view.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.dicoding.semaroam.R


class CategoryAdapter(private val categories: List<String>, private val onCategoryClick: (String) -> Unit) : RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder>() {

    inner class CategoryViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val textView: TextView = view.findViewById(R.id.category_text)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        val layoutId = when (categories[viewType]) {
            "Bahari" -> R.layout.item_category_bahari
            "Budaya" -> R.layout.item_category_budaya
            "Taman Hiburan" -> R.layout.item_category_taman_hiburan
            "Cagar Alam" -> R.layout.item_category_cagar_alam
            "Tempat Ibadah" -> R.layout.item_category_tempat_ibadah
            else -> R.layout.item_category_default
        }
        val view = LayoutInflater.from(parent.context).inflate(layoutId, parent, false)
        return CategoryViewHolder(view)
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        val category = categories[position]
        holder.textView.text = category
        holder.itemView.setOnClickListener { onCategoryClick(category) }
    }

    override fun getItemCount() = categories.size
}