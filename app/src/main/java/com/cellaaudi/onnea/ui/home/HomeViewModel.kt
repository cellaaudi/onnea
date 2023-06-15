package com.cellaaudi.onnea.ui.home

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.cellaaudi.onnea.api.MLConfig
import com.cellaaudi.onnea.model.OnlyBooleanResponse
import com.cellaaudi.onnea.model.RecommendationResponse
import com.cellaaudi.onnea.ui.profile.ProfileViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

class HomeViewModel : ViewModel() {

    private val _date = MutableLiveData<Date>()
    val date: LiveData<Date> = _date

    private val _food = MutableLiveData<RecommendationResponse>()
    val food: LiveData<RecommendationResponse> = _food

    private val _recMsg = MutableLiveData<String>()
    val recMsg: LiveData<String> = _recMsg

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    fun setDate(selDate: Date) {
        _date.value = selDate
    }

    fun getRecommendation(id :String, day: String, month: String) {
        _isLoading.value = true
        val client = MLConfig.getApiService().getRecommendation(id, day, month)

        client.enqueue(object : Callback<RecommendationResponse> {
            override fun onResponse(
                call: Call<RecommendationResponse>,
                response: Response<RecommendationResponse>
            ) {
                _isLoading.value = false

                if (response.isSuccessful) {
                    _food.value = response.body()
                } else {
                    _recMsg.value = "There was an error retrieving recommendation."
                }
            }

            override fun onFailure(call: Call<RecommendationResponse>, t: Throwable) {
                _isLoading.value = false
                _recMsg.value = "There was an error retrieving recommendation."
            }
        })
    }

    fun updateEat(id: String, day: String, month: String, type: String) {
        val client = MLConfig.getApiService().updateEaten(id, day, month, type)

        client.enqueue(object : Callback<OnlyBooleanResponse> {
            override fun onResponse(
                call: Call<OnlyBooleanResponse>,
                response: Response<OnlyBooleanResponse>
            ) {
                if (response.isSuccessful) {
                    if (response.body()?.message == true) {
                        getRecommendation(id, day, month)
                    }
                }
            }

            override fun onFailure(call: Call<OnlyBooleanResponse>, t: Throwable) {
                Log.e(TAG, "onFailure: ${t.message}")
            }
        })
    }

    companion object {
        private const val TAG = "HomeViewModel"
    }
}