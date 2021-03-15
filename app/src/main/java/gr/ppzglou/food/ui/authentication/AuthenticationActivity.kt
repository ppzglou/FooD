package gr.ppzglou.food.ui.authentication

import android.content.Intent
import android.view.animation.AnimationUtils
import androidx.biometric.BiometricManager.BIOMETRIC_SUCCESS
import androidx.biometric.BiometricManager.from
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import dagger.hilt.android.AndroidEntryPoint
import gr.ppzglou.food.ERROR_PIN_OF_USER_IS_WRONG
import gr.ppzglou.food.R
import gr.ppzglou.food.base.BaseActivity
import gr.ppzglou.food.databinding.ActivityAuthenticationBinding
import gr.ppzglou.food.ext.foodErrorSnackBar
import gr.ppzglou.food.ext.setPic
import gr.ppzglou.food.ext.vibratePhone
import gr.ppzglou.food.ui.dashboard.DashboardActivity
import gr.ppzglou.food.ui.landing.LandingActivity
import ir.samanjafari.easycountdowntimer.CountDownInterface
import java.util.concurrent.Executor
import java.util.concurrent.TimeUnit


@AndroidEntryPoint
class AuthenticationActivity :
    BaseActivity<ActivityAuthenticationBinding, AuthenticationViewModel>(AuthenticationViewModel::class.java) {

    private lateinit var executor: Executor
    private lateinit var biometricPrompt: BiometricPrompt
    private lateinit var promptInfo: BiometricPrompt.PromptInfo
    private var pin = ""
    private var endTime = 0L
    private var TIME = 2
    private var biometricSensorAvailable = 0
    private var isUserWantFingerprint = false

    override fun getViewBinding(): ActivityAuthenticationBinding {
        return ActivityAuthenticationBinding.inflate(layoutInflater)
    }

    override fun setupObservers() {
        with(viewModel) {
            isFingerprint.observe(this@AuthenticationActivity) {
                isUserWantFingerprint = it
                biometricSensorAvailable = from(this@AuthenticationActivity).canAuthenticate()
                if (biometricSensorAvailable == BIOMETRIC_SUCCESS && isUserWantFingerprint) {
                    setupBiometrics()
                } else {
                    binding.icBackspace.setPic(R.drawable.ic_baseline_backspace_24)
                    binding.icBackspace.isVisible = false
                }
                viewModel.fetchPinTime()
            }
            fetchPinCount.observe(this@AuthenticationActivity) {
                if (it == 3)
                    setupTimer()
            }
            fetchPinTime.observe(this@AuthenticationActivity) {
                endTime = it
                if (System.currentTimeMillis() < endTime)
                    startTimer()
                else {
                    if (biometricSensorAvailable == BIOMETRIC_SUCCESS && isUserWantFingerprint)
                        biometricPrompt.authenticate(promptInfo)
                    else
                        pinLayoutVisible(true)
                }
            }
            fetchDaoUserPin.observe(this@AuthenticationActivity) {
                updateCountOfPin(0)
                navigateToDashboard()
            }
            successLogout.observe(this@AuthenticationActivity) {
                val intent = Intent(this@AuthenticationActivity, LandingActivity::class.java)
                startActivity(intent)
                finish()
            }
            error.observe(this@AuthenticationActivity) {
                foodErrorSnackBar(it)
                if (it == ERROR_PIN_OF_USER_IS_WRONG) {
                    refactorData()
                }
            }
        }
    }

    override fun setupViews() {
        viewModel.isUserFingerprint()
        binding.icBackspace.setColorFilter(ContextCompat.getColor(this, R.color.colorPrimary))
    }

    override fun setupListeners() {
        with(binding) {
            btn0.setOnClickListener { setNumToPin("0") }
            btn1.setOnClickListener { setNumToPin("1") }
            btn2.setOnClickListener { setNumToPin("2") }
            btn3.setOnClickListener { setNumToPin("3") }
            btn4.setOnClickListener { setNumToPin("4") }
            btn5.setOnClickListener { setNumToPin("5") }
            btn6.setOnClickListener { setNumToPin("6") }
            btn7.setOnClickListener { setNumToPin("7") }
            btn8.setOnClickListener { setNumToPin("8") }
            btn9.setOnClickListener { setNumToPin("9") }
            icBackspace.setOnClickListener {
                vibratePhone()
                if (pin.isNotEmpty()) {
                    pin = ""
                    pinViewOnChange()
                    if (biometricSensorAvailable != BIOMETRIC_SUCCESS && isUserWantFingerprint) {
                        icBackspace.isVisible = false
                    }
                } else {
                    if (biometricSensorAvailable == BIOMETRIC_SUCCESS && isUserWantFingerprint) {
                        pinLayoutVisible(false)
                        biometricPrompt.authenticate(promptInfo)
                    } else
                        icBackspace.isVisible = false
                }
            }
            usePassBtn.setOnClickListener {
                authBtnsVisible(false)
                pinLayoutVisible(true)
            }
            useFingerprintBtn.setOnClickListener {
                biometricPrompt.authenticate(promptInfo)
                authBtnsVisible(false)
                pinLayoutVisible(false)
            }
            loginBtn.setOnClickListener {
                viewModel.logout()
            }
            timer.setOnTick(object : CountDownInterface {
                override fun onTick(time: Long) {}

                override fun onFinish() {
                    errorLayoutVisible(false)
                    if (biometricSensorAvailable == BIOMETRIC_SUCCESS && isUserWantFingerprint) {
                        pinLayoutVisible(false)
                        authBtnsVisible(true)
                        viewModel.updateCountOfPin(2)
                    } else {
                        pinLayoutVisible(true)
                        authBtnsVisible(false)
                    }
                }
            })
        }
    }

    private fun setupBiometrics() {
        executor = ContextCompat.getMainExecutor(this)

        biometricPrompt = BiometricPrompt(
            this@AuthenticationActivity,
            executor,
            object : BiometricPrompt.AuthenticationCallback() {
                override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                    super.onAuthenticationError(errorCode, errString)
                    pinLayoutVisible(false)
                    authBtnsVisible(true)
                }

                override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                    super.onAuthenticationSucceeded(result)
                    viewModel.updateCountOfPin(0)
                    navigateToDashboard()
                }

                override fun onAuthenticationFailed() {
                    super.onAuthenticationFailed()
                    pinLayoutVisible(false)
                    authBtnsVisible(true)
                }
            })

        promptInfo = BiometricPrompt.PromptInfo.Builder()
            .setTitle(getString(R.string.auth_dialog_title))
            .setNegativeButtonText(getString(R.string.auth_dialog_btn))
            .build()
    }

    private fun setNumToPin(num: String) {
        vibratePhone()
        with(binding) {
            icBackspace.isVisible = true
            icBackspace.setPic(R.drawable.ic_baseline_backspace_24)
        }
        if (pin.length < 4) {
            pin += num
            pinViewOnChange()
            if (pin.length == 4)
                viewModel.fetchDaoUserPin(pin)
        }
    }

    private fun pinViewOnChange() {
        with(binding) {
            val bg = R.drawable.f_btn_primary_20
            val bgTrans = R.drawable.f_bullet_dark_trans
            when (pin.length) {
                1 -> {
                    icBackspace.setPic(R.drawable.ic_baseline_backspace_24)
                    pinDot1.setPic(bg)
                }
                2 -> pinDot2.setPic(bg)
                3 -> pinDot3.setPic(bg)
                4 -> pinDot4.setPic(bg)
                else -> {
                    if (biometricSensorAvailable == BIOMETRIC_SUCCESS && isUserWantFingerprint)
                        icBackspace.setPic(R.drawable.ic_baseline_fingerprint_24)
                    else
                        icBackspace.isVisible = false
                    pinDot1.setPic(bgTrans)
                    pinDot2.setPic(bgTrans)
                    pinDot3.setPic(bgTrans)
                    pinDot4.setPic(bgTrans)
                }
            }
        }
    }

    private fun pinLayoutAnimated() {
        val topAnim = AnimationUtils.loadAnimation(this, R.anim.pin_layout_top_anim)
        val btnAnim = AnimationUtils.loadAnimation(this, R.anim.pin_layout_btm_anim)

        with(binding)
        {
            pinDot1.startAnimation(topAnim)
            pinDot2.startAnimation(btnAnim)
            pinDot3.startAnimation(topAnim)
            pinDot4.startAnimation(btnAnim)
        }
    }

    private fun setupTimer() {
        with(viewModel) {
            updateCountOfPin(2)
            endTime = System.currentTimeMillis() + TimeUnit.MINUTES.toMillis(TIME.toLong())
            addPinTime(endTime)

            with(binding) {
                timer.setTime(0, 0, TIME, 0)
                timer.startTimer()
                errorLayoutVisible(true)
                pinLayoutVisible(false)
            }
        }
    }

    private fun startTimer() {
        errorLayoutVisible(true)
        authBtnsVisible(false)
        pinLayoutVisible(false)
        val minutes = (endTime - System.currentTimeMillis()) / 1000 / 60
        val seconds = (endTime - System.currentTimeMillis()) / 1000 % 60

        binding.timer.setTime(0, 0, minutes.toInt(), seconds.toInt())
        binding.timer.startTimer()
    }

    private fun refactorData() {
        with(viewModel) {
            updateCountOfPin()
            pin = ""
            pinViewOnChange()
            pinLayoutAnimated()
            fetchCountOfPin()
        }
    }

    private fun navigateToDashboard() {
        val intent = Intent(this@AuthenticationActivity, DashboardActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun pinLayoutVisible(isVisible: Boolean) {
        with(binding) {
            keyboardLayout.isVisible = isVisible
            pinLayout.isVisible = isVisible
            loginBtn.isVisible = isVisible
        }
    }

    private fun authBtnsVisible(isVisible: Boolean) {
        with(binding) {
            useFingerprintBtn.isVisible = isVisible
            usePassBtn.isVisible = isVisible
        }
    }

    private fun errorLayoutVisible(isVisible: Boolean) {
        with(binding) {
            errorLayout.isVisible = isVisible
        }
    }

    override fun onBackPressed() {}
}