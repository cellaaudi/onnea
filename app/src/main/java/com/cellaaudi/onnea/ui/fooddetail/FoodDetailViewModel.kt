package com.cellaaudi.onnea.ui.fooddetail

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.cellaaudi.onnea.api.SpoonacularConfig
import com.cellaaudi.onnea.model.DetailFoodResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FoodDetailViewModel : ViewModel() {

    private val _detail = MutableLiveData<DetailFoodResponse>()
    val detail: LiveData<DetailFoodResponse> = _detail

    private val _msg = MutableLiveData<String>()
    val msg: LiveData<String> = _msg

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
}