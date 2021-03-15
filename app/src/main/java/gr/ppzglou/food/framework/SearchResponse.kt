package gr.ppzglou.food.framework

import com.google.gson.annotations.SerializedName


data class SearchResponse(
    @SerializedName("hits") val hits: MutableList<Hits>
)

data class Hits(
    @SerializedName("recipe") val recipe: Recipe?
)

data class Recipe(
    @SerializedName("uri") val uri: String?,
    @SerializedName("label") val label: String?,
    @SerializedName("image") val image: String?,
    @SerializedName("url") val url: String?,
    @SerializedName("shareAs") val shareAs: String?,
    @SerializedName("yield") val yield: Int?,
    @SerializedName("dietLabels") val dietLabels: MutableList<String>?,
    @SerializedName("healthLabels") val healthLabels: MutableList<String>?,
    @SerializedName("ingredientLines") val ingredientLines: MutableList<String>?,
    @SerializedName("ingredients") val ingredients: MutableList<Ingredients>?,
    @SerializedName("calories") val calories: Double?,
    @SerializedName("totalWeight") val totalWeight: Double?,
    @SerializedName("cuisineType") val cuisineType: MutableList<String>?,
    @SerializedName("mealType") val mealType: MutableList<String>?,
    @SerializedName("dishType") val dishType: MutableList<String>?
)

data class Ingredients(
    @SerializedName("text") val text: String?,
    @SerializedName("foodCategory") val foodCategory: String?,
    @SerializedName("foodId") val foodId: String?,
    @SerializedName("image") val image: String?
)

