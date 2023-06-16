package com.cellaaudi.onnea.model

import com.google.gson.annotations.SerializedName

data class CateringResponse(

	@field:SerializedName("Address")
	val address: String,

	@field:SerializedName("Price")
	val price: String,

	@field:SerializedName("Phone")
	val phone: String,

	@field:SerializedName("TnC")
	val tnC: String,

	@field:SerializedName("Menu")
	val menu: String,

	@field:SerializedName("Name")
	val name: String
)
