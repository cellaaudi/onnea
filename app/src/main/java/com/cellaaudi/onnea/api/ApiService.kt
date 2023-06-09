package com.cellaaudi.onnea.api

import com.cellaaudi.onnea.model.RegisterResponse
import com.cellaaudi.onnea.model.SearchUserResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*

interface ApiService {

    @Multipart
    @POST("register.php")
    fun register(
        @Part("email") email: RequestBody,
        @Part("password") password: RequestBody,
        @Part("name") name: RequestBody,
        @Part photo: MultipartBody.Part,
        @Part("timestamp") timestamp: RequestBody
    ): Call<RegisterResponse>

    @POST("searchuser.php")
    fun searchUser(
        @Field("email") email: String
    ): Call<SearchUserResponse>

//    @Multipart
//    @POST("foodRecognition")
//    fun addFood(
//        @Part file: MultipartBody.Part
//    ): Call<>
}