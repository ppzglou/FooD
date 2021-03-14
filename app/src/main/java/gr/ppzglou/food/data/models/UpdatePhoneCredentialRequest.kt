package gr.ppzglou.food.data.models


data class UpdatePhoneCredentialRequest(
    val code: String?,
    val verificationId: String?
)