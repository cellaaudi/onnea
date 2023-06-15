package com.cellaaudi.onnea.api

import com.cellaaudi.onnea.model.DetailFoodResponse
import com.cellaaudi.onnea.model.SearchFoodResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface SpoonacularService {

    @GET("recipes/complexSearch")
    fun searchFood(
        @Query("apiKey") apiKey: String = API_KEY,
        @Query("query") query: String,
        @Query("number") number: Int = 50
    ): Call<SearchFoodResponse>

    @GET("recipes/{id}/information?includeNutrition=false")
    fun getFoodDetail(
        @Path("id") id: Int,
        @Query("apiKey") apiKey: String = API_KEY
    ): Call<DetailFoodResponse>

    companion object {
        private const val API_KEY = "6e93a79ca3e14908b5ec900e92f7d2c1"
    }
}