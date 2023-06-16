package com.cellaaudi.onnea.ui.addfood

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.cellaaudi.onnea.api.MLConfig
import com.cellaaudi.onnea.api.SpoonacularConfig
import com.cellaaudi.onnea.model.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.time.Month

class AddFoodDetailViewModel : ViewModel() {

    private val _detail = MutableLiveData<DetailFoodResponse>()
    val detail: LiveData<DetailFoodResponse> = _detail

    private val _nutrition = MutableLiveData<NutritionSpoonacularResponse>()
    val nutrition: LiveData<NutritionSpoonacularResponse> = _nutrition

//    private val _ingredients = MutableLiveData<IngredientsResponse>()
//    val ingredients: LiveData<IngredientsResponse> = _ingredients

    private val _msg = MutableLiveData<String>()
    val msg: LiveData<String> = _msg

    private val _msgNut = MutableLiveData<String>()
    val msgNut: LiveData<String> = _msgNut

    private val _add = MutableLiveData<Boolean>()
    val add: LiveData<Boolean> get() = _add

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    fun getDetail(id: Int) {
        _isLoading.value = true
        val client = SpoonacularConfig.getApiService().getFoodDetail(id = id)

        client.enqueue(object : Callback<DetailFoodResponse> {
            override fun onResponse(
                call: Call<DetailFoodResponse>,
                response: Response<DetailFoodResponse>
            ) {
                _isLoading.value = false

                if (response.isSuccessful) {
                    _detail.value = response.body()
                } else {
                    _msg.value = "There was an error retrieving food data."
                }
            }

            override fun onFailure(call: Call<DetailFoodResponse>, t: Throwable) {
                _isLoading.value = false
                _msg.value = "There was an error retrieving food data."
            }
        })
    }

    fun getNutrition(foodId: Int) {
        _isLoading.value = true
        val client = SpoonacularConfig.getApiService().getNutrition(id = foodId)

        client.enqueue(object : Callback<NutritionSpoonacularResponse> {
            override fun onResponse(
                call: Call<NutritionSpoonacularResponse>,
                response: Response<NutritionSpoonacularResponse>
            ) {
                _isLoading.value = false

                if (response.isSuccessful) {
                    _nutrition.value = response.body()
                    Log.e("Nutrition", "${response.body()} -- URL: ${call.request().url}")
                } else {
                    _msgNut.value = "There was an error retrieving nutrition data."
                }
            }

            override fun onFailure(call: Call<NutritionSpoonacularResponse>, t: Throwable) {
                _isLoading.value = false
                _msgNut.value = "There was an error retrieving nutrition data."
            }
        })
    }

//    fun getIngredients(id: Int) {
//        _isLoading.value = true
//        val client = SpoonacularConfig.getApiService().getIngredients(id)
//
//        client.enqueue(object : Callback<IngredientsResponse> {
//            override fun onResponse(
//                call: Call<IngredientsResponse>,
//                response: Response<IngredientsResponse>
//            ) {
//                _isLoading.value = false
//
//                if (response.isSuccessful) {
//                    _ingredients.value = response.body()
//                }
//            }
//
//            override fun onFailure(call: Call<IngredientsResponse>, t: Throwable) {
//                _isLoading.value = false
//            }
//        })
//    }

    fun changeFood(
        uid: String,
        id: String,
        day: String,
        month: String,
        name: String,
        link: String,
        type: String,
        calories: String,
        imagetype: String,
        protein: String,
        carbohydrates: String,
        fat: String
    ) {
        _isLoading.value = true
        val client = MLConfig.getApiService().updateFood(
            uid, id, day, month, name, link, type, calories, imagetype, protein, carbohydrates, fat
        )

        client.enqueue(object : Callback<OnlyBooleanResponse> {
            override fun onResponse(
                call: Call<OnlyBooleanResponse>,
                response: Response<OnlyBooleanResponse>
            ) {
                _isLoading.value = false

                if (response.isSuccessful) {
                    _add.value = response.body()?.message
                    Log.e("addfood", "${response.body()} -- URL: ${call.request().url}")
                } else {
                    Log.e("addfood", "fail ${response.body()}")
                }
            }

            override fun onFailure(call: Call<OnlyBooleanResponse>, t: Throwable) {
                _isLoading.value = false
                Log.e("addfood", "failure ${t.message}")
            }
        })
    }
}