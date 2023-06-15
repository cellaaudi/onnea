package com.cellaaudi.onnea.api

import com.cellaaudi.onnea.model.*
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

    @GET("updateeaten")
    fun updateEaten(
        @Query("id") id: String,
        @Query("day") day: String,
        @Query("month") month: String,
        @Query("type") type: String
    ): Call<OnlyBooleanResponse>

    @GET("gettingnutrition")
    fun getNutrition(
        @Query("id") id: String
    ): Call<NutritionResponse>

    @GET("updatefoodday")
    fun updateFood(
        @Query("uid") uid: String,
        @Query("id") id: String,
        @Query("day") day: String,
        @Query("month") month: String,
        @Query("name") name: String,
        @Query("link") link: String,
        @Query("type") type: String,
        @Query("calories") calories: String,
        @Query("imagetype") imagetype: String,
        @Query("protein") protein: String,
        @Query("carbohydrates") carbohydrates: String,
        @Query("fat") fat: String
    ): Call<OnlyBooleanResponse>

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