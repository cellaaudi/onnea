package com.cellaaudi.onnea.model

import com.google.gson.annotations.SerializedName

data class SearchFoodResponse(

	@field:SerializedName("number")
	val number: Int,

	@field:SerializedName("totalResults")
	val totalResults: Int,

	@field:SerializedName("offset")
	val offset: Int,

	@field:SerializedName("results")
	val results: List<ResultsItem>
)

data class ResultsItem(

	@field:SerializedName("image")
	val image: String,

	@field:SerializedName("id")
	val id: Int,

	@field:SerializedName("title")
	val title: String,

	@field:SerializedName("imageType")
	val imageType: String
)
