package com.dicoding.semaroam.view.start

import android.annotation.SuppressLint
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.dicoding.semaroam.R
import com.dicoding.semaroam.view.profile.ProfileActivity

class HomeActivity : AppCompatActivity() {
    private lateinit var sharedPreferences: SharedPreferences

    @SuppressLint("SetTextI18n")
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

        // Load user name from SharedPreferences and set it to the TextView
        val userName = sharedPreferences.getString("user_name", "User")
        hiUserTextView.text = getString(R.string.hi) + " " + userName
    }

}
