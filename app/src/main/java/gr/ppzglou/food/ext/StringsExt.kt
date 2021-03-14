package gr.ppzglou.food.ext

fun String?.handleNullValue(): String? {
    return if (this == "null") {
        null
    } else {
        this
    }
}

val String?.isNullOrEmptyOrNullString: Boolean
    get() = isNullOrEmpty() || this == "null"


val String?.isNullOrEmptyOrBlank: Boolean
    get() = isNullOrEmpty() || this!!.isBlank()