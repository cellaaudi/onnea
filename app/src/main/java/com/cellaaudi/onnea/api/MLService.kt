package com.cellaaudi.onnea.api

import com.cellaaudi.onnea.model.FoodRecognitionResponse
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface MLService {
    @Multipart
    @POST("food")
    fun foodRecognition(
        @Part file: MultipartBody.Part
    ): Call<FoodRecognitionResponse>
}