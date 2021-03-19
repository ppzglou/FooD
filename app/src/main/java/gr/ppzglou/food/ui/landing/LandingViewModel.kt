package gr.ppzglou.food.ui.landing

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
import gr.ppzglou.food.data.models.*
import gr.ppzglou.food.ext.isNullOrEmptyOrBlank
import gr.ppzglou.food.usecases.*
import gr.ppzglou.food.util.ResultWrapper
import gr.ppzglou.food.util.SingleLiveEvent
import gr.ppzglou.food.util.connectivity.ConnectivityLiveData
import gr.ppzglou.food.util.get
import gr.ppzglou.food.util.set

class LandingViewModel
@ViewModelInject
constructor(
    connectivityLiveData: ConnectivityLiveData,
    connectivityManager: ConnectivityManager,
    private val sharedPrefs: SharedPreferences,
    private val loginUseCase: LoginUseCase,
    private val registerUseCase: RegisterUseCase,
    private val verificationEmailUseCase: VerificationEmailUseCase,
    private val updateDisplayNameUseCase: UpdateDisplayNameUseCase,
    private val addUserFireStoreUserUseCase: AddUserFireStoreUserUseCase,
    private val resetPasswordUseCase: ResetPasswordUseCase,
    private val userPinDaoImpl: UserPinDaoImpl

) : BaseViewModel(connectivityLiveData, connectivityManager) {
    private val DELAY = 10

    private val currentUser: String?
        get() = sharedPrefs[AUTH_UUID, ""]

    private val _successLogin = SingleLiveEvent<Boolean>()
    val successLogin: LiveData<Boolean> = _successLogin

    private val _successRegister = SingleLiveEvent<Boolean>()
    val successRegister: LiveData<Boolean> = _successRegister

    private val _successVerEmail = SingleLiveEvent<Boolean>()
    val successVerEmail: LiveData<Boolean> = _successVerEmail

    private val _successResetPass = SingleLiveEvent<Boolean>()
    val successResetPass: LiveData<Boolean> = _successResetPass

    private val _fetchDaoUserPin = MutableLiveData<UserPinEntity>()
    val fetchDaoUserPin: LiveData<UserPinEntity> = _fetchDaoUserPin

    private val _fetchDaoUserPinError = MutableLiveData<Int>()
    val fetchDaoUserPinError: LiveData<Int> = _fetchDaoUserPinError


    fun login(email: String, password: String) {
        launchLogin(200) {
            when (val response = loginUseCase(LoginRequest(email, password))) {
                is ResultWrapper.Success -> {
                    sharedPrefs[AUTH_UUID] = response.data.uuid
                    sharedPrefs[AUTH_IS_VERIFIED] = response.data.emailVerified.toString()
                    _successLogin.value = true
                }
            }
        }
    }

    fun register(email: String, password: String, fname: String, sname: String) {
        var name = fname.replace("\\s".toRegex(), "") + " "
        name += sname.replace("\\s".toRegex(), "")

        launch(DELAY) {
            when (val response = registerUseCase(RegisterRequest(email, password))) {
                is ResultWrapper.Success -> {
                    updateDisplayName(
                        UpdateDisplayNameRequest(name),
                        AddUserDbRequest(fname, sname)
                    )
                    verificationEmail()
                }
            }
        }
    }

    private fun updateDisplayName(
        updateNameR: UpdateDisplayNameRequest,
        addUserDbR: AddUserDbRequest
    ) {
        launch {
            when (val response = updateDisplayNameUseCase(updateNameR)) {
                is ResultWrapper.Success -> {
                    addUserDB(addUserDbR)
                }
            }
        }
    }

    private fun addUserDB(request: AddUserDbRequest) {
        launch {
            when (val response = addUserFireStoreUserUseCase(request)) {
                is ResultWrapper.Success -> {
                    _successRegister.value = response.data!!
                }
            }
        }
    }

    fun verificationEmail() {
        launch {
            when (val response = verificationEmailUseCase()) {
                is ResultWrapper.Success -> {
                    _successVerEmail.value = response.data!!
                }
            }
        }
    }

    fun resetPass(email: String) {
        launch(DELAY) {
            when (val response = resetPasswordUseCase(ResetPasswordRequest(email))) {
                is ResultWrapper.Success -> {
                    _successResetPass.value = response.data!!
                }
            }
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
