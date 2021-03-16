package gr.ppzglou.food.ui.dashboard.fragments.settings

import androidx.fragment.app.activityViewModels
import gr.ppzglou.food.R
import gr.ppzglou.food.base.BaseFragment
import gr.ppzglou.food.databinding.FragmentChangePassBinding
import gr.ppzglou.food.ext.foodToast
import gr.ppzglou.food.ext.isNotEmpty
import gr.ppzglou.food.ext.isValidPassword
import gr.ppzglou.food.ui.dashboard.DashboardViewModel

class ChangePassFragment : BaseFragment<FragmentChangePassBinding>() {

    private val viewModel: DashboardViewModel by activityViewModels()

    override fun getViewBinding(): FragmentChangePassBinding =
        FragmentChangePassBinding.inflate(layoutInflater)

    override fun setupObservers() {
        viewModel.successPassUpdated.observe(viewLifecycleOwner) {
            activity?.foodToast("success changed!")
            activity?.onBackPressed()
        }
    }

    override fun setupListeners() {
        with(binding) {
            changePassBtn.setOnClickListener {
                 if (validate())
                     viewModel.updatePass(
                         oldPassword.editText?.text.toString(),
                         newPassword.editText?.text.toString()
                     )
            }
        }
    }

    private fun validate(): Boolean {
        var flag = true
        with(binding) {
            val oldPass = !oldPassword.isNotEmpty()
            val newPass = !newPassword.isValidPassword()
            val verNewPass = !verNewPassword.isNotEmpty()

            if (oldPass || newPass || verNewPass) flag = false

            if (flag)
                if (newPassword.editText?.text.toString() != verNewPassword.editText?.text.toString()) {
                    verNewPassword.error = resources.getString(R.string.error_not_same_pass)
                    flag = false
                }

            return flag
        }
    }

    override fun setupViews() {}
}