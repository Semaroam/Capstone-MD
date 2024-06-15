package com.dicoding.semaroam.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.cardview.widget.CardView
import com.dicoding.semaroam.R

class UlasanFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_ulasan, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val reviewCard: CardView = view.findViewById(R.id.review)
        reviewCard.setOnClickListener {
            // Handle review card click
        }

        val likeCard: CardView = view.findViewById(R.id.like)
        likeCard.setOnClickListener {
            // Handle like card click
        }
    }
}