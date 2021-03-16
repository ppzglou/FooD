package gr.ppzglou.food.data.models


data class CurrentUserResponse(
    val uuid: String,
    val emailVerified: Boolean = false
)