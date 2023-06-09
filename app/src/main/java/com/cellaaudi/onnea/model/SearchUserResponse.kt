package com.cellaaudi.onnea.model

import com.google.gson.annotations.SerializedName

data class SearchUserResponse(

	@field:SerializedName("result")
	val result: Boolean,

	@field:SerializedName("message")
	val message: String
)
