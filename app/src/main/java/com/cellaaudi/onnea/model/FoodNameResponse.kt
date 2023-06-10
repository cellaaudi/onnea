package com.cellaaudi.onnea.model

import com.google.gson.annotations.SerializedName

data class FoodNameResponse(

	@field:SerializedName("error")
	val error: Boolean,

	@field:SerializedName("message")
	val message: String
)
