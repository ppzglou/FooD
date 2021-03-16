package gr.ppzglou.food.ui.dashboard.fragments.settings

import android.app.Dialog
import android.content.Intent
import android.view.View
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomsheet.BottomSheetDialog
import gr.ppzglou.food.DARK_MODE
import gr.ppzglou.food.LIGHT_MODE
import gr.ppzglou.food.R
import gr.ppzglou.food.SYSTEM_MODE
import gr.ppzglou.food.base.BaseFragment
import gr.ppzglou.food.databinding.FragmentSettingsBinding
import gr.ppzglou.food.databinding.ThemeSelectorDialogBinding
import gr.ppzglou.food.ext.createPopUpDialog
import gr.ppzglou.food.ext.safeNavigate
import gr.ppzglou.food.ui.dashboard.DashboardViewModel
import gr.ppzglou.food.ui.landing.LandingActivity
import gr.ppzglou.food.ui.stepper.StepperActivity


class SettingsFragment : BaseFragment<FragmentSettingsBinding>() {

    private val viewModel: DashboardViewModel by activityViewModels()

    override fun getViewBinding(): FragmentSettingsBinding =
        FragmentSettingsBinding.inflate(layoutInflater)


    override fun setupListeners() {
        with(binding) {
            logoutBtn.setOnClickListener {
                val dialog = context?.let { it1 -> Dialog(it1) }
                val listener = View.OnClickListener {
                    viewModel.logout()
                }
                dialog?.createPopUpDialog(
                    getString(R.string.settings_dialog_title),
                    getString(R.string.settings_dialog_message),
                    getString(R.string.settings_dialog_poss_btn),
                    getString(R.string.settings_dialog_neg_btn),
                    listener
                )
            }
            fingerprintSwitch.setOnClickListener {
                viewModel.changeFingerprintSettings()
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
                val intent = Intent(context, StepperActivity::class.java)
                startActivity(intent)
            }
            changePersonalDetailsBtn.setOnClickListener {
                findNavController().safeNavigate(
                    SettingsFragmentDirections.actionNavSettingsToNavPersonalSettings(),
                    R.id.nav_settings
                )
            }
            changeTheme.setOnClickListener {
                showThemeDialog()
            }
        }
    }

    override fun setupObservers() {
        with(viewModel) {
            successLogout.observe(viewLifecycleOwner) {
                val intent = Intent(context, LandingActivity::class.java)
                startActivity(intent)
                activity?.finish()
            }
            fetchFingerprintSettings.observe(viewLifecycleOwner) {
                binding.fingerprintSwitch.isChecked = it
            }
        }
    }

    override fun setupViews() {
        viewModel.fetchFingerprintSettings()
    }

    private fun showThemeDialog() {
        val itemViewBinding = ThemeSelectorDialogBinding.inflate(layoutInflater)
        val dialog = BottomSheetDialog(itemViewBinding.root.context)

        dialog.setContentView(itemViewBinding.root)
        val theme = viewModel.getTheme()

        with(itemViewBinding) {
            when (theme) {
                DARK_MODE -> darkTxt.background =
                    ContextCompat.getDrawable(darkTxt.context, R.drawable.f_btn_primary_20)
                LIGHT_MODE -> lightTxt.background =
                    ContextCompat.getDrawable(darkTxt.context, R.drawable.f_btn_primary_20)
                else -> systemTxt.background =
                    ContextCompat.getDrawable(darkTxt.context, R.drawable.f_btn_primary_20)
            }

            darkCard.setOnClickListener {
                viewModel.changeTheme(DARK_MODE)
                dialog.hide()
            }
            systemCard.setOnClickListener {
                viewModel.changeTheme(SYSTEM_MODE)
                dialog.hide()
            }
            lightCard.setOnClickListener {
                viewModel.changeTheme(LIGHT_MODE)
                dialog.hide()
            }
        }
        dialog.show()
    }

}