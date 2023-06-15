package com.cellaaudi.onnea.ui.addfood

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.cellaaudi.onnea.api.MLConfig
import com.cellaaudi.onnea.api.SpoonacularConfig
import com.cellaaudi.onnea.model.DetailFoodResponse
import com.cellaaudi.onnea.model.OnlyBooleanResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.time.Month

class AddFoodDetailViewModel : ViewModel() {

    private val _detail = MutableLiveData<DetailFoodResponse>()
    val detail: LiveData<DetailFoodResponse> = _detail

    private val _msg = MutableLiveData<String>()
    val msg: LiveData<String> = _msg

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
                    Log.e("FoodDetail", "${response.body()}")
                } else {
                    _msg.value = "There was an error retrieving food data."
                    Log.e("FoodDetail", "onFailureResponse")
                }
            }

            override fun onFailure(call: Call<DetailFoodResponse>, t: Throwable) {
                _isLoading.value = false
                _msg.value = "There was an error retrieving food data."
                Log.e("FoodDetail", "onFailure")
            }

        })
    }

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
                }
            }

            override fun onFailure(call: Call<OnlyBooleanResponse>, t: Throwable) {
                _isLoading.value = false
            }
        })
    }
}