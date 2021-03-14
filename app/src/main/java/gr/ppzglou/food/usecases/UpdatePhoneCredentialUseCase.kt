package gr.ppzglou.food.usecases

import dagger.hilt.android.scopes.ActivityScoped
import gr.ppzglou.food.data.models.UpdatePhoneCredentialRequest
import gr.ppzglou.food.domain.SetupDataSource
import javax.inject.Inject

@ActivityScoped
class UpdatePhoneCredentialUseCase @Inject constructor(
    private val dataSource: SetupDataSource
) {
    suspend operator fun invoke(request: UpdatePhoneCredentialRequest) =
        dataSource.updatePhoneCredential(request)
}