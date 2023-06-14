package com.cellaaudi.onnea.model

import com.google.gson.annotations.SerializedName

data class OnlyBooleanResponse(

	@field:SerializedName("message")
	val message: Boolean
)
