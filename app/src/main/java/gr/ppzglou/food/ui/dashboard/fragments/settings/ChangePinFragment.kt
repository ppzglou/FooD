package gr.ppzglou.food.ui.dashboard.fragments.settings

import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import gr.ppzglou.food.R
import gr.ppzglou.food.base.BaseFragment
import gr.ppzglou.food.databinding.FragmentChangePinBinding
import gr.ppzglou.food.ext.foodErrorSnackBar
import gr.ppzglou.food.ext.foodToast
import gr.ppzglou.food.ext.isNotEmpty
import gr.ppzglou.food.ext.isValidPin
import gr.ppzglou.food.ui.dashboard.DashboardViewModel


class ChangePinFragment : BaseFragment<FragmentChangePinBinding>() {

    private val viewModel: DashboardViewModel by activityViewModels()

    override fun getViewBinding(): FragmentChangePinBinding =
        FragmentChangePinBinding.inflate(layoutInflater)


    override fun setupListeners() {
        with(binding) {
            pinBtn.setOnClickListener {
                oldPin.isVisible = true
                pass.isVisible = false
            }
            passBtn.setOnClickListener {
                oldPin.isVisible = false
                pass.isVisible = true
                choiceErrorTxt.isVisible = false
            }
            changePinBtn.setOnClickListener {
                if (validate()) {
                    when {
                        oldPin.isVisible -> {
                            if (oldPin.isNotEmpty())
                                viewModel.fetchDaoUserPin(oldPin.editText?.text.toString())
                        }
                        pass.isVisible -> {
                            if (pass.isNotEmpty())
                                viewModel.auth(pass.editText?.text.toString())
                        }
                        else -> {
                            choiceErrorTxt.isVisible = true
                        }
                    }
                }
            }
        }
    }

    override fun setupObservers() {
        viewModel.successAuth.observe(viewLifecycleOwner) {
            viewModel.changePin(binding.newPin.editText?.text.toString())
        }
        viewModel.fetchDaoUserPin.observe(viewLifecycleOwner) {
            viewModel.changePin(binding.newPin.editText?.text.toString())
        }
        viewModel.successPinChanged.observe(viewLifecycleOwner) {
            activity?.foodToast("success changed!")
        }
        viewModel.error.observe(viewLifecycleOwner) {
            activity?.foodErrorSnackBar(it)
        }
    }

    private fun validate(): Boolean {
        var flag = true
        with(binding) {
            val new = !newPin.isValidPin()
            val ver = !verNewPin.isValidPin()

            if (new || ver) flag = false

            if (flag)
                if (newPin.editText?.text.toString() != verNewPin.editText?.text.toString()) {
                    verNewPin.error = resources.getString(R.string.error_not_same_pin)
                    flag = false
                }

            return flag
        }
    }

    override fun setupViews() {
    }

}