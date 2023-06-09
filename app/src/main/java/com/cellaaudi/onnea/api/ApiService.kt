package com.cellaaudi.onnea.api

import com.cellaaudi.onnea.model.FoodNameResponse
import com.cellaaudi.onnea.model.OnlyBooleanResponse
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
    ): Call<OnlyBooleanResponse>

    @POST("searchuser.php")
    fun searchUser(
        @Field("email") email: String
    ): Call<SearchUserResponse>

    @FormUrlEncoded
    @POST("foodname.php")
    fun getFood(
        @Field("food_id") food_id: Int
    ): Call<FoodNameResponse>
}