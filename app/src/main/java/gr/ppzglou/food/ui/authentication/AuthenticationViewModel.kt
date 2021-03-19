package gr.ppzglou.food.ui.authentication

import android.content.SharedPreferences
import android.net.ConnectivityManager
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import gr.ppzglou.food.*
import gr.ppzglou.food.base.BaseViewModel
import gr.ppzglou.food.dao.userpin.UserPinDaoImpl
import gr.ppzglou.food.ext.isNullOrEmptyOrBlank
import gr.ppzglou.food.usecases.LogoutUserUseCase
import gr.ppzglou.food.util.ResultWrapper
import gr.ppzglou.food.util.connectivity.ConnectivityLiveData
import gr.ppzglou.food.util.get
import gr.ppzglou.food.util.set


class AuthenticationViewModel
@ViewModelInject
constructor(
    connectivityLiveData: ConnectivityLiveData,
    connectivityManager: ConnectivityManager,
    private val sharedPrefs: SharedPreferences,
    private val userPinDaoImpl: UserPinDaoImpl,
    private val logoutUserUseCase: LogoutUserUseCase,
    private val sharedPref: SharedPreferences

) : BaseViewModel(connectivityLiveData, connectivityManager) {

    private val currentUser: String?
        get() = sharedPrefs[AUTH_UUID, ""]

    private val _isFingerprint = MutableLiveData<Boolean>()
    val isFingerprint: LiveData<Boolean> = _isFingerprint

    private val _daoUserPin = MutableLiveData<Boolean>()
    val fetchDaoUserPin: LiveData<Boolean> = _daoUserPin

    private val _pinTime = MutableLiveData<Long>()
    val fetchPinTime: LiveData<Long> = _pinTime

    private val _fetchPinCount = MutableLiveData<Int>()
    val fetchPinCount: LiveData<Int> = _fetchPinCount

    private val _updatePinCount = MutableLiveData<Boolean>()
    val updatePinCount: LiveData<Boolean> = _updatePinCount

    private val _successLogout = MutableLiveData<Boolean>()
    val successLogout: LiveData<Boolean> = _successLogout

    fun isUserFingerprint() {
        launch {
            if (!currentUser.isNullOrEmptyOrBlank) {
                val users = userPinDaoImpl.getAll()

                for (u in users) {
                    if (u.uid == currentUser) {
                        _isFingerprint.value = u.fingerprint
                        break
                    }
                }
                if (_isFingerprint.value == null) {
                    _error.value = ERROR_GENERAL
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

    fun fetchPinTime() {
        var time = 0L
        sharedPref[AUTHENTICATION_PIN_TIME, ""]?.let {
            if (it.isNotEmpty())
                time = it.toLong()
        }
        _pinTime.value = time
    }

    fun addPinTime(time: Long) {
        sharedPref[AUTHENTICATION_PIN_TIME] = time.toString()
    }

    fun fetchCountOfPin() {
        var count = 0
        sharedPref[AUTHENTICATION_PIN_COUNT, ""]?.let {
            if (it.isNotEmpty())
                count = it.toInt()
        }
        _fetchPinCount.value = count
    }

    fun updateCountOfPin(num: Int? = null) {
        val count = sharedPref[AUTHENTICATION_PIN_COUNT, ""]?.toInt()

        if (num == null)
            sharedPref[AUTHENTICATION_PIN_COUNT] = (count!! + 1).toString()
        else
            sharedPref[AUTHENTICATION_PIN_COUNT] = num.toString()

        _updatePinCount.value = true
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


}


