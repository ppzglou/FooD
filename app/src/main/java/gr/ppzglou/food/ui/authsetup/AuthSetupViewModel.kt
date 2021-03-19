package gr.ppzglou.food.ui.authsetup

import android.content.SharedPreferences
import android.net.ConnectivityManager
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import gr.ppzglou.food.AUTHENTICATION_PIN_COUNT
import gr.ppzglou.food.AUTH_UUID
import gr.ppzglou.food.ERROR_GENERAL
import gr.ppzglou.food.base.BaseViewModel
import gr.ppzglou.food.dao.userpin.UserPinDaoImpl
import gr.ppzglou.food.dao.userpin.UserPinEntity
import gr.ppzglou.food.ext.isNullOrEmptyOrBlank
import gr.ppzglou.food.util.connectivity.ConnectivityLiveData
import gr.ppzglou.food.util.get
import gr.ppzglou.food.util.set


class AuthSetupViewModel @ViewModelInject constructor(
    connectivityLiveData: ConnectivityLiveData,
    connectivityManager: ConnectivityManager,
    private val sharedPref: SharedPreferences,
    private val userPinDaoImpl: UserPinDaoImpl
) : BaseViewModel(connectivityLiveData, connectivityManager) {

    private val currentUser: String?
        get() = sharedPref[AUTH_UUID, ""]

    private val _successAddDaoUserPin = MutableLiveData<Boolean>()
    val successAddDaoUserPin: LiveData<Boolean> = _successAddDaoUserPin

    private val _addFingerprintSettings = MutableLiveData<Boolean>()
    val addFingerprintSettings: LiveData<Boolean> = _addFingerprintSettings

    fun addDaoUserPin(pin: String) {
        launch {
            if (!currentUser.isNullOrEmptyOrBlank) {
                userPinDaoImpl.insert(UserPinEntity(currentUser!!, pin)).let {
                    sharedPref[AUTHENTICATION_PIN_COUNT] = "0"
                    _successAddDaoUserPin.value = true
                }
            }
        }
    }

    fun loader() {
        launch(300) { }
    }

    fun addFingerprintSettings() {
        launch {
            if (!currentUser.isNullOrEmptyOrBlank) {
                val users = userPinDaoImpl.getAll()
                for (u in users) {
                    if (u.uid == currentUser) {
                        userPinDaoImpl.insert(UserPinEntity(currentUser!!, u.pin, true)).let {
                            _addFingerprintSettings.value = true
                        }
                    }
                }
                if (_addFingerprintSettings.value == null) {
                    _error.value = ERROR_GENERAL
                }
            }
        }
    }
}