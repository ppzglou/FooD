package gr.ppzglou.food.usecases

import dagger.hilt.android.scopes.ActivityScoped
import gr.ppzglou.food.data.models.PersonalDetailsModel
import gr.ppzglou.food.domain.SetupDataSource
import javax.inject.Inject

@ActivityScoped
class SetPersonalDetailsUseCase @Inject constructor(
    private val dataSource: SetupDataSource
) {
    suspend operator fun invoke(request: PersonalDetailsModel) =
        dataSource.setPersonalDetails(request)
}