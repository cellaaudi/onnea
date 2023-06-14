package com.cellaaudi.onnea.ui.questions

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cellaaudi.onnea.api.MLConfig
import com.cellaaudi.onnea.model.OnlyBooleanResponse
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.Exception

class QuestionsViewModel : ViewModel() {

    private val _response = MutableLiveData<String>()
    val response: LiveData<String> get() = _response

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    fun insertQuestions(
        id: String,
        name: String,
        age: String,
        gender: String,
        weight: String,
        height: String,
        activity: String,
        goals: String,
        fruit: String,
        healthy: String
    ) {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                val client = MLConfig.getApiService().insertQuestion(
                    id = id,
                    activity = activity,
                    age = age,
                    fruit = fruit,
                    healthy = healthy,
                    height = height,
                    weight = weight,
                    name = name,
                    gender = gender,
                    goals = goals
                )

                client.enqueue(object : Callback<OnlyBooleanResponse> {
                    override fun onResponse(
                        call: Call<OnlyBooleanResponse>,
                        response: Response<OnlyBooleanResponse>
                    ) {
                        _isLoading.value = false

                        if (response.isSuccessful) {
                            if (response.body()?.message == true) {
                                _response.value = "Welcome."
                            } else {
                                _response.value = "Something went wrong. Please try again."
                            }
                        }
                    }

                    override fun onFailure(call: Call<OnlyBooleanResponse>, t: Throwable) {
                        _isLoading.value = false
                        _response.value = "Something went wrong. Please try again."
                    }
                })
            } catch (e: Exception) {
                // Do nothing
            }
        }
    }
}