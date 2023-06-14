package com.cellaaudi.onnea.ui.register

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.cellaaudi.onnea.api.MLConfig
import com.cellaaudi.onnea.model.OnlyBooleanResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegisterViewModel : ViewModel() {

    private val _registerMsg = MutableLiveData<String>()
    val registerMsg: LiveData<String> get() = _registerMsg

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    fun register(id: String) {
        _isLoading.value = true
        val client = MLConfig.getApiService().register(id)

        client.enqueue(object : Callback<OnlyBooleanResponse> {
            override fun onResponse(
                call: Call<OnlyBooleanResponse>,
                response: Response<OnlyBooleanResponse>
            ) {
                _isLoading.value = false

                if (response.isSuccessful) {
                    if (response.body()?.message == true) {
                        _registerMsg.value = "Register success. Please login."
                    } else {
                        _registerMsg.value = "Register failed. Please try again."
                    }
                }
            }

            override fun onFailure(call: Call<OnlyBooleanResponse>, t: Throwable) {
                _isLoading.value = false
                _registerMsg.value = "Register failed. Please try again."
            }
        })
    }

    companion object {
        private const val TAG = "RegisterViewModel"
    }
}