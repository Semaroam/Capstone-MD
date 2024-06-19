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

data class PlaceResponse(
    val message: String,
    val data: List<PlaceData>
)


data class PlaceData(
    val Description: String,
    val Category: String,
    val Place_Id: Int,
    val Place_Ratings: Int,
    val Place_Name: String,
    val City: String,
    val Image: String
)

data class RecommendationResponse(
    val message: String,
    val data_req: PlaceData,
    val total_data_recommendation: Int,
    val data: List<RecommendationData>
)

data class RecommendationData(
    val Description: String,
    val Category: String,
    val Place_Id: String,
    val Place_Ratings: Int,
    val Place_Name: String,
    val City: String,
    val Image: String,
    val Score: Double
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

    @GET("place")
    fun getAllPlaces(): Call<PlaceResponse>

    @GET("place/keyword/{keyword}")
    fun getPlaceByKeyword(@Path("keyword") keyword: String): Call<PlaceResponse>

    @GET("place/category/{category}")
    fun getPlaceByCategory(@Path("category") category: String): Call<PlaceResponse>

    @GET("place/{id}")
    fun getRecommendations(@Path("id") placeId: Int): Call<RecommendationResponse>
}