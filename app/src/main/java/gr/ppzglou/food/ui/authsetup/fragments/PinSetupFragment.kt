package gr.ppzglou.food.ui.authsetup.fragments

import android.content.Intent
import android.view.animation.AnimationUtils
import androidx.biometric.BiometricManager
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import gr.ppzglou.food.R
import gr.ppzglou.food.base.BaseFragment
import gr.ppzglou.food.databinding.FragmentPinSetupBinding
import gr.ppzglou.food.ext.*
import gr.ppzglou.food.ui.authsetup.AuthSetupViewModel
import gr.ppzglou.food.ui.dashboard.DashboardActivity

class PinSetupFragment : BaseFragment<FragmentPinSetupBinding>() {

    private val viewModel: AuthSetupViewModel by activityViewModels()
    private var pin1 = ""
    private var pin2 = ""
    private var pinSwitch = false

    override fun getViewBinding(): FragmentPinSetupBinding =
        FragmentPinSetupBinding.inflate(layoutInflater)


    override fun setupObservers() {
        viewModel.successAddDaoUserPin.observe(viewLifecycleOwner) {
            if (context?.let { it1 ->
                    BiometricManager.from(it1).canAuthenticate()
                } == BiometricManager.BIOMETRIC_SUCCESS) {
                findNavController().safeNavigate(
                    PinSetupFragmentDirections.actionNavPinToNavFingerprint(),
                    R.id.nav_pin
                )
            } else {
                val intent = Intent(context, DashboardActivity::class.java)
                startActivity(intent)
                activity?.finish()
            }
        }
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
                activity?.vibratePhone()
                if (pin1.length < 4 && pin1.isNotEmpty()) {
                    pin1 = ""
                    pinViewOnChange(pin1.length)
                } else if (pin1.isNullOrEmptyOrBlank) {
                    icBackspace.isVisible = false
                } else if (pinSwitch) {
                    if (pin2.length < 4 && pin2.isNotEmpty()) {
                        pin2 = ""
                        pinViewOnChange(pin2.length)
                    } else if (pin2.isNullOrEmptyOrBlank) {
                        icBackspace.isVisible = false
                    }
                }
            }
        }
    }

    private fun setNumToPin(num: String) {
        activity?.vibratePhone()
        if (pin1.length < 4) {
            pin1 += num
            pinViewOnChange(pin1.length)
            if (pin1.length == 4) {
                pinSwitch = true
                viewModel.loader()
                binding.titleTv.text = getString(R.string.pin_setup_title2)
                pinViewOnChange(0)
            }
        } else if (pinSwitch && pin2.length < 4) {
            pin2 += num
            pinViewOnChange(pin2.length)
        }

        if (pin1.length == 4 && pin2.length == 4) {
            if (pin1 == pin2)
                viewModel.addDaoUserPin(pin1)
            else {
                activity?.foodToast(getString(R.string.pin_setup_deference_pins))
                pin1 = ""
                pin2 = ""
                binding.titleTv.text = getString(R.string.pin_setup_title1)
                pinViewOnChange(0)
                pinLayoutAnimated()
            }
        }
    }

    private fun pinViewOnChange(pin: Int) {
        with(binding) {
            val bg = R.drawable.f_btn_primary_20
            val bgTrans = R.drawable.f_bullet_dark_trans
            when (pin) {
                1 -> {
                    icBackspace.isVisible = true
                    pinDot1.setPic(bg)
                }
                2 -> pinDot2.setPic(bg)
                3 -> pinDot3.setPic(bg)
                4 -> pinDot4.setPic(bg)
                else -> {
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
        val topAnim = AnimationUtils.loadAnimation(context, R.anim.pin_layout_top_anim)
        val btnAnim = AnimationUtils.loadAnimation(context, R.anim.pin_layout_btm_anim)

        with(binding)
        {
            pinDot1.startAnimation(topAnim)
            pinDot2.startAnimation(btnAnim)
            pinDot3.startAnimation(topAnim)
            pinDot4.startAnimation(btnAnim)
        }
    }

    override fun setupViews() {
    }

}