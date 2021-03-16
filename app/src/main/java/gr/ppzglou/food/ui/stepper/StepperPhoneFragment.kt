package gr.ppzglou.food.ui.stepper

import android.view.View
import android.view.animation.AnimationUtils
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.google.firebase.FirebaseException
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider
import gr.ppzglou.food.ERROR_GENERAL
import gr.ppzglou.food.ERROR_INVALID_CREDENTIALS
import gr.ppzglou.food.ERROR_TOO_MANY_REQUESTS
import gr.ppzglou.food.R
import gr.ppzglou.food.base.BaseFragment
import gr.ppzglou.food.databinding.FragmentStepperPhoneBinding
import gr.ppzglou.food.ext.*
import io.michaelrocks.libphonenumber.android.NumberParseException
import io.michaelrocks.libphonenumber.android.PhoneNumberUtil
import ir.samanjafari.easycountdowntimer.CountDownInterface


class StepperPhoneFragment : BaseFragment<FragmentStepperPhoneBinding>() {

    private val viewModel: StepperViewModel by activityViewModels()
    private var storedVerificationId = ""
    private var otpCode = ""

    override fun getViewBinding(): FragmentStepperPhoneBinding =
        FragmentStepperPhoneBinding.inflate(layoutInflater)

    override fun setupObservers() {
        with(viewModel) {
            successPhoneCredentials.observe(viewLifecycleOwner) {
                navigateToNextPage()
            }
            successGetPhone.observe(viewLifecycleOwner) {
                if (!it.isNullOrEmptyOrBlank)
                    showChangeMyPhoneViews(it!!)
            }
            successPhoneOtp.observe(viewLifecycleOwner) {
                binding.sendCodeBtn.visibility = View.INVISIBLE
            }
            error.observe(viewLifecycleOwner) {
                binding.sendCodeBtn.visibility = View.VISIBLE
            }
        }
    }

    override fun setupViews() {
        binding.timer.setTime(0, 0, 0, 30)
        viewModel.getPhone()

        with(binding)
        {
            ed1.addTextChangedListener(GenericTextWatcher(ed1, ed2, binding))
            ed2.addTextChangedListener(GenericTextWatcher(ed2, ed3, binding))
            ed3.addTextChangedListener(GenericTextWatcher(ed3, ed4, binding))
            ed4.addTextChangedListener(GenericTextWatcher(ed4, ed5, binding))
            ed5.addTextChangedListener(GenericTextWatcher(ed5, ed6, binding))
            ed6.addTextChangedListener(GenericTextWatcher(ed6, null, binding))

            ed1.setOnKeyListener(GenericKeyEvent(ed1, null, binding))
            ed2.setOnKeyListener(GenericKeyEvent(ed2, ed1, binding))
            ed3.setOnKeyListener(GenericKeyEvent(ed3, ed2, binding))
            ed4.setOnKeyListener(GenericKeyEvent(ed4, ed3, binding))
            ed5.setOnKeyListener(GenericKeyEvent(ed5, ed4, binding))
            ed6.setOnKeyListener(GenericKeyEvent(ed6, ed5, binding))
        }
    }

    override fun setupListeners() {
        with(binding) {
            with(viewModel) {
                sendCodeBtn.setOnClickListener {
                    val code = countryPicker.selectedCountryCode
                    if ((code == "30" && phone.isValidPhone()) || (code != "30" && phone.isNotEmpty())) {
                        val phoneNum = "+$code${phone.editText?.text}"
                        phoneOtp(phoneNum, callbacks())
                    }
                }
                verificationBtn.setOnClickListener {
                    if (otpCodeValidation()) {
                        updatePhoneCredentials(
                            otpCode,
                            storedVerificationId
                        )
                    }
                }
                changePhoneBtn.setOnClickListener {
                    showPhoneLayout()
                }
                resendCodeBtn.setOnClickListener {
                    val phoneNum = "+${countryPicker.selectedCountryCode}${phone.editText?.text}"
                    phoneOtp(phoneNum, callbacks())
                    timer.startTimer()
                    resendCodeBtn.visibility = View.INVISIBLE
                    changePhoneBtn.visibility = View.INVISIBLE
                }
                changeMyPhoneBtn.setOnClickListener {
                    showSendCodeViews()
                }
                next.setOnClickListener {
                    navigateToNextPage()
                }
                timer.setOnTick(object : CountDownInterface {
                    override fun onTick(time: Long) {}
                    override fun onFinish() {
                        resendCodeBtn.visibility = View.VISIBLE
                        changePhoneBtn.visibility = View.VISIBLE
                    }
                })
            }
        }
    }

    private fun callbacks() = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        override fun onVerificationCompleted(credential: PhoneAuthCredential) {}
        override fun onVerificationFailed(e: FirebaseException) {
            sendError(e)
        }

        override fun onCodeSent(
            verificationId: String,
            token: PhoneAuthProvider.ForceResendingToken
        ) {
            with(binding) {
                if (phoneLayout.visibility == View.VISIBLE)
                    showOtpCodeLayout()
                resendCodeBtn.visibility = View.INVISIBLE
                changePhoneBtn.visibility = View.INVISIBLE
                sendCodeBtn.visibility = View.VISIBLE
                timer.startTimer()
            }
            storedVerificationId = verificationId
        }
    }

    private fun sendError(e: FirebaseException) {
        when (e) {
            is FirebaseAuthInvalidCredentialsException -> viewModel._error.value =
                ERROR_INVALID_CREDENTIALS
            is FirebaseTooManyRequestsException -> viewModel._error.value =
                ERROR_TOO_MANY_REQUESTS
            else -> viewModel._error.value = ERROR_GENERAL
        }
    }

    private fun showPhoneLayout() {
        with(binding) {
            val enterLeft = AnimationUtils.loadAnimation(context, R.anim.enter_from_left)
            val exitRight = AnimationUtils.loadAnimation(context, R.anim.exit_to_right)
            otpCodeLayout.startAnimation(exitRight)
            phoneLayout.visibility = View.VISIBLE
            phoneLayout.startAnimation(enterLeft)
            phoneView.visibility = View.VISIBLE
            otpCodeLayout.visibility = View.GONE
            otpView.visibility = View.GONE
            cleanOtpField()
        }
    }

    private fun showOtpCodeLayout() {
        val enterRight = AnimationUtils.loadAnimation(context, R.anim.enter_from_right)
        val exitLeft = AnimationUtils.loadAnimation(context, R.anim.exit_to_left)
        with(binding) {
            phoneLayout.startAnimation(exitLeft)
            otpCodeLayout.visibility = View.VISIBLE
            otpCodeLayout.startAnimation(enterRight)
            otpView.visibility = View.VISIBLE
            ed1.requestFocus()
            activity?.openKeyboard()
            phoneLayout.visibility = View.GONE
            phoneView.visibility = View.GONE
            otpCodeError.isVisible = false
        }
    }

    private fun showChangeMyPhoneViews(myPhone: String) {
        with(binding) {
            var countryCode = getCountryIsoCode(myPhone)
            countryPicker.setCountryForNameCode(countryCode)
            countryCode = countryPicker.selectedCountryCode
            val myPhone = myPhone.substring(1)

            phone.editText?.setText(myPhone.replace(countryCode.toRegex(), ""))
            phone.editText?.isFocusableInTouchMode = false
            countryPicker.setCcpClickable(false)
            changePhoneTitle.visibility = View.VISIBLE
            changeMyPhoneBtn.visibility = View.VISIBLE
            next.visibility = View.VISIBLE
            sendCodeBtn.visibility = View.GONE
            otpCodeError.isVisible = false
            phoneImg.visibility = View.INVISIBLE
            phoneVerifiedImg.visibility = View.VISIBLE
            phoneDescription.visibility = View.INVISIBLE
        }
    }

    private fun showSendCodeViews() {
        with(binding) {
            phone.editText?.isFocusableInTouchMode = true
            countryPicker.setCcpClickable(true)
            changePhoneTitle.visibility = View.GONE
            changeMyPhoneBtn.visibility = View.GONE
            next.visibility = View.GONE
            sendCodeBtn.visibility = View.VISIBLE
            otpCodeError.isVisible = false
            phoneImg.visibility = View.VISIBLE
            phoneVerifiedImg.visibility = View.INVISIBLE
            phoneDescription.visibility = View.VISIBLE
        }
    }

    private fun navigateToNextPage() {
        findNavController().safeNavigate(
            StepperPhoneFragmentDirections.actionNavStepperPhoneToNavStepperPesronalInfo(),
            R.id.nav_stepper_phone
        )
    }

    private fun otpCodeValidation(): Boolean {
        with(binding) {
            otpCode = ed1.text.toString()
            otpCode += ed2.text.toString()
            otpCode += ed3.text.toString()
            otpCode += ed4.text.toString()
            otpCode += ed5.text.toString()
            otpCode += ed6.text.toString()
        }

        if (otpCode.length == 6)
            return true
        else
            binding.otpCodeError.isVisible = true
        return false
    }

    private fun cleanOtpField() {
        with(binding) {
            ed1.setText("")
            ed2.setText("")
            ed3.setText("")
            ed4.setText("")
            ed5.setText("")
            ed6.setText("")
        }
    }

    private fun getCountryIsoCode(number: String): String {
        val validatedNumber = if (number.startsWith("+")) number else "+$number"
        val phoneNumberUtil = PhoneNumberUtil.createInstance(context)
        val phoneNumber = try {
            phoneNumberUtil.parse(validatedNumber, null)
        } catch (e: NumberParseException) {
            null
        }

        return if (phoneNumber != null) {
            phoneNumberUtil.getRegionCodeForCountryCode(phoneNumber.countryCode)
        } else ""
    }
}