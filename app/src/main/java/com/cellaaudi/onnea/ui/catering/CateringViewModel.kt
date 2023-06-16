package com.cellaaudi.onnea.ui.catering

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.cellaaudi.onnea.api.MLConfig
import com.cellaaudi.onnea.model.CateringResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CateringViewModel : ViewModel() {

    private val _data = MutableLiveData<CateringResponse>()
    val data: LiveData<CateringResponse> = _data

    private val _msg = MutableLiveData<String>()
    val msg: LiveData<String> = _msg

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> get() = _isLoading

    fun getCatering(id: Int) {
        _isLoading.value = true
        val client = MLConfig.getApiService().getCatering(id)

        client.enqueue(object : Callback<CateringResponse> {
            override fun onResponse(
                call: Call<CateringResponse>,
                response: Response<CateringResponse>
            ) {
                _isLoading.value = false

                if (response.isSuccessful) {
                    _data.value = response.body()
                    Log.e("CateringViewModel", "${response.body()}")
                } else {
                    _msg.value = "Fail to retrieve catering data."
                }
            }

            override fun onFailure(call: Call<CateringResponse>, t: Throwable) {
                _isLoading.value = false
                _msg.value = "Fail to retrieve catering data."
            }
        })
    }
}