package gr.ppzglou.food.ui.dashboard.fragments.settings

import androidx.navigation.fragment.findNavController
import gr.ppzglou.food.R
import gr.ppzglou.food.base.BaseFragment
import gr.ppzglou.food.databinding.FragmentPersonalDetailsSettingsBinding
import gr.ppzglou.food.ext.safeNavigate

class PersonalDetailsSettingsFragment : BaseFragment<FragmentPersonalDetailsSettingsBinding>() {

    override fun getViewBinding(): FragmentPersonalDetailsSettingsBinding =
        FragmentPersonalDetailsSettingsBinding.inflate(layoutInflater)


    override fun setupListeners() {
        with(binding) {
            nameBtn.setOnClickListener {
                findNavController().safeNavigate(
                    PersonalDetailsSettingsFragmentDirections.actionNavPersonalDetailsSettingsToNavChangePersonalDetails(),
                    R.id.nav_personal_details_settings
                )
            }
            phoneBtn.setOnClickListener {
                findNavController().safeNavigate(
                    PersonalDetailsSettingsFragmentDirections.actionNavPersonalSettingsToNavChangePhone(),
                    R.id.nav_personal_details_settings
                )
            }
        }
    }

    override fun setupObservers() {}

    override fun setupViews() {}

}