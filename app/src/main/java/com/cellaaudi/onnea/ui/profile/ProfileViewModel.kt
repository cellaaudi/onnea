package com.cellaaudi.onnea.ui.profile

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.cellaaudi.onnea.api.MLConfig
import com.cellaaudi.onnea.model.UserResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProfileViewModel : ViewModel() {

    private val _user = MutableLiveData<UserResponse>()
    val user: LiveData<UserResponse> get() = _user

    private val _msg = MutableLiveData<String>()
    val msg: LiveData<String> get() = _msg

    fun getUser(id: String) {
        val client = MLConfig.getApiService().getUser(id)

        client.enqueue(object : Callback<UserResponse> {
            override fun onResponse(call: Call<UserResponse>, response: Response<UserResponse>) {
                if (response.isSuccessful) {
                    _user.value = response.body()
                    Log.e(TAG, "${response.body()}")
                } else {
                    _msg.value = "Retrieve user data failed"
                    Log.e(TAG, "onFailureResponse")
                }
            }

            override fun onFailure(call: Call<UserResponse>, t: Throwable) {
                _msg.value = "Retrieve user data failed"
                Log.e(TAG, "onFailure: ${t.message}")
            }

        })
    }

    companion object {
        private const val TAG = "ProfileViewModel"
    }
}