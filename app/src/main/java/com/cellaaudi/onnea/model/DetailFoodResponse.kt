package com.cellaaudi.onnea.model

import com.google.gson.annotations.SerializedName

data class DetailFoodResponse(

	@field:SerializedName("instructions")
	val instructions: String,

	@field:SerializedName("sustainable")
	val sustainable: Boolean,

	@field:SerializedName("analyzedInstructions")
	val analyzedInstructions: List<Any>,

	@field:SerializedName("glutenFree")
	val glutenFree: Boolean,

	@field:SerializedName("veryPopular")
	val veryPopular: Boolean,

	@field:SerializedName("title")
	val title: String,

	@field:SerializedName("healthScore")
	val healthScore: Any,

	@field:SerializedName("diets")
	val diets: List<Any>,

	@field:SerializedName("readyInMinutes")
	val readyInMinutes: Int,

	@field:SerializedName("sourceUrl")
	val sourceUrl: String,

	@field:SerializedName("creditsText")
	val creditsText: String,

	@field:SerializedName("dairyFree")
	val dairyFree: Boolean,

	@field:SerializedName("servings")
	val servings: Int,

	@field:SerializedName("vegetarian")
	val vegetarian: Boolean,

	@field:SerializedName("whole30")
	val whole30: Boolean,

	@field:SerializedName("id")
	val id: Int,

	@field:SerializedName("imageType")
	val imageType: String,

	@field:SerializedName("summary")
	val summary: String,

	@field:SerializedName("image")
	val image: String,

	@field:SerializedName("veryHealthy")
	val veryHealthy: Boolean,

	@field:SerializedName("vegan")
	val vegan: Boolean,

	@field:SerializedName("cheap")
	val cheap: Boolean,

	@field:SerializedName("dishTypes")
	val dishTypes: List<String>,

	@field:SerializedName("gaps")
	val gaps: String,

	@field:SerializedName("cuisines")
	val cuisines: List<Any>,

	@field:SerializedName("lowFodmap")
	val lowFodmap: Boolean,

	@field:SerializedName("license")
	val license: String,

	@field:SerializedName("weightWatcherSmartPoints")
	val weightWatcherSmartPoints: Int,

	@field:SerializedName("occasions")
	val occasions: List<Any>,

	@field:SerializedName("spoonacularScore")
	val spoonacularScore: Any,

	@field:SerializedName("pricePerServing")
	val pricePerServing: Any,

	@field:SerializedName("sourceName")
	val sourceName: String,

	@field:SerializedName("spoonacularSourceUrl")
	val spoonacularSourceUrl: String,

	@field:SerializedName("ketogenic")
	val ketogenic: Boolean
)
