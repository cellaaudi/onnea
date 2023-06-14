package com.cellaaudi.onnea.model

import com.google.gson.annotations.SerializedName

data class RecommendationResponse(

	@field:SerializedName("Month")
	val month: String,

	@field:SerializedName("Breakfast")
	val breakfast: List<BreakfastItem>,

	@field:SerializedName("Dinner")
	val dinner: List<DinnerItem>,

	@field:SerializedName("Day")
	val day: String,

	@field:SerializedName("Lunch")
	val lunch: List<LunchItem>
)

data class LunchItem(

	@field:SerializedName("Eat")
	val eat: Boolean,

	@field:SerializedName("Carbohydrates")
	val carbohydrates: String,

	@field:SerializedName("Fat")
	val fat: String,

	@field:SerializedName("Protein")
	val protein: String,

	@field:SerializedName("Calories")
	val calories: String,

	@field:SerializedName("Image_Type")
	val imageType: String,

	@field:SerializedName("Link")
	val link: String,

	@field:SerializedName("Name")
	val name: String,

	@field:SerializedName("ID")
	val iD: Int
)

data class BreakfastItem(

	@field:SerializedName("Eat")
	val eat: Boolean,

	@field:SerializedName("Carbohydrates")
	val carbohydrates: String,

	@field:SerializedName("Fat")
	val fat: String,

	@field:SerializedName("Protein")
	val protein: String,

	@field:SerializedName("Calories")
	val calories: String,

	@field:SerializedName("Image_Type")
	val imageType: String,

	@field:SerializedName("Link")
	val link: String,

	@field:SerializedName("Name")
	val name: String,

	@field:SerializedName("ID")
	val iD: Int
)

data class DinnerItem(

	@field:SerializedName("Eat")
	val eat: Boolean,

	@field:SerializedName("Carbohydrates")
	val carbohydrates: String,

	@field:SerializedName("Fat")
	val fat: String,

	@field:SerializedName("Protein")
	val protein: String,

	@field:SerializedName("Calories")
	val calories: String,

	@field:SerializedName("Image_Type")
	val imageType: String,

	@field:SerializedName("Link")
	val link: String,

	@field:SerializedName("Name")
	val name: String,

	@field:SerializedName("ID")
	val iD: Int
)
