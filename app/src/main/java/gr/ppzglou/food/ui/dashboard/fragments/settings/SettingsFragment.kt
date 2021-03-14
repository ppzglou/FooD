package gr.ppzglou.food.ui.dashboard.fragments.settings

import androidx.fragment.app.activityViewModels
import gr.ppzglou.food.base.BaseFragment
import gr.ppzglou.food.databinding.FragmentSettingsBinding
import gr.ppzglou.food.ui.dashboard.DashboardViewModel


class SettingsFragment : BaseFragment<FragmentSettingsBinding>() {

    private val viewModel: DashboardViewModel by activityViewModels()

    override fun getViewBinding(): FragmentSettingsBinding =
        FragmentSettingsBinding.inflate(layoutInflater)


    override fun setupListeners() {

    }

    override fun setupObservers() {

    }

    override fun setupViews() {

    }


}