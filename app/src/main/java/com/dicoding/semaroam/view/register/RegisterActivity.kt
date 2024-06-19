package com.dicoding.semaroam.view.register

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.dicoding.semaroam.R
import com.dicoding.semaroam.data.retrofit.ApiConfig
import com.dicoding.semaroam.data.retrofit.SignupRequest
import com.dicoding.semaroam.data.retrofit.SignupResponse
import com.dicoding.semaroam.databinding.ActivityRegisterBinding
import com.dicoding.semaroam.view.login.LoginActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegisterActivity : AppCompatActivity() {
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var binding: ActivityRegisterBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)
        sharedPreferences = getSharedPreferences("user_pref", MODE_PRIVATE)

        val registerButton  = binding.daftarButton
        val nameEditText = binding.etName
        val usernameEditText = binding.etUsername
        val passwordEditText = binding.etPassword
        val signInLink = binding.signinLink

        registerButton.setOnClickListener {
            val name = nameEditText.editText?.text.toString().trim()
            val username = usernameEditText.editText?.text.toString().trim()
            val password = passwordEditText.editText?.text.toString().trim()

            Log.d("RegisterActivity", "Register button clicked with name: $name, username: $username")
            Log.d("daftarButton", "Register button clicked with password: ${binding.etName}")
            if (validateRegister(name, username, password)) {
                registerUser(name, username, password)
            }
        }

        signInLink.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }
    }

    private fun validateRegister(name: String, username: String, password: String): Boolean {
        if (name.isEmpty()) {
            Toast.makeText(this, "Nama tidak boleh kosong", Toast.LENGTH_SHORT).show()
            return false
        }

        if (username.isEmpty()) {
            Toast.makeText(this, "Nama pengguna tidak boleh kosong", Toast.LENGTH_SHORT).show()
            return false
        }

        if (password.isEmpty()) {
            Toast.makeText(this, "Kata sandi tidak boleh kosong", Toast.LENGTH_SHORT).show()
            return false
        }

        if (password.length < 6) {
            Toast.makeText(this, "Kata sandi minimal harus 6 karakter", Toast.LENGTH_SHORT).show()
            return false
        }

        return true
    }

    private fun registerUser(name: String, username: String, password: String) {
        val progressBar = findViewById<ProgressBar>(R.id.progressBar)
        progressBar.visibility = View.VISIBLE

        val apiService = ApiConfig.getApiService()
        val signupRequest = SignupRequest(username, password, name)

        Log.d("RegisterActivity", "Sending signup request: $signupRequest")
        apiService.signupUser(signupRequest).enqueue(object : Callback<SignupResponse> {
            override fun onResponse(call: Call<SignupResponse>, response: Response<SignupResponse>) {
                progressBar.visibility = View.GONE

                if (response.isSuccessful) {
                    val signupResponse = response.body()
                    Log.d("RegisterActivity", "Signup response received: $signupResponse")
                    if (signupResponse != null) {

                        with(sharedPreferences.edit()) {
                            putString("user_name", signupResponse.data.nama)
                            putString("user_username", signupResponse.data.username)
                            apply()
                        }
                        Toast.makeText(this@RegisterActivity, signupResponse.message, Toast.LENGTH_SHORT).show()
                        val intent = Intent(this@RegisterActivity, LoginActivity::class.java)
                        startActivity(intent)
                        finish()
                    }
                } else {
                    val errorBody = response.errorBody()?.string()
                    Log.e("RegisterActivity", "Signup error response: $errorBody")
                    Toast.makeText(this@RegisterActivity, "Registration failed: $errorBody", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<SignupResponse>, t: Throwable) {
                progressBar.visibility = View.GONE

                Log.e("RegisterActivity", "Network error: ${t.localizedMessage}", t)
                Toast.makeText(this@RegisterActivity, "Network error: ${t.localizedMessage}", Toast.LENGTH_SHORT).show()
            }
        })
    }
}