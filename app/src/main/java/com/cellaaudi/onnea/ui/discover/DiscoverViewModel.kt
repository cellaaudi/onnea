package com.cellaaudi.onnea.ui.discover

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.cellaaudi.onnea.api.SpoonacularConfig
import com.cellaaudi.onnea.model.SearchFoodResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DiscoverViewModel: ViewModel() {

    private val _load = MutableLiveData<Boolean>()
    val load: LiveData<Boolean> = _load

    private val _search = MutableLiveData<SearchFoodResponse>()
    val search: LiveData<SearchFoodResponse> = _search

    fun search(query: String) {
        _load.value = true
        val client = SpoonacularConfig.getApiService().searchFood(query = query)
        client.enqueue(object : Callback<SearchFoodResponse> {
            override fun onResponse(
                call: Call<SearchFoodResponse>,
                response: Response<SearchFoodResponse>
            ) {
                _load.value = false

                if (response.isSuccessful) {
                    response.body()?.let { _search.value = response.body() }
                } else {
                    Log.e("DiscoverViewModel", "onFailureResponse: ${response.code()} ${response.message()} ${response.raw().request.url.toString()}")
                }
            }

            override fun onFailure(call: Call<SearchFoodResponse>, t: Throwable) {
                _load.value = false
                Log.e("DiscoverViewModel", "onFailure: ${t.message.toString()}")
            }
        })
    }
}