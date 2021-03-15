package gr.ppzglou.food.data.models

import dagger.hilt.android.scopes.ActivityScoped
import gr.ppzglou.food.domain.SetupDataSource
import javax.inject.Inject

@ActivityScoped
class LogoutUserUseCase @Inject constructor(
    private val dataSource: SetupDataSource
) {
    suspend operator fun invoke() =
        dataSource.logout()
}