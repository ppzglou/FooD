package gr.ppzglou.food.framework

data class SearchRequest(
    val txt: String,
    val from: Int? = 0,
    val to: Int? = 10
)