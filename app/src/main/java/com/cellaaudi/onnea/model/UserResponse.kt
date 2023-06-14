package com.cellaaudi.onnea.model

import com.google.gson.annotations.SerializedName

data class UserResponse(

	@field:SerializedName("Carbohydrates")
	val carbohydrates: String,

	@field:SerializedName("Bitterness")
	val bitterness: String,

	@field:SerializedName("Spiciness")
	val spiciness: String,

	@field:SerializedName("Sourness")
	val sourness: String,

	@field:SerializedName("Sweetness")
	val sweetness: String,

	@field:SerializedName("Savoriness")
	val savoriness: String,

	@field:SerializedName("Gender")
	val gender: String,

	@field:SerializedName("Fattiness")
	val fattiness: String,

	@field:SerializedName("Calories")
	val calories: String,

	@field:SerializedName("Protein")
	val protein: String,

	@field:SerializedName("Weight")
	val weight: String,

	@field:SerializedName("Name")
	val name: String,

	@field:SerializedName("Fruit_Level")
	val fruitLevel: String,

	@field:SerializedName("Fat")
	val fat: String,

	@field:SerializedName("Healthy_Food_Level")
	val healthyFoodLevel: String,

	@field:SerializedName("Height")
	val height: String,

	@field:SerializedName("Saltiness")
	val saltiness: String,

	@field:SerializedName("ID")
	val iD: String,

	@field:SerializedName("Goals")
	val goals: String,

	@field:SerializedName("Activity_Level")
	val activityLevel: String,

	@field:SerializedName("Age")
	val age: String
)
