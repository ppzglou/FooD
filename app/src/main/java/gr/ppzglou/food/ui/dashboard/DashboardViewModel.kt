package gr.ppzglou.food.ui.dashboard

import android.content.SharedPreferences
import android.net.ConnectivityManager
import android.net.Uri
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import gr.ppzglou.food.*
import gr.ppzglou.food.base.BaseViewModel
import gr.ppzglou.food.dao.fav.FavDaoImpl
import gr.ppzglou.food.dao.fav.FavEntity
import gr.ppzglou.food.dao.userpin.UserPinDaoImpl
import gr.ppzglou.food.dao.userpin.UserPinEntity
import gr.ppzglou.food.data.models.UpdateEmailRequest
import gr.ppzglou.food.data.models.UpdatePassRequest
import gr.ppzglou.food.data.models.UploadFileRequest
import gr.ppzglou.food.data.models.UserProfileResponse
import gr.ppzglou.food.ext.isNullOrEmptyOrBlank
import gr.ppzglou.food.framework.Hits
import gr.ppzglou.food.framework.Recipe
import gr.ppzglou.food.framework.SearchRequest
import gr.ppzglou.food.ui.dashboard.fragments.profile.ProfileFragmentDirections
import gr.ppzglou.food.usecases.*
import gr.ppzglou.food.util.*
import gr.ppzglou.food.util.connectivity.ConnectivityLiveData

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
    private val userPinDaoImpl: UserPinDaoImpl,
    private val favDaoImpl: FavDaoImpl,
    private val uploadFileUseCase: UploadFileUseCase,
    private val authUseCase: AuthUseCase,
) : BaseViewModel(connectivityLiveData, connectivityManager) {

    private val DELAY = 10
    private var from = 0
    private var to = 10

    private val currentUser: String?
        get() = sharedPrefs[AUTH_UUID, ""]

    private val _successProfile = MutableLiveData<UserProfileResponse>()
    val successProfile: LiveData<UserProfileResponse> = _successProfile

    private val _successSearch = MutableLiveData<MutableList<Hits>>()
    val successSearch: LiveData<MutableList<Hits>> = _successSearch

    private val _successRecipe = MutableLiveData<Recipe>()
    val successRecipe: LiveData<Recipe> = _successRecipe

    private val _successRecipeList = MutableLiveData<MutableList<Hits>>()
    val successRecipeList: LiveData<MutableList<Hits>> = _successRecipeList

    private val _successLogout = MutableLiveData<Boolean>()
    val successLogout: LiveData<Boolean> = _successLogout

    private val _successEmailUpdated = MutableLiveData<Boolean>()
    val successEmailUpdated: LiveData<Boolean> = _successEmailUpdated

    private val _successPassUpdated = MutableLiveData<Boolean>()
    val successPassUpdated: LiveData<Boolean> = _successPassUpdated

    private val _successUploadedFile = SingleLiveEvent<Boolean>()
    val successUploadedFile: LiveData<Boolean> = _successUploadedFile

    private val _fetchFingerprintSettings = MutableLiveData<Boolean>()
    val fetchFingerprintSettings: LiveData<Boolean> = _fetchFingerprintSettings

    private val _themeChoice = MutableLiveData<Event<String>>()
    val themeChoice: LiveData<Event<String>> = _themeChoice

    private val _successAuth = MutableLiveData<Boolean>()
    val successAuth: LiveData<Boolean> = _successAuth

    private val _daoUserPin = MutableLiveData<Boolean>()
    val fetchDaoUserPin: LiveData<Boolean> = _daoUserPin

    private val _successPinChanged = MutableLiveData<Boolean>()
    val successPinChanged: LiveData<Boolean> = _successPinChanged

    private val _daoIsFav = MutableLiveData<Boolean>()
    val fetchDaoIsFav: LiveData<Boolean> = _daoIsFav

    private val _successAddFav = MutableLiveData<Boolean>()
    val successAddFav: LiveData<Boolean> = _successAddFav

    private val _successDelFav = MutableLiveData<Boolean>()
    val successDelFav: LiveData<Boolean> = _successDelFav

    private val _successFetchFavs = MutableLiveData<MutableList<String>>()
    val successFetchFavs: LiveData<MutableList<String>> = _successFetchFavs

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
                R.drawable.ic_heart,
                nav.actionNavProfileToNavFav()
            )
        )
    }

    fun profile() {
        launchSearch(DELAY) {
            when (val response = profileUseCase()) {
                is ResultWrapper.Success -> {
                    _successProfile.value = response.data!!
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
                }
            }
        }
    }

    fun updateSearchData(txt: String) {
        if (!txt.isNullOrEmptyOrBlank) {
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
                        } else
                            _successSearch.value = list!!
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

    fun recipeList(uriList: MutableList<String>) {
        launch(DELAY) {
            val list = mutableListOf<Hits>()
            for (uri in uriList) {
                when (val response = recipeUseCase(uri)) {
                    is ResultWrapper.Success -> {
                        list.add(Hits(response.data[0]))
                    }
                }
            }
            _successRecipeList.value = list
        }
    }


    fun logout() {
        launch {
            when (val response = logoutUserUseCase()) {
                is ResultWrapper.Success -> {
                    sharedPrefs[AUTH_UUID] = null
                    sharedPrefs[AUTH_IS_VERIFIED] = null
                    _successLogout.value = response.data!!
                }
            }
        }
    }

    fun updateEmail(email: String, pass: String) {
        launch(DELAY) {
            when (val response = updateEmailUseCase(UpdateEmailRequest(email, pass))) {
                is ResultWrapper.Success -> {
                    _successEmailUpdated.value = response.data!!
                }
            }
        }
    }

    fun updatePass(oldPass: String, newPass: String) {
        launch(DELAY) {
            when (val response = updatePassUseCase(UpdatePassRequest(oldPass, newPass))) {
                is ResultWrapper.Success -> {
                    _successPassUpdated.value = response.data!!
                }
            }
        }
    }

    fun fetchFingerprintSettings() {
        launch {
            if (!currentUser.isNullOrEmptyOrBlank) {
                val users = userPinDaoImpl.getAll()
                for (u in users) {
                    if (u.uid == currentUser) {
                        _fetchFingerprintSettings.value = u.fingerprint
                    }
                }
                if (_fetchFingerprintSettings.value == null) {
                    _error.value = ERROR_GENERAL
                }
            }
        }
    }


    fun changeFingerprintSettings() {
        launch {
            if (!currentUser.isNullOrEmptyOrBlank) {
                val users = userPinDaoImpl.getAll()
                for (u in users) {
                    if (u.uid == currentUser) {
                        userPinDaoImpl.insert(UserPinEntity(currentUser!!, u.pin, !u.fingerprint))
                    }
                }
            }
        }
    }

    fun changeTheme(choice: String) {
        sharedPrefs[THEME_MODE] = choice
        _themeChoice.value = Event(choice)
    }

    fun getTheme() = sharedPrefs[THEME_MODE, ""]

    fun uploadFile(uri: Uri) {
        launch(DELAY) {
            when (val response =
                uploadFileUseCase(UploadFileRequest(uri))) {
                is ResultWrapper.Success -> {
                    _successUploadedFile.value = response.data!!
                }
            }
        }
    }

    fun auth(pass: String) {
        launch(DELAY) {
            when (val response = authUseCase(pass)) {
                is ResultWrapper.Success -> {
                    _successAuth.value = response.data
                }
            }
        }
    }

    fun fetchDaoUserPin(pin: String) {
        launch {
            if (!currentUser.isNullOrEmptyOrBlank) {
                val users = userPinDaoImpl.getAll()

                for (u in users) {
                    if (u.uid == currentUser && u.pin == pin) {
                        _daoUserPin.value = true
                        break
                    }
                }
                if (_daoUserPin.value == null) {
                    _error.value = ERROR_PIN_OF_USER_IS_WRONG
                }
            }
        }
    }

    fun changePin(pin: String) {
        launch {
            if (!currentUser.isNullOrEmptyOrBlank) {
                val users = userPinDaoImpl.getAll()

                for (u in users) {
                    if (u.uid == currentUser) {
                        userPinDaoImpl.insert(UserPinEntity(u.uid, pin, u.fingerprint))
                        _successPinChanged.value = true
                        break
                    }
                }
                if (_successPinChanged.value == null) {
                    _error.value = ERROR_PIN_OF_USER_IS_WRONG
                }
            }
        }
    }

    fun isFav(uri: String) {
        launch {
            if (!currentUser.isNullOrEmptyOrBlank) {
                val fav = favDaoImpl.findById(uri)
                _daoIsFav.value = (fav != null && fav.uid == currentUser)
            }
        }
    }

    fun addDaoFav(uri: String) {
        launch {
            if (!currentUser.isNullOrEmptyOrBlank) {
                favDaoImpl.insert(FavEntity(uri, currentUser!!)).let {
                    _successAddFav.value = true
                }
            }
        }
    }

    fun deleteDaoFav(uri: String) {
        launch {
            if (!currentUser.isNullOrEmptyOrBlank) {
                val fav = favDaoImpl.findById(uri)
                if (fav.uid == currentUser)
                    favDaoImpl.delete(fav).let {
                        _successDelFav.value = true
                    }
            }
        }
    }

    fun fetchFavsDao() {
        launch {
            val favsCurrentUser = mutableListOf<String>()
            if (!currentUser.isNullOrEmptyOrBlank) {
                val favsList = favDaoImpl.getAll()
                for (fav in favsList) {
                    if (fav.uid == currentUser)
                        favsCurrentUser.add(fav.uri)
                }
            }
            _successFetchFavs.value = favsCurrentUser
        }
    }

}


