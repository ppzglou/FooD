package gr.ppzglou.food.ui.landing.fragments

import android.view.animation.AnimationUtils
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import gr.ppzglou.food.R
import gr.ppzglou.food.ui.landing.LandingViewModel
import gr.ppzglou.food.base.BaseFragment
import gr.ppzglou.food.databinding.FragmentLoginBinding
import gr.ppzglou.food.ext.foodToast
import gr.ppzglou.food.ext.hideKeyboard
import gr.ppzglou.food.ext.safeNavigate

class LoginFragment : BaseFragment<FragmentLoginBinding>() {

    private val viewModel: LandingViewModel by activityViewModels()

    override fun getViewBinding(): FragmentLoginBinding =
        FragmentLoginBinding.inflate(layoutInflater)


    override fun setupListeners() {
        with(binding) {
            backBtn.setOnClickListener {
                activity?.onBackPressed()
            }

            loginBtn.setOnClickListener {
                activity.hideKeyboard()
                /*if (validate()) {
                    viewModel.login(
                        email.editText?.text.toString(),
                        password.editText?.text.toString()
                    )
                }*/
            }
            forgotPass.setOnClickListener {
                findNavController().safeNavigate(
                    LoginFragmentDirections.actionNavLoginToNavForgotPass(),
                    R.id.nav_login
                )
            }
            resendEmailBtn.setOnClickListener {
               // viewModel.verificationEmail()
            }
        }

    }

    override fun setupObservers() {
       /* viewModel.successLogin.observe(viewLifecycleOwner) {
            viewModel.fetchDaoUserPin()
            activity?.foodToast("oook")

        }
        viewModel.successVerEmail.observe(viewLifecycleOwner) {
            activity?.foodToast("des ta email sou")
            binding.resendEmailBtn.visibility = View.INVISIBLE
        }
        viewModel.fetchDaoUserPin.observe(viewLifecycleOwner) {
            if (it != null) {
                startActivity(Intent(activity, DashboardActivity::class.java))
                activity?.finish()
            }
        }
        viewModel.error.observe(viewLifecycleOwner) {
            if (it == ERROR_NOT_EMAIL_VALIDATED) {
                binding.resendEmailBtn.visibility = View.VISIBLE
            }
        }
        viewModel.fetchDaoUserPinError.observe(viewLifecycleOwner) {
            if (it == ERROR_PIN_OF_USER_NOT_EXIST) {
                startActivity(Intent(activity, AuthSetupActivity::class.java))
                activity?.finish()
            }
        }
        viewModel.loadLogin.observe(viewLifecycleOwner, { event ->
            event.getContentIfNotHandled()?.let {
                if (it) startAnimLogo()
                else stopAnimLogo()
            }
        })
    }

    private fun validate(): Boolean {
        var flag = true
        with(binding) {
            val email = !email.isValidEmail()
            val pass = !password.isNotEmpty()

            if (email || pass) flag = false

            return flag
        }*/
    }

    private fun startAnimLogo() {
        val anim = AnimationUtils.loadAnimation(context, R.anim.rotate_anim)
        binding.logoLogin.startAnimation(anim)
        binding.loaderViewLogin.isVisible = true
    }

    private fun stopAnimLogo() {
        binding.logoLogin.clearAnimation()
        binding.loaderViewLogin.isVisible = false
    }


    override fun setupViews() {
    }

}