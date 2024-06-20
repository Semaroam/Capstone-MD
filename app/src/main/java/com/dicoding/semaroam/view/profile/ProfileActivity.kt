package com.dicoding.semaroam.view.profile

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.Dialog
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.dicoding.semaroam.data.retrofit.ApiConfig
import com.dicoding.semaroam.data.retrofit.AuthService
import com.dicoding.semaroam.databinding.ActivityProfileBinding
import com.dicoding.semaroam.view.login.LoginActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import com.google.android.material.tabs.TabLayoutMediator
import androidx.viewpager2.widget.ViewPager2
import com.dicoding.semaroam.R
import com.dicoding.semaroam.adapter.MyPagerAdapter
import com.dicoding.semaroam.utils.PreferencesHelper
import com.dicoding.semaroam.view.start.HomeActivity
import com.google.android.material.tabs.TabLayout

class ProfileActivity : AppCompatActivity() {
    private lateinit var nameTextView: TextView
    private lateinit var usernameTextView: TextView
    private lateinit var logoutButton: ImageButton
    private lateinit var backButton: ImageButton
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var binding: ActivityProfileBinding
    private lateinit var authService: AuthService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)



        val tabLayout = findViewById<TabLayout>(R.id.tab_layout)
        val viewPager = findViewById<ViewPager2>(R.id.view_pager)

        val pagerAdapter = MyPagerAdapter(this)
        viewPager.adapter = pagerAdapter

        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.text = when (position) {
                0 -> "Aktivitas"
                else -> "Tentang"
            }
        }.attach()

        nameTextView = binding.textView2
        usernameTextView = binding.textView3
        logoutButton = binding.logoutButton
        backButton = binding.backButton
        sharedPreferences = getSharedPreferences("user_pref", MODE_PRIVATE)
        val preferencesHelper = PreferencesHelper(this)
        preferencesHelper.setLoggedInStatus(false)
        authService = ApiConfig.getApiService()

        loadUserData()

        logoutButton.setOnClickListener {
            confirmLogout()
        }

        backButton.setOnClickListener {
            navigateToHome()
        }
    }

    @SuppressLint("SetTextI18n")
    private fun loadUserData() {
        val name = sharedPreferences.getString("user_name", "Unknown")
        val username = sharedPreferences.getString("user_username", "Unknown")

        nameTextView.text = name
        usernameTextView.text = "@$username"
    }

    private fun confirmLogout() {
        val alertDialog = AlertDialog.Builder(this)
        alertDialog.setTitle("Konfirmasi")
        alertDialog.setMessage("Apakah Anda yakin ingin keluar?")
        alertDialog.setPositiveButton("Ya") { _, _ ->
            val progressDialog = Dialog(this)
            progressDialog.setContentView(R.layout.dialog_progress)
            progressDialog.setCancelable(false)
            progressDialog.show()

            // Simulate logout process with delay
            Handler(Looper.getMainLooper()).postDelayed({
                progressDialog.dismiss()
                logoutUser()
            }, 2000) // delay 2 seconds
        }
        alertDialog.setNegativeButton("Tidak") { dialog, _ ->
            dialog.dismiss()
        }
        alertDialog.show()
    }

    private fun logoutUser() {
        authService.logoutUser().enqueue(object : Callback<Unit> {
            override fun onResponse(call: Call<Unit>, response: Response<Unit>) {
                if (response.isSuccessful) {
                    clearUserData()
                    navigateToLogin()
                    Toast.makeText(this@ProfileActivity, "User was logged out successfully!", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this@ProfileActivity, "Logout failed", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<Unit>, t: Throwable) {
                Toast.makeText(this@ProfileActivity, "Logout failed: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun clearUserData() {
        with(sharedPreferences.edit()) {
            clear()
            apply()
        }
    }

    private fun navigateToLogin() {
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun navigateToHome() {
        val intent = Intent(this, HomeActivity::class.java)
        startActivity(intent)
        finish()
    }
}