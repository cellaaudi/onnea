package com.cellaaudi.onnea.model

import com.google.gson.annotations.SerializedName

data class FoodRecognitionResponse(

	@field:SerializedName("prediction")
	val prediction: Int
)
