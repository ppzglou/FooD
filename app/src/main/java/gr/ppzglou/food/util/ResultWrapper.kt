package gr.ppzglou.food.util


sealed class ResultWrapper<out T> {
    data class Success<out T>(val data: T) : ResultWrapper<T>()
    data class Failure(val error: Int?) : ResultWrapper<Nothing>()
}




