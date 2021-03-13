package gr.ppzglou.food.ui.landing.fragments

import androidx.fragment.app.activityViewModels
import gr.ppzglou.food.ui.landing.LandingViewModel
import gr.ppzglou.food.base.BaseFragment
import gr.ppzglou.food.databinding.FragmentForgotPassBinding

class ForgotPassFragment : BaseFragment<FragmentForgotPassBinding>() {

    private val viewModel: LandingViewModel by activityViewModels()

    override fun getViewBinding(): FragmentForgotPassBinding =
        FragmentForgotPassBinding.inflate(layoutInflater)


    override fun setupListeners() {
        with(binding) {
            backBtn.setOnClickListener {
                activity?.onBackPressed()
            }

            resetBtn.setOnClickListener {
                if (validate()) {
                   /* viewModel.resetPass(
                        email.editText?.text.toString()
                    )*/
                }
            }
        }

    }

    override fun setupObservers() {
       /* viewModel.successResetPass.observe(viewLifecycleOwner) {
            activity?.eForologikiToast("ok")
        }*/

    }

    private fun validate(): Boolean {
        var flag = true
        with(binding) {
           // if (!email.isValidEmail()) flag = false

            return flag
        }
    }

    override fun setupViews() {
    }

}