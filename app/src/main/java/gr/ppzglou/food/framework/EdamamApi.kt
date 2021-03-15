package gr.ppzglou.food.framework

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface EdamamApi {

    @GET("search")
    fun getSearchResult(
        @Query("q") searchText: String,
        @Query("from") from: Int? = 0,
        @Query("to") to: Int? = 10
    ): Call<SearchResponse>

    @GET("search")
    fun getRecipeResult(@Query("r") uri: String): Call<MutableList<Recipe>>


}