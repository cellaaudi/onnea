package com.cellaaudi.onnea.model

import com.google.gson.annotations.SerializedName

data class NutritionSpoonacularResponse(

	@field:SerializedName("expires")
	val expires: Long,

	@field:SerializedName("caloricBreakdown")
	val caloricBreakdown: CaloricBreakdown,

	@field:SerializedName("bad")
	val bad: List<BadItem>,

	@field:SerializedName("carbs")
	val carbs: String,

	@field:SerializedName("calories")
	val calories: String,

	@field:SerializedName("good")
	val good: List<GoodItem>,

	@field:SerializedName("nutrients")
	val nutrients: List<NutrientsItem>,

	@field:SerializedName("protein")
	val protein: String,

	@field:SerializedName("weightPerServing")
	val weightPerServing: WeightPerServing,

	@field:SerializedName("fat")
	val fat: String,

	@field:SerializedName("ingredients")
	val ingredients: List<IngredientsItem>,

	@field:SerializedName("flavonoids")
	val flavonoids: List<FlavonoidsItem>,

	@field:SerializedName("isStale")
	val isStale: Boolean,

	@field:SerializedName("properties")
	val properties: List<PropertiesItem>
)

data class CaloricBreakdown(

	@field:SerializedName("percentCarbs")
	val percentCarbs: Any,

	@field:SerializedName("percentProtein")
	val percentProtein: Any,

	@field:SerializedName("percentFat")
	val percentFat: Any
)

data class FlavonoidsItem(

	@field:SerializedName("amount")
	val amount: Any,

	@field:SerializedName("unit")
	val unit: String,

	@field:SerializedName("name")
	val name: String
)

data class GoodItem(

	@field:SerializedName("amount")
	val amount: String,

	@field:SerializedName("percentOfDailyNeeds")
	val percentOfDailyNeeds: Any,

	@field:SerializedName("indented")
	val indented: Boolean,

	@field:SerializedName("title")
	val title: String
)

data class IngredientsItem(

	@field:SerializedName("amount")
	val amount: Any,

	@field:SerializedName("unit")
	val unit: String,

	@field:SerializedName("name")
	val name: String,

	@field:SerializedName("id")
	val id: Int,

	@field:SerializedName("nutrients")
	val nutrients: List<NutrientsItem>
)

data class NutrientsItem(

	@field:SerializedName("amount")
	val amount: Double,

	@field:SerializedName("unit")
	val unit: String,

	@field:SerializedName("percentOfDailyNeeds")
	val percentOfDailyNeeds: Double,

	@field:SerializedName("name")
	val name: String
)

data class BadItem(

	@field:SerializedName("amount")
	val amount: String,

	@field:SerializedName("percentOfDailyNeeds")
	val percentOfDailyNeeds: Any,

	@field:SerializedName("indented")
	val indented: Boolean,

	@field:SerializedName("title")
	val title: String
)

data class WeightPerServing(

	@field:SerializedName("amount")
	val amount: Int,

	@field:SerializedName("unit")
	val unit: String
)

data class PropertiesItem(

	@field:SerializedName("amount")
	val amount: Any,

	@field:SerializedName("unit")
	val unit: String,

	@field:SerializedName("name")
	val name: String
)
