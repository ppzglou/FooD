package gr.ppzglou.food.ext

import gr.ppzglou.food.*


data class BaseException(val code: Int) : Exception()

fun String.getErrorCode(): Int {
    return when (this) {
        "An internal error has occurred. [ Unable to resolve host \"www.googleapis.com\":No address associated with hostname ]" -> ERROR_NO_INTERNET
        "The email address is already in use by another account." -> ERROR_MEMBER_EXIST
        "There is no user record corresponding to this identifier. The user may have been deleted." -> ERROR_NOT_MEMBER_EXIST
        "The password is invalid or the user does not have a password." -> ERROR_WRONG_PASSWORD
        "The user account has been disabled by an administrator." -> ERROR_MEMBER_HAS_DISABLED
        "We have blocked all requests from this device due to unusual activity. Try again later. [ Access to this account has been temporarily disabled due to many failed login attempts. You can immediately restore it by resetting your password or you can try again later. ]" -> ERROR_MEMBER_HAS_BLOCKED
        "We have blocked all requests from this device due to unusual activity. Try again later." -> ERROR_MEMBER_HAS_BLOCKED
        "The sms verification code used to create the phone auth credential is invalid. Please resend the verification code sms and be sure use the verification code provided by the user." -> ERROR_WRONG_OTP_CODE
        "This credential is already associated with a different user account." -> ERROR_PHONE_IS_LINKED_WITH_OTHER_ACCOUNT
        else -> ERROR_GENERAL
    }
}