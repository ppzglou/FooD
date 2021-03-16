package gr.ppzglou.food.usecases

import dagger.hilt.android.scopes.ActivityScoped
import gr.ppzglou.food.domain.SetupDataSource
import javax.inject.Inject

@ActivityScoped
class AuthUseCase @Inject constructor(
    private val dataSource: SetupDataSource
) {
    suspend operator fun invoke(pass: String) =
        dataSource.auth(pass)
}