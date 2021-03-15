package gr.ppzglou.food.usecases

import dagger.hilt.android.scopes.ActivityScoped
import gr.ppzglou.food.domain.SetupDataSource
import gr.ppzglou.food.framework.SearchRequest
import javax.inject.Inject

@ActivityScoped
class SearchUseCase @Inject constructor(
    private val dataSource: SetupDataSource
) {
    suspend operator fun invoke(request: SearchRequest) =
        dataSource.searchRecipe(request)
}