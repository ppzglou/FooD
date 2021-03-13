package gr.ppzglou.food.ui.landing

import android.content.SharedPreferences
import android.net.ConnectivityManager
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import gr.ppzglou.food.AUTH_UUID
import gr.ppzglou.food.base.BaseViewModel
import gr.ppzglou.food.util.connectivity.ConnectivityLiveData
import gr.ppzglou.food.util.get


class LandingViewModel
@ViewModelInject
constructor(
    connectivityLiveData: ConnectivityLiveData,
    connectivityManager: ConnectivityManager,
    private val sharedPrefs: SharedPreferences

) : BaseViewModel(connectivityLiveData, connectivityManager) {
    private val DELAY = 10


}


