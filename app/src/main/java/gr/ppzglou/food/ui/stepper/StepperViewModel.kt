package gr.ppzglou.food.ui.stepper

import android.content.SharedPreferences
import android.net.ConnectivityManager
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.PhoneAuthProvider
import gr.ppzglou.food.AUTH_UUID
import gr.ppzglou.food.base.BaseViewModel
import gr.ppzglou.food.data.models.OtpPhoneRequest
import gr.ppzglou.food.data.models.PersonalDetailsModel
import gr.ppzglou.food.data.models.UpdatePhoneCredentialRequest
import gr.ppzglou.food.usecases.*
import gr.ppzglou.food.util.ResultWrapper
import gr.ppzglou.food.util.SingleLiveEvent
import gr.ppzglou.food.util.connectivity.ConnectivityLiveData
import gr.ppzglou.food.util.get


class StepperViewModel @ViewModelInject constructor(
    connectivityLiveData: ConnectivityLiveData,
    connectivityManager: ConnectivityManager,
    private val sharedPrefs: SharedPreferences,
    private val phoneOtpUseCase: PhoneOtpUseCase,
    private val updatePhoneCredentialUseCase: UpdatePhoneCredentialUseCase,
    private val getPhoneUseCase: GetPhoneUseCase,
    private val getPersonalDetailsUseCase: GetPersonalDetailsUseCase,
    private val setPersonalDetailsUseCase: SetPersonalDetailsUseCase
) : BaseViewModel(connectivityLiveData, connectivityManager) {

    companion object {
        private const val DELAY = 10
    }

    private val currentUser: String?
        get() = sharedPrefs[AUTH_UUID, ""]

    private val _successPhoneOtp = MutableLiveData<Boolean>()
    val successPhoneOtp: LiveData<Boolean> = _successPhoneOtp

    private val _successPhoneCredentials = SingleLiveEvent<Boolean>()
    val successPhoneCredentials: LiveData<Boolean> = _successPhoneCredentials

    private val _successGetPhone = MutableLiveData<String?>()
    val successGetPhone: LiveData<String?> = _successGetPhone

    private val _successGetName = SingleLiveEvent<PersonalDetailsModel>()
    val successGetName: LiveData<PersonalDetailsModel> = _successGetName

    private val _successSetName = SingleLiveEvent<Boolean>()
    val successSetName: LiveData<Boolean> = _successSetName

    fun phoneOtp(
        phone: String,
        callbacks: PhoneAuthProvider.OnVerificationStateChangedCallbacks
    ) {
        launch(DELAY) {
            when (val response = phoneOtpUseCase(OtpPhoneRequest(phone, callbacks))) {
                is ResultWrapper.Success -> {
                    _successPhoneOtp.value = response.data
                }
            }
        }
    }

    fun updatePhoneCredentials(code: String, verificationId: String) {
        launch(DELAY) {
            when (val response =
                updatePhoneCredentialUseCase(UpdatePhoneCredentialRequest(code, verificationId))) {
                is ResultWrapper.Success -> {
                    _successPhoneCredentials.value = response.data
                }
            }
        }
    }

    fun getPhone() {
        launch(DELAY) {
            when (val response = getPhoneUseCase()) {
                is ResultWrapper.Success -> {
                    _successGetPhone.value = response.data
                }
            }
        }
    }

    fun getName() {
        launch(DELAY) {
            when (val response = getPersonalDetailsUseCase()) {
                is ResultWrapper.Success -> {
                    _successGetName.value = response.data
                }
            }
        }
    }

    fun setName(name: String, sname: String) {
        launch(DELAY) {
            when (val response = setPersonalDetailsUseCase(
                PersonalDetailsModel(
                    name,
                    sname
                )
            )) {
                is ResultWrapper.Success -> {
                    _successSetName.value = response.data
                }
            }
        }
    }
}