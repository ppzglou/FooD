package gr.ppzglou.food.data.models

import com.google.firebase.auth.PhoneAuthProvider

data class OtpPhoneRequest(
    val phone: String?,
    val callbacks: PhoneAuthProvider.OnVerificationStateChangedCallbacks?
)