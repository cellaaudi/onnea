package com.cellaaudi.onnea.ui.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.cellaaudi.onnea.api.ApiConfig
import com.cellaaudi.onnea.model.SearchUserResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginViewModel : ViewModel() {

    private val _doesUserExist = MutableLiveData<Boolean>()
    val doesUserExist: LiveData<Boolean> get() = _doesUserExist

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    fun checkUser(email: String) {
        _isLoading.value = true
        val client = ApiConfig.getApiService().searchUser(email)

        client.enqueue(object : Callback<SearchUserResponse> {
            override fun onResponse(
                call: Call<SearchUserResponse>,
                response: Response<SearchUserResponse>
            ) {
                _isLoading.value = false

                if (response.isSuccessful) {
                    response.body()?.let { _doesUserExist.value = response.body()?.result }
                }
            }

            override fun onFailure(call: Call<SearchUserResponse>, t: Throwable) {
                TODO("Not yet implemented")
            }

        })
    }
}