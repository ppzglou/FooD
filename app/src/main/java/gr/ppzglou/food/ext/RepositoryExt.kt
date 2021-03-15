package gr.ppzglou.food.ext

import gr.ppzglou.food.ERROR_GENERAL
import gr.ppzglou.food.util.ResultWrapper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Response

suspend fun <T : Any> networkCall(
    function: suspend () -> T
): T {
    return withContext(Dispatchers.IO) {
        function.invoke()
    }
}


fun <R : Any> Response<R>.handleApiFormat(): ResultWrapper.Success<R> {
    val body = body()
    return if (isSuccessful && body != null) {
        ResultWrapper.Success(body)
    } else {
        throw BaseException(ERROR_GENERAL)
    }
}


