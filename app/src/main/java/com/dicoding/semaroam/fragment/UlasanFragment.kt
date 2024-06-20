package com.dicoding.semaroam.fragment

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
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

        val reviewButton: CardView = view.findViewById(R.id.review)
        val likeButton: CardView = view.findViewById(R.id.like)

        reviewButton.setOnClickListener {
            showComingSoonDialog()
        }

        likeButton.setOnClickListener {
            showComingSoonDialog()
        }
    }

    private fun showComingSoonDialog() {
        AlertDialog.Builder(context)
            .setTitle("Segera Hadir")
            .setMessage("Fitur ini sedang dalam pengembangan.")
            .setPositiveButton("OK") { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }
}