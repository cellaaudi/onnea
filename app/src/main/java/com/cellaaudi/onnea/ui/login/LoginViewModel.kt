package com.cellaaudi.onnea.ui.login

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.cellaaudi.onnea.api.ApiConfig
import com.cellaaudi.onnea.api.MLConfig
import com.cellaaudi.onnea.model.OnlyBooleanResponse
import com.cellaaudi.onnea.model.SearchUserResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginViewModel : ViewModel() {

    private val _loginMsg = MutableLiveData<String>()
    val loginMsg: LiveData<String> get() = _loginMsg

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    fun login(id: String) {
        _isLoading.value = true
        val client = MLConfig.getApiService().login(id)

        client.enqueue(object : Callback<OnlyBooleanResponse> {
            override fun onResponse(
                call: Call<OnlyBooleanResponse>,
                response: Response<OnlyBooleanResponse>
            ) {
                _isLoading.value = false

                if (response.isSuccessful) {
                    if (response.body()?.message == true) {
                        _loginMsg.value = "Login success. Welcome."
                    } else {
                        _loginMsg.value = "Login success. Please answer these questions."
                    }
                } else {
                    Log.e("LoginViewModel", "onFailureResponse: ${response.code()} ${response.message()}")
                }
            }

            override fun onFailure(call: Call<OnlyBooleanResponse>, t: Throwable) {
                _isLoading.value = false
                _loginMsg.value = "Login failed. Please try again."
                Log.e("LoginViewModel", "onFailure: ${t.message}")
            }
        })
    }
}