package com.dicoding.semaroam.view.login

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.dicoding.semaroam.data.retrofit.ApiConfig
import com.dicoding.semaroam.data.retrofit.LoginRequest
import com.dicoding.semaroam.data.retrofit.LoginResponse
import com.dicoding.semaroam.databinding.ActivityLoginBinding
import com.dicoding.semaroam.view.register.RegisterActivity
import com.dicoding.semaroam.view.start.HomeActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sharedPreferences = getSharedPreferences("user_pref", MODE_PRIVATE)

        val loginButton = binding.loginButton
        val usernameEditText = binding.etUsername
        val passwordEditText = binding.etPassword
        val signUpLink = binding.registerLink

        loginButton.setOnClickListener {
            val username = usernameEditText.editText?.text.toString().trim()
            val password = passwordEditText.editText?.text.toString().trim()

            Log.d("LoginActivity", "Login button clicked with username: $username")

            if (validateLogin(username, password)) {
                loginUser(username, password)
            }
        }

        signUpLink.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }
    }

    private fun validateLogin(username: String, password: String): Boolean {
        if (username.isEmpty()) {
            Toast.makeText(this, "Nama pengguna tidak boleh kosong", Toast.LENGTH_SHORT).show()
            return false
        }

        if (password.isEmpty()) {
            Toast.makeText(this, "Kata sandi tidak boleh kosong", Toast.LENGTH_SHORT).show()
            return false
        }

        return true
    }

    private fun loginUser(username: String, password: String) {

        Log.d("LoginActivity", "Logging in user with username: $username, password: $password")

        val apiService = ApiConfig.getApiService()
        val loginRequest = LoginRequest(username, password)

        Log.d("LoginActivity", "Sending login request: $loginRequest")

        apiService.loginUser(loginRequest).enqueue(object : Callback<LoginResponse> {
            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                if (response.isSuccessful) {
                    val loginResponse = response.body()
                    Log.d("LoginActivity", "Login response received: $loginResponse")
                    if (loginResponse?.accessToken != null) {
                            with(sharedPreferences.edit()) {
                            putString("access_token", loginResponse.accessToken)
                            apply()
                        }
                        loginResponse.data?.let { userData ->
                            with(sharedPreferences.edit()) {
                                putString("user_name", userData.nama)
                                putString("user_username", userData.username)
                                apply()
                            }
                        }
                        Toast.makeText(this@LoginActivity, loginResponse.message, Toast.LENGTH_SHORT).show()
                        val intent = Intent(this@LoginActivity, HomeActivity::class.java)
                        startActivity(intent)
                        finish()
                    } else {
                        Toast.makeText(this@LoginActivity, "Login failed: ${loginResponse?.message}", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    val errorBody = response.errorBody()?.string()
                    Log.e("LoginActivity", "Login error response: $errorBody")
                    Toast.makeText(this@LoginActivity, "Login error: $errorBody", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                Log.e("LoginActivity", "Network error: ${t.localizedMessage}", t)
                Toast.makeText(this@LoginActivity, "Network error: ${t.localizedMessage}", Toast.LENGTH_SHORT).show()
            }
        })
    }
}
