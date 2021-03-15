package gr.ppzglou.food.ui.dashboard.fragments.settings

import android.app.Dialog
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import gr.ppzglou.food.R
import gr.ppzglou.food.base.BaseFragment
import gr.ppzglou.food.databinding.FragmentSettingsBinding
import gr.ppzglou.food.ext.safeNavigate
import gr.ppzglou.food.ui.dashboard.DashboardViewModel


class SettingsFragment : BaseFragment<FragmentSettingsBinding>() {


    private val viewModel: DashboardViewModel by activityViewModels()

    override fun getViewBinding(): FragmentSettingsBinding =
        FragmentSettingsBinding.inflate(layoutInflater)


    override fun setupListeners() {
        with(binding) {
            logoutBtn.setOnClickListener {
                val dialog = context?.let { it1 -> Dialog(it1) }
                val listener = View.OnClickListener {
                    // viewModel.logout()
                }
                /*dialog?.createPopUpDialog(
                    getString(R.string.settings_dialog_title),
                    getString(R.string.settings_dialog_message),
                    getString(R.string.settings_dialog_poss_btn),
                    getString(R.string.settings_dialog_neg_btn),
                    listener
                )*/
            }
            fingerprintSwitch.setOnClickListener {
                // viewModel.changeFingerprintSettings()
            }
            deleteBtn.setOnClickListener {
                findNavController().safeNavigate(
                    SettingsFragmentDirections.actionNavSettingsToNavDeleteAccountMessage(),
                    R.id.nav_settings
                )
            }
            changePassBtn.setOnClickListener {
                findNavController().safeNavigate(
                    SettingsFragmentDirections.actionNavSettingsToNavPassSettings(),
                    R.id.nav_settings
                )
            }
            quickSetup.setOnClickListener {
                findNavController().safeNavigate(
                    SettingsFragmentDirections.actionNavSettingsToNavSearch(),
                    R.id.nav_settings
                )
                /*  val intent = Intent(context, StepperActivity::class.java)
                  startActivity(intent)*/
            }

            changePersonalDetailsBtn.setOnClickListener {
                findNavController().safeNavigate(
                    SettingsFragmentDirections.actionNavSettingsToNavPersonalSettings(),
                    R.id.nav_settings
                )
            }
            changeTheme.setOnClickListener {
                //showThemeDialog()
            }
        }
    }

    override fun setupObservers() {
        with(viewModel) {
            /* successLogout.observe(viewLifecycleOwner) {
                 val intent = Intent(context, LandingActivity::class.java)
                 startActivity(intent)
                 activity?.finish()
             }
             fetchFingerprintSettings.observe(viewLifecycleOwner) {
                 binding.fingerprintSwitch.isChecked = it
             }*/
        }
    }

    override fun setupViews() {
        // viewModel.fetchFingerprintSettings()
    }


}