package gr.ppzglou.food.ui.dashboard.fragments.profile

import androidx.fragment.app.activityViewModels
import gr.ppzglou.food.base.BaseFragment
import gr.ppzglou.food.databinding.FragmentProfileBinding
import gr.ppzglou.food.ui.dashboard.DashboardViewModel

class ProfileFragment : BaseFragment<FragmentProfileBinding>() {

    private val viewModel: DashboardViewModel by activityViewModels()


    override fun getViewBinding(): FragmentProfileBinding =
        FragmentProfileBinding.inflate(layoutInflater)


    override fun setupListeners() {

    }

    override fun setupObservers() {

    }

    override fun setupViews() {

    }


}