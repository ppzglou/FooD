package gr.ppzglou.food.ui.dashboard.fragments.settings

import androidx.fragment.app.activityViewModels
import gr.ppzglou.food.base.BaseFragment
import gr.ppzglou.food.databinding.FragmentChangeEmailBinding
import gr.ppzglou.food.ext.foodToast
import gr.ppzglou.food.ui.dashboard.DashboardViewModel

class ChangeEmailFragment : BaseFragment<FragmentChangeEmailBinding>() {

    private val viewModel: DashboardViewModel by activityViewModels()

    override fun getViewBinding(): FragmentChangeEmailBinding =
        FragmentChangeEmailBinding.inflate(layoutInflater)


    override fun setupListeners() {
        with(binding) {
            changeEmailBtn.setOnClickListener {
                viewModel.updateEmail(
                    newEmail.editText?.text.toString(),
                    password.editText?.text.toString()
                )
            }
        }
    }

    override fun setupObservers() {
        viewModel.successEmailUpdated.observe(viewLifecycleOwner) {
            activity?.foodToast("ook emial")
        }

    }

    override fun setupViews() {
    }

}