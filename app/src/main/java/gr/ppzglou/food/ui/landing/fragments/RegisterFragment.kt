package gr.ppzglou.food.ui.landing.fragments

import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import gr.ppzglou.food.base.BaseFragment
import gr.ppzglou.food.databinding.FragmentRegisterBinding
import gr.ppzglou.food.ext.foodToast
import gr.ppzglou.food.ext.isNotEmpty
import gr.ppzglou.food.ext.isValidEmail
import gr.ppzglou.food.ext.isValidPassword
import gr.ppzglou.food.ui.landing.LandingViewModel

class RegisterFragment : BaseFragment<FragmentRegisterBinding>() {

    private val viewModel: LandingViewModel by activityViewModels()

    override fun getViewBinding(): FragmentRegisterBinding =
        FragmentRegisterBinding.inflate(layoutInflater)


    override fun setupListeners() {
        with(binding) {
            backBtn.setOnClickListener {
                activity?.onBackPressed()
            }
            regBtn.setOnClickListener {
                if (validate()) {
                    viewModel.register(
                        email.editText?.text.toString(),
                        password.editText?.text.toString(),
                        fname.editText?.text.toString(),
                        sname.editText?.text.toString()
                    )
                }
            }
        }

    }

    override fun setupObservers() {
        viewModel.successRegister.observe(viewLifecycleOwner, {
            activity?.foodToast("ook")

            findNavController().navigate(RegisterFragmentDirections.actionNavRegisterToNavLogin())
        })
        viewModel.successVerEmail.observe(viewLifecycleOwner, {
            activity?.foodToast("epitixis email")
        })
    }

    override fun setupViews() {
    }

    private fun validate(): Boolean {
        var flag = true
        with(binding) {
            val name = !fname.isNotEmpty()
            val sname = !sname.isNotEmpty()
            val email = !email.isValidEmail()
            val pass = !password.isValidPassword()

            if (name || sname || email || pass) flag = false

            return flag
        }
    }

}