package com.dicoding.semaroam.data.retrofit

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

data class LoginRequest(val username: String, val password: String)
data class SignupRequest(val username: String, val password: String, val nama: String)

data class LoginResponse(
    val message: String,
    val data: UserData?,
    val accessToken: String?
)

data class SignupResponse(
    val message: String,
    val data: UserData
)

data class UserResponse(
    val message: String,
    val data: UserData?
)

data class UserData(
    val id: String,
    val nama: String,
    val username: String,
    val createdAt: String
)

interface AuthService {
    @POST("signin")
    fun loginUser(@Body request: LoginRequest): Call<LoginResponse>

    @POST("signup")
    fun signupUser(@Body request: SignupRequest): Call<SignupResponse>

    @POST("signout")
    fun logoutUser(): Call<Unit>

    @GET("user/{id}")
    fun getUserById(@Path("id") userId: String): Call<UserResponse>


}