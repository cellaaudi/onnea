package com.cellaaudi.onnea.ui.register

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.cellaaudi.onnea.api.ApiConfig
import com.cellaaudi.onnea.model.RegisterResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegisterViewModel : ViewModel() {

    private val _registerMsg = MutableLiveData<String>()
    val registerMsg: LiveData<String> get() = _registerMsg

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    fun register(email: RequestBody, password: RequestBody, name: RequestBody, photo: MultipartBody.Part, timestamp: RequestBody) {
        _isLoading.value = true
        val client = ApiConfig.getApiService().register(email, password, name, photo, timestamp)

        client.enqueue(object : Callback<RegisterResponse> {
            override fun onResponse(
                call: Call<RegisterResponse>,
                response: Response<RegisterResponse>
            ) {
                _isLoading.value = false

                if (response.isSuccessful) {
                    _registerMsg.postValue(response.body()?.message)
                }
            }

            override fun onFailure(call: Call<RegisterResponse>, t: Throwable) {
                _isLoading.value = false
                Log.e(TAG, "onFailure: ${t.message.toString()}")
            }

        })
    }

    companion object {
        private const val TAG = "RegisterViewModel"
    }
}