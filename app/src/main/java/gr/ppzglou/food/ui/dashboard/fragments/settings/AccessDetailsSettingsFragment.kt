package gr.ppzglou.food.ui.dashboard.fragments.settings

import androidx.navigation.fragment.findNavController
import gr.ppzglou.food.R
import gr.ppzglou.food.base.BaseFragment
import gr.ppzglou.food.databinding.FragmentAccessDetailsSettingsBinding
import gr.ppzglou.food.ext.safeNavigate

class AccessDetailsSettingsFragment : BaseFragment<FragmentAccessDetailsSettingsBinding>() {

    override fun getViewBinding(): FragmentAccessDetailsSettingsBinding =
        FragmentAccessDetailsSettingsBinding.inflate(layoutInflater)


    override fun setupListeners() {
        with(binding) {
            pinBtn.setOnClickListener {
                findNavController().safeNavigate(
                    AccessDetailsSettingsFragmentDirections.actionNavAccessDetailsSettingsToNavChangePin(),
                    R.id.nav_access_details_settings
                )
            }
            passwordBtn.setOnClickListener {
                findNavController().safeNavigate(
                    AccessDetailsSettingsFragmentDirections.actionNavAccessDetailsSettingsToNavChangePass(),
                    R.id.nav_access_details_settings
                )
            }
            emailBtn.setOnClickListener {
                findNavController().safeNavigate(
                    AccessDetailsSettingsFragmentDirections.actionNavAccessDetailsSettingsToNavChangeEmail(),
                    R.id.nav_access_details_settings
                )
            }
        }
    }

    override fun setupObservers() {}

    override fun setupViews() {}

}