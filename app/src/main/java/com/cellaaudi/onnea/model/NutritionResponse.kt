package com.cellaaudi.onnea.model

import com.google.gson.annotations.SerializedName

data class NutritionResponse(

	@field:SerializedName("Carbohydrates")
	val carbohydrates: String,

	@field:SerializedName("Fat")
	val fat: String,

	@field:SerializedName("Calories")
	val calories: String,

	@field:SerializedName("Protein")
	val protein: String
)
