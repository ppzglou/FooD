package gr.ppzglou.food.usecases

import dagger.hilt.android.scopes.ActivityScoped
import gr.ppzglou.food.domain.SetupDataSource
import javax.inject.Inject

@ActivityScoped
class RecipeUseCase @Inject constructor(
    private val dataSource: SetupDataSource
) {
    suspend operator fun invoke(request: String) =
        dataSource.recipe(request)
}