package gr.ppzglou.food.util.connectivity

import android.app.Application
import android.net.ConnectivityManager
import androidx.lifecycle.MutableLiveData
import gr.ppzglou.food.util.Event

class ConnectivityLiveData(application: Application, connectivityManager: ConnectivityManager) :
    MutableLiveData<Event<ConnectivityStatus>>() {

    private val connectionMonitor =
        ConnectivityMonitor.getInstance(application.applicationContext, connectivityManager)

    override fun onActive() {
        super.onActive()
        connectionMonitor.startListening(::setConnected)
    }

    override fun onInactive() {
        connectionMonitor.stopListening()
        super.onInactive()
    }

    private fun setConnected(isConnected: Boolean) =
        postValue(
            when {
                isConnected -> Event(ConnectivityStatus.Connected)
                else -> Event(ConnectivityStatus.Disconnected)
            }
        )
}

enum class ConnectivityStatus {
    Connected,
    Disconnected
}