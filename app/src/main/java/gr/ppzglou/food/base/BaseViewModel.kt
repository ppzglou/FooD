package gr.ppzglou.food.base

import android.net.ConnectivityManager
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import gr.ppzglou.food.ext.BaseException
import gr.ppzglou.food.ext.getErrorCode
import gr.ppzglou.food.ext.isNetworkConnected
import gr.ppzglou.food.util.Event
import gr.ppzglou.food.util.connectivity.ConnectivityLiveData
import gr.ppzglou.food.util.connectivity.ConnectivityStatus
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

abstract class BaseViewModel(
    val connectivityLiveData: ConnectivityLiveData,
    private val connectivityManager: ConnectivityManager
) : ViewModel() {

    private val _load = MutableLiveData<Event<Boolean>>()
    val load: LiveData<Event<Boolean>> = _load

    private val _loadLogin = MutableLiveData<Event<Boolean>>()
    val loadLogin: LiveData<Event<Boolean>> = _loadLogin

    private val _loadSearch = MutableLiveData<Event<Boolean>>()
    val loadSearch: LiveData<Event<Boolean>> = _loadSearch

    val _error = MutableLiveData<Int>()
    val error: LiveData<Int> = _error

    private val _connectivityUI = MutableLiveData<Event<ConnectivityStatus>>()
    val connectivityUI: LiveData<Event<ConnectivityStatus>> = _connectivityUI

    fun launch(function: suspend () -> Unit) {
        viewModelScope.launch {
            try {
                function.invoke()
            } catch (e: Exception) {
                if (e.message == null)
                    _error.value = (e as BaseException).code
                else
                    _error.value = e.message?.getErrorCode()!!
            }
        }
    }


    fun launch(delayTime: Int = 0, function: suspend () -> Unit) {
        viewModelScope.launch {
            _load.value = Event(true)
            delay(delayTime.toLong())
            try {
                function.invoke()
            } catch (e: Exception) {
                if (e.message == null)
                    _error.value = (e as BaseException).code
                else
                    _error.value = e.message?.getErrorCode()!!
            }
        }.invokeOnCompletion {
            _load.value = Event(false)
        }
    }

    fun launchLogin(delayTime: Int = 0, function: suspend () -> Unit) {
        viewModelScope.launch {
            _loadLogin.value = Event(true)
            delay(delayTime.toLong())
            try {
                function.invoke()
            } catch (e: Exception) {
                if (e.message == null)
                    _error.value = (e as BaseException).code
                else
                    _error.value = e.message?.getErrorCode()!!
            }
        }.invokeOnCompletion {
            _loadLogin.value = Event(false)
        }
    }

    fun launchSearch(delayTime: Int = 0, function: suspend () -> Unit) {
        viewModelScope.launch {
            _loadSearch.value = Event(true)
            delay(delayTime.toLong())
            try {
                function.invoke()
            } catch (e: Exception) {
                if (e.message == null)
                    _error.value = (e as BaseException).code
                else
                    _error.value = e.message?.getErrorCode()!!
            }
        }.invokeOnCompletion {
            _loadSearch.value = Event(false)
        }
    }


    fun checkConnectivity() {
        _connectivityUI.value = when {
            connectivityManager.isNetworkConnected -> Event(ConnectivityStatus.Connected)
            else -> Event(ConnectivityStatus.Disconnected)
        }
    }
}