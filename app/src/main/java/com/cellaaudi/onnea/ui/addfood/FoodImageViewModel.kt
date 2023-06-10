package com.cellaaudi.onnea.ui.addfood

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.cellaaudi.onnea.api.ApiConfig
import com.cellaaudi.onnea.api.MLConfig
import com.cellaaudi.onnea.model.FoodNameResponse
import com.cellaaudi.onnea.model.FoodRecognitionResponse
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FoodImageViewModel : ViewModel() {

    private val _prediction = MutableLiveData<Int>()
    val prediction: MutableLiveData<Int> get() = _prediction

    private val _name = MutableLiveData<String>()
    val name: MutableLiveData<String> get() = _name

    fun foodRecognition(file: MultipartBody.Part) {
        val client = MLConfig.getApiService().foodRecognition(file)

        client.enqueue(object : Callback<FoodRecognitionResponse> {
            override fun onResponse(
                call: Call<FoodRecognitionResponse>,
                response: Response<FoodRecognitionResponse>
            ) {
                if (response.isSuccessful) {
                    response.body()?.prediction?.let { foodName(it) }
                }
            }

            override fun onFailure(call: Call<FoodRecognitionResponse>, t: Throwable) {
                Log.e(TAG, "onFailure: ${t.message.toString()}")
            }
        })
    }

    private fun foodName(food_id: Int) {
        val client = ApiConfig.getApiService().getFood(food_id)

        client.enqueue(object : Callback<FoodNameResponse> {
            override fun onResponse(
                call: Call<FoodNameResponse>,
                response: Response<FoodNameResponse>
            ) {
                if (response.isSuccessful) {
                    _name.value = response.body()?.message
                }
            }

            override fun onFailure(call: Call<FoodNameResponse>, t: Throwable) {
                Log.e(TAG, "onFailure Name: ${t.message.toString()}")
            }
        })
    }

    companion object {
        private const val TAG = "FoodImageViewModel"
    }
}