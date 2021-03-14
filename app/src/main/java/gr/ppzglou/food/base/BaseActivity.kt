package gr.ppzglou.food.base

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.viewbinding.ViewBinding
import com.tapadoo.alerter.Alerter
import gr.ppzglou.food.ERROR_NO_INTERNET
import gr.ppzglou.food.ERROR_PIN_OF_USER_IS_WRONG
import gr.ppzglou.food.ERROR_PIN_OF_USER_NOT_EXIST
import gr.ppzglou.food.R
import gr.ppzglou.food.ext.*
import gr.ppzglou.food.util.connectivity.ConnectivityStatus


abstract class BaseActivity<VB : ViewBinding, VM : BaseViewModel>(clazz: Class<VM>) :
    AppCompatActivity() {

    protected lateinit var binding: VB
    protected val viewModel: VM by lazy { ViewModelProvider(this).get(clazz) }
    private var snackbar: Alerter? = null

    abstract fun getViewBinding(): VB
    abstract fun setupObservers()
    abstract fun setupViews()
    abstract fun setupListeners()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = getViewBinding()
        setContentView(binding.root)

        viewModel.error.observe(this, Observer {
            if (it != ERROR_PIN_OF_USER_NOT_EXIST && it != ERROR_PIN_OF_USER_IS_WRONG)
                foodErrorSnackBar(it)
        })

        viewModel.load.observe(this, Observer{
            this.hideKeyboard()
        })

        setupObservers()
        setupViews()
        setupListeners()
    }

    protected fun connectivityChange(connectivityState: ConnectivityStatus) {
        val title = ERROR_NO_INTERNET.setTitle()
        val message = ERROR_NO_INTERNET.getErrorMessage()
        val icon = ERROR_NO_INTERNET.setIcon()
        val color = R.color.colorPrimaryDark

        snackbar = Alerter.create(this)
            .setTitle(getString(title))
            .setText(getString(message))
            .setBackgroundColorRes(color)
            .setIcon(icon)
            .setDuration(999999)

        when (connectivityState) {
            ConnectivityStatus.Connected -> {
                snackbar!!.setDuration(0)
            }
            else -> {
                snackbar!!.show()
            }
        }
    }
}