package com.cellaaudi.onnea.model

import com.google.gson.annotations.SerializedName

data class RegisterResponse(

	@field:SerializedName("result")
	val result: Boolean,

	@field:SerializedName("message")
	val message: String
)
