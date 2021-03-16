package gr.ppzglou.food.data

import gr.ppzglou.food.data.models.*
import gr.ppzglou.food.domain.SetupDataSource
import gr.ppzglou.food.framework.Recipe
import gr.ppzglou.food.framework.SearchRequest
import gr.ppzglou.food.framework.SearchResponse
import gr.ppzglou.food.util.ResultWrapper


class SetupDataSourceImpl(
    private val repository: SetupRepository
) : SetupDataSource {

    override suspend fun searchRecipe(request: SearchRequest): ResultWrapper<SearchResponse> =
        repository.searchRecipeRemote(request)

    override suspend fun recipe(request: String): ResultWrapper<MutableList<Recipe>> =
        repository.recipeRemote(request)

    override suspend fun currentUser() =
        repository.currentUserRemote()

    override suspend fun logout() =
        repository.logoutRemote()

    override suspend fun login(request: LoginRequest) =
        repository.loginRemote(request)

    override suspend fun register(request: RegisterRequest) =
        repository.registerRemote(request)

    override suspend fun updateDisplayName(request: UpdateDisplayNameRequest) =
        repository.updateDisplayNameRemote(request)

    override suspend fun addUserFirestore(request: AddUserDbRequest) =
        repository.addUserFirestoreRemote(request)

    override suspend fun verificationEmail() =
        repository.verificationEmailRemote()

    override suspend fun resetPass(request: ResetPasswordRequest) =
        repository.resetPassRemote(request)

    override suspend fun updatePass(request: UpdatePassRequest) =
        repository.updatePassRemote(request)

    override suspend fun updateEmail(request: UpdateEmailRequest) =
        repository.updateEmailRemote(request)

    override suspend fun phoneOtp(request: OtpPhoneRequest): ResultWrapper<Boolean> =
        repository.phoneOtpRemote(request)

    override suspend fun updatePhoneCredential(request: UpdatePhoneCredentialRequest): ResultWrapper<Boolean> =
        repository.updatePhoneCredentialRemote(request)

    override suspend fun getPhone(): ResultWrapper<String?> =
        repository.getPhoneRemote()

    override suspend fun userProfile(): ResultWrapper<UserProfileResponse> =
        repository.userProfileRemote()

    override suspend fun uploadFileToStorage(request: UploadFileRequest): ResultWrapper<Boolean> =
        repository.uploadFileToStorageRemote(request)

}
