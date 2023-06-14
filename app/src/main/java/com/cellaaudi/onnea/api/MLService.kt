package com.cellaaudi.onnea.api

import com.cellaaudi.onnea.model.FoodRecognitionResponse
import com.cellaaudi.onnea.model.OnlyBooleanResponse
import com.cellaaudi.onnea.model.RecommendationResponse
import com.cellaaudi.onnea.model.UserResponse
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.http.*

interface MLService {

    @GET("regisuid")
    fun register(
        @Query("id") id: String
    ): Call<OnlyBooleanResponse>

    @GET("getquestionanswer")
    fun login(
        @Query("id") id: String
    ): Call<OnlyBooleanResponse>

    @GET("updatequestion")
    fun insertQuestion(
        @Query("id") id: String,
        @Query("activity") activity: String,
        @Query("age") age: String,
        @Query("fruit") fruit: String,
        @Query("healthy") healthy: String,
        @Query("height") height: String,
        @Query("weight") weight: String,
        @Query("name") name: String,
        @Query("gender") gender: String,
        @Query("goals") goals: String,
    ): Call<OnlyBooleanResponse>

    @GET("recommendation")
    fun getRecommendation(
        @Query("id") id: String,
        @Query("day") day: String,
        @Query("month") month: String
    ): Call<RecommendationResponse>

    @GET("getuserdata")
    fun getUser(
        @Query("id") id: String
    ): Call<UserResponse>

    @Multipart
    @POST("food")
    fun foodRecognition(
        @Part file: MultipartBody.Part
    ): Call<FoodRecognitionResponse>
}