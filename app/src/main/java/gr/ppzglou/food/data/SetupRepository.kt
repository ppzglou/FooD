package gr.ppzglou.food.data

import gr.ppzglou.food.data.models.*
import gr.ppzglou.food.framework.Recipe
import gr.ppzglou.food.framework.SearchRequest
import gr.ppzglou.food.framework.SearchResponse
import gr.ppzglou.food.util.ResultWrapper


interface SetupRepository {

    suspend fun searchRecipeRemote(request: SearchRequest): ResultWrapper<SearchResponse>

    suspend fun recipeRemote(request: String): ResultWrapper<MutableList<Recipe>>

    suspend fun currentUserRemote(): ResultWrapper<CurrentUserResponse>

    suspend fun logoutRemote(): ResultWrapper<Boolean>

    suspend fun loginRemote(request: LoginRequest): ResultWrapper<CurrentUserResponse>

    suspend fun registerRemote(request: RegisterRequest): ResultWrapper<CurrentUserResponse>

    suspend fun updateDisplayNameRemote(request: UpdateDisplayNameRequest): ResultWrapper<Boolean>

    suspend fun addUserFirestoreRemote(request: AddUserDbRequest): ResultWrapper<Boolean>

    suspend fun verificationEmailRemote(): ResultWrapper<Boolean>

    suspend fun updatePassRemote(request: UpdatePassRequest): ResultWrapper<Boolean>

    suspend fun resetPassRemote(request: ResetPasswordRequest): ResultWrapper<Boolean>

    suspend fun updateEmailRemote(request: UpdateEmailRequest): ResultWrapper<Boolean>

    suspend fun phoneOtpRemote(request: OtpPhoneRequest): ResultWrapper<Boolean>

    suspend fun updatePhoneCredentialRemote(request: UpdatePhoneCredentialRequest): ResultWrapper<Boolean>

    suspend fun getPhoneRemote(): ResultWrapper<String?>

    suspend fun userProfileRemote(): ResultWrapper<UserProfileResponse>

    suspend fun uploadFileToStorageRemote(request: UploadFileRequest): ResultWrapper<Boolean>


}
