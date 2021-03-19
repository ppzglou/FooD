package gr.ppzglou.food.ui.splash

import android.content.SharedPreferences
import android.net.ConnectivityManager
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import gr.ppzglou.food.AUTH_IS_VERIFIED
import gr.ppzglou.food.AUTH_UUID
import gr.ppzglou.food.ERROR_PIN_OF_USER_NOT_EXIST
import gr.ppzglou.food.base.BaseViewModel
import gr.ppzglou.food.dao.userpin.UserPinDaoImpl
import gr.ppzglou.food.dao.userpin.UserPinEntity
import gr.ppzglou.food.ext.isNullOrEmptyOrBlank
import gr.ppzglou.food.util.connectivity.ConnectivityLiveData
import gr.ppzglou.food.util.get

class SplashViewModel @ViewModelInject constructor(
    connectivityLiveData: ConnectivityLiveData,
    connectivityManager: ConnectivityManager,
    private val sharedPrefs: SharedPreferences,
    private val userPinDaoImpl: UserPinDaoImpl
) : BaseViewModel(connectivityLiveData, connectivityManager) {

    companion object {
        private const val SPLASH_DELAY = 1000
    }

    private val currentUser: String?
        get() = sharedPrefs[AUTH_UUID, ""]

    private val _navigateToLoginScreen = MutableLiveData<Unit>()
    val navigateToLoginScreen: LiveData<Unit> = _navigateToLoginScreen

    private val _navigateToDashboardScreen = MutableLiveData<Unit>()
    val navigateToAuthenticationScreen: LiveData<Unit> = _navigateToDashboardScreen

    private val _fetchDaoUserPin = MutableLiveData<UserPinEntity>()
    val fetchDaoUserPin: LiveData<UserPinEntity> = _fetchDaoUserPin

    private val _fetchDaoUserPinError = MutableLiveData<Int>()
    val fetchDaoUserPinError: LiveData<Int> = _fetchDaoUserPinError

    fun checkUserExistence() {
        launch(SPLASH_DELAY) {
            var isCleanInstallation = currentUser.isNullOrEmptyOrBlank
            if (!isCleanInstallation) {
                val isVerified = sharedPrefs[AUTH_IS_VERIFIED, ""].toBoolean()
                isCleanInstallation = !isVerified
            }
            if (isCleanInstallation) {
                _navigateToLoginScreen.value = Unit
                return@launch
            }
            _navigateToDashboardScreen.value = Unit

        }
    }

    fun fetchDaoUserPin() {
        launch {
            if (!currentUser.isNullOrEmptyOrBlank) {
                val users = userPinDaoImpl.getAll()

                for (u in users) {
                    if (u.uid == currentUser) {
                        _fetchDaoUserPin.value = u
                        break
                    }
                }
                if (_fetchDaoUserPin.value == null) {
                    _fetchDaoUserPinError.value = ERROR_PIN_OF_USER_NOT_EXIST
                }
            }
        }
    }
}