package gr.ppzglou.food.ui.dashboard

import android.content.SharedPreferences
import android.net.ConnectivityManager
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import gr.ppzglou.food.AUTH_IS_VERIFIED
import gr.ppzglou.food.AUTH_UUID
import gr.ppzglou.food.R
import gr.ppzglou.food.base.BaseViewModel
import gr.ppzglou.food.data.models.UpdateEmailRequest
import gr.ppzglou.food.data.models.UpdatePassRequest
import gr.ppzglou.food.data.models.UserProfileResponse
import gr.ppzglou.food.framework.Hits
import gr.ppzglou.food.framework.Recipe
import gr.ppzglou.food.framework.SearchRequest
import gr.ppzglou.food.ui.dashboard.fragments.profile.ProfileFragmentDirections
import gr.ppzglou.food.usecases.*
import gr.ppzglou.food.util.MenuButton
import gr.ppzglou.food.util.ResultWrapper
import gr.ppzglou.food.util.SingleLiveEvent
import gr.ppzglou.food.util.connectivity.ConnectivityLiveData
import gr.ppzglou.food.util.set

class DashboardViewModel
@ViewModelInject
constructor(
    connectivityLiveData: ConnectivityLiveData,
    connectivityManager: ConnectivityManager,
    private val sharedPrefs: SharedPreferences,
    private val logoutUserUseCase: LogoutUserUseCase,
    private val profileUseCase: ProfileUseCase,
    private val updateEmailUseCase: UpdateEmailUseCase,
    private val searchUseCase: SearchUseCase,
    private val recipeUseCase: RecipeUseCase,
    private val updatePassUseCase: UpdatePassUseCase,
    //private val uploadFileUseCase: UploadFileUseCase,
) : BaseViewModel(connectivityLiveData, connectivityManager) {

    private val DELAY = 10
    private var from = 0
    private var to = 10
    private var research = true

    private val _successProfile = MutableLiveData<UserProfileResponse>()
    val successProfile: LiveData<UserProfileResponse> = _successProfile

    private val _successSearch = MutableLiveData<MutableList<Hits>>()
    val successSearch: LiveData<MutableList<Hits>> = _successSearch

    private val _successRecipe = MutableLiveData<Recipe>()
    val successRecipe: LiveData<Recipe> = _successRecipe

    private val _successLogout = MutableLiveData<Boolean>()
    val successLogout: LiveData<Boolean> = _successLogout

    private val _successEmailUpdated = MutableLiveData<Boolean>()
    val successEmailUpdated: LiveData<Boolean> = _successEmailUpdated

    private val _successPassUpdated = MutableLiveData<Boolean>()
    val successPassUpdated: LiveData<Boolean> = _successPassUpdated

    private val _successUploadedFile = SingleLiveEvent<Boolean>()
    val successUploadedFile: LiveData<Boolean> = _successUploadedFile

    fun getMenu(): MutableList<MenuButton> {
        val nav = ProfileFragmentDirections
        return mutableListOf(
            MenuButton(
                "settings",
                R.drawable.ic_settings,
                nav.actionNavProfileToNavSettings()
            ),
            MenuButton(
                "settings",
                R.drawable.ic_recipe,
                nav.actionNavProfileToNavSearch()
            ),
            MenuButton(
                "settings",
                R.drawable.alerter_ic_face,
                nav.actionNavProfileToNavSettings()
            )
        )
    }

    fun profile() {
        launchSearch(DELAY) {
            when (val response = profileUseCase()) {
                is ResultWrapper.Success -> {
                    _successProfile.value = response.data
                }
            }
        }
    }

    fun search(txt: String) {
        launchSearch(DELAY) {
            when (val response = searchUseCase(SearchRequest(txt))) {
                is ResultWrapper.Success -> {
                    _successSearch.value = response.data.hits
                    from = 0
                    to = 10
                    research = true
                }
            }
        }
    }

    fun updateSearchData(txt: String) {
        if (research) {
            launchSearch(DELAY) {
                from += 10
                to += 10
                when (val response = searchUseCase(SearchRequest(txt, from, to))) {
                    is ResultWrapper.Success -> {
                        val list = _successSearch.value
                        list?.addAll(response.data.hits)
                        if (response.data.hits.isNullOrEmpty()) {
                            from += 10
                            to += 10
                            research = false
                        } else
                            _successSearch.value = list
                    }
                }
            }
        }
    }

    fun recipe(uri: String) {
        launch(DELAY) {
            when (val response = recipeUseCase(uri)) {
                is ResultWrapper.Success -> {
                    _successRecipe.value = response.data[0]
                }
            }
        }
    }


    fun logout() {
        launch {
            when (val response = logoutUserUseCase()) {
                is ResultWrapper.Success -> {
                    sharedPrefs[AUTH_UUID] = null
                    sharedPrefs[AUTH_IS_VERIFIED] = null
                    _successLogout.value = response.data
                }
            }
        }
    }

    fun updateEmail(email: String, pass: String) {
        launch(DELAY) {
            when (val response = updateEmailUseCase(UpdateEmailRequest(email, pass))) {
                is ResultWrapper.Success -> {
                    _successEmailUpdated.value = response.data
                }
            }
        }
    }

    fun updatePass(oldPass: String, newPass: String) {
        launch(DELAY) {
            when (val response = updatePassUseCase(UpdatePassRequest(oldPass, newPass))) {
                is ResultWrapper.Success -> {
                    _successPassUpdated.value = response.data
                }
            }
        }
    }

/* fun uploadFile(uri: Uri, name: String) {
     launch(DELAY) {
         when (val response =
             uploadFileUseCase(UploadFileRequest(uri, name, _successGetFiles.value))) {
             is ResultWrapper.Success -> {
                 _successUploadedFile.value = response.data
             }
         }
     }
 }*/


}


