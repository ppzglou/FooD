package gr.ppzglou.food.domain


import gr.ppzglou.food.data.models.*
import gr.ppzglou.food.util.ResultWrapper


interface SetupDataSource {

    suspend fun currentUser(): ResultWrapper<CurrentUserResponse>

    suspend fun logout(): ResultWrapper<Boolean>

    suspend fun login(request: LoginRequest): ResultWrapper<CurrentUserResponse>

    suspend fun register(request: RegisterRequest): ResultWrapper<CurrentUserResponse>

    suspend fun updateDisplayName(request: UpdateDisplayNameRequest): ResultWrapper<Boolean>

    suspend fun addUserFirestore(request: AddUserDbRequest): ResultWrapper<Boolean>

    suspend fun verificationEmail(): ResultWrapper<Boolean>

    suspend fun resetPass(request: ResetPasswordRequest): ResultWrapper<Boolean>

    suspend fun updatePass(request: UpdatePassRequest): ResultWrapper<Boolean>

    suspend fun updateEmail(request: UpdateEmailRequest): ResultWrapper<Boolean>

    suspend fun phoneOtp(request: OtpPhoneRequest): ResultWrapper<Boolean>

    suspend fun updatePhoneCredential(request: UpdatePhoneCredentialRequest): ResultWrapper<Boolean>

    suspend fun getPhone(): ResultWrapper<String?>

}