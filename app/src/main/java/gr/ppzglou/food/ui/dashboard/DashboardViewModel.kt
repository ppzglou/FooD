package gr.ppzglou.food.ui.dashboard

import android.net.ConnectivityManager
import androidx.hilt.lifecycle.ViewModelInject
import gr.ppzglou.food.R
import gr.ppzglou.food.base.BaseViewModel
import gr.ppzglou.food.ui.dashboard.fragments.profile.ProfileFragmentDirections
import gr.ppzglou.food.util.MenuButton
import gr.ppzglou.food.util.connectivity.ConnectivityLiveData

class DashboardViewModel
@ViewModelInject
constructor(
    connectivityLiveData: ConnectivityLiveData,
    connectivityManager: ConnectivityManager
) : BaseViewModel(connectivityLiveData, connectivityManager) {

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
                nav.actionNavProfileToNavSettings()
            ),
            MenuButton(
                "settings",
                R.drawable.alerter_ic_face,
                nav.actionNavProfileToNavSettings()
            )
        )
    }


}


