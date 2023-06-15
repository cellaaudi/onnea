package com.cellaaudi.onnea.ui.home

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.cellaaudi.onnea.api.MLConfig
import com.cellaaudi.onnea.model.NutritionResponse
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

    private val _nutrition = MutableLiveData<NutritionResponse>()
    val nutrition: LiveData<NutritionResponse> get() = _nutrition

    private val _nutrMsg = MutableLiveData<String>()
    val nutrMsg: LiveData<String> get() = _nutrMsg

    private val _food = MutableLiveData<RecommendationResponse>()
    val food: LiveData<RecommendationResponse> get() = _food

    private val _recMsg = MutableLiveData<String>()
    val recMsg: LiveData<String> get() = _recMsg

    private val _updateEaten = MutableLiveData<Boolean>()
    val updateEaten: LiveData<Boolean> get() = _updateEaten

    private val _updateMsg = MutableLiveData<String>()
    val updateMsg: LiveData<String> get() = _updateMsg

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> get() = _isLoading

    private val _isLoadingN = MutableLiveData<Boolean>()
    val isLoadingN: LiveData<Boolean> get() = _isLoadingN

    fun setDate(selDate: Date) {
        _date.value = selDate
    }

    fun getNutrition(id: String) {
        _isLoadingN.value = true
        val client = MLConfig.getApiService().getNutrition(id)

        client.enqueue(object : Callback<NutritionResponse> {
            override fun onResponse(
                call: Call<NutritionResponse>,
                response: Response<NutritionResponse>
            ) {
                _isLoadingN.value = false

                if (response.isSuccessful) {
                    _nutrition.value = response.body()
                } else {
                    _nutrMsg.value = "There was an error retrieving nutrition data."
                }
            }

            override fun onFailure(call: Call<NutritionResponse>, t: Throwable) {
                _isLoadingN.value = false
                _nutrMsg.value = "There was an error retrieving nutrition data."
            }
        })
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
        _isLoading.value = true
        val client = MLConfig.getApiService().updateEaten(id, day, month, type)

        client.enqueue(object : Callback<OnlyBooleanResponse> {
            override fun onResponse(
                call: Call<OnlyBooleanResponse>,
                response: Response<OnlyBooleanResponse>
            ) {
                _isLoading.value = false

                if (response.isSuccessful) {
                    if (response.body()?.message == true) {
                        _updateEaten.value = response.body()?.message
                    } else {
                        _updateMsg.value = "Something went wrong. Please try again."
                    }
                } else {
                    _updateMsg.value = "Something went wrong. Please try again."
                }
            }

            override fun onFailure(call: Call<OnlyBooleanResponse>, t: Throwable) {
                _isLoading.value = false
                _updateMsg.value = "Something went wrong. Please try again."
            }
        })
    }

    companion object {
        private const val TAG = "HomeViewModel"
    }
}