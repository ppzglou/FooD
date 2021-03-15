package gr.ppzglou.food.ui.authsetup.fragments

import android.content.Intent
import androidx.fragment.app.activityViewModels
import gr.ppzglou.food.base.BaseFragment
import gr.ppzglou.food.databinding.FragmentFingerprintSetupBinding
import gr.ppzglou.food.ui.authsetup.AuthSetupViewModel
import gr.ppzglou.food.ui.dashboard.DashboardActivity

class FingerprintSetupFragment : BaseFragment<FragmentFingerprintSetupBinding>() {

    private val viewModel: AuthSetupViewModel by activityViewModels()

    override fun getViewBinding(): FragmentFingerprintSetupBinding =
        FragmentFingerprintSetupBinding.inflate(layoutInflater)


    override fun setupListeners() {
        with(binding) {
            fingerprintBtn.setOnClickListener {
                viewModel.addFingerprintSettings()
            }
            notNowBtn.setOnClickListener {
                val intent = Intent(context, DashboardActivity::class.java)
                startActivity(intent)
                activity?.finish()
            }
        }
    }

    override fun setupObservers() {
        viewModel.addFingerprintSettings.observe(viewLifecycleOwner) {
            val intent = Intent(context, DashboardActivity::class.java)
            startActivity(intent)
            activity?.finish()
        }
    }

    override fun setupViews() {
    }

}