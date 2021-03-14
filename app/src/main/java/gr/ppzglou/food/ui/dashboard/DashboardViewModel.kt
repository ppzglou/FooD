package gr.ppzglou.food.ui.dashboard

import android.net.ConnectivityManager
import androidx.hilt.lifecycle.ViewModelInject
import gr.ppzglou.food.base.BaseViewModel
import gr.ppzglou.food.util.connectivity.ConnectivityLiveData

class DashboardViewModel
@ViewModelInject
constructor(
    connectivityLiveData: ConnectivityLiveData,
    connectivityManager: ConnectivityManager
) : BaseViewModel(connectivityLiveData, connectivityManager) {


}


