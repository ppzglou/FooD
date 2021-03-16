package gr.ppzglou.food.ui.stepper

import android.view.animation.AnimationUtils
import androidx.core.view.isVisible
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import dagger.hilt.android.AndroidEntryPoint
import gr.ppzglou.food.R
import gr.ppzglou.food.base.BaseActivity
import gr.ppzglou.food.databinding.ActivityStepperBinding

@AndroidEntryPoint
class StepperActivity :
    BaseActivity<ActivityStepperBinding, StepperViewModel>(StepperViewModel::class.java) {

    private lateinit var navController: NavController
    private lateinit var appBarConfiguration: AppBarConfiguration

    override fun getViewBinding(): ActivityStepperBinding {
        return ActivityStepperBinding.inflate(layoutInflater)
    }

    override fun setupObservers() {
        with(viewModel) {
            connectivityLiveData.observe(this@StepperActivity) { event ->
                event.getContentIfNotHandled()?.let(this@StepperActivity::connectivityChange)
            }

            connectivityUI.observe(this@StepperActivity) { event ->
                event.getContentIfNotHandled()?.let(this@StepperActivity::connectivityChange)
            }

            load.observe(this@StepperActivity) { event ->
                event.getContentIfNotHandled()?.let {
                    binding.progress.isVisible = it
                }
            }
            checkConnectivity()
        }
    }


    override fun setupViews() {
        val anim = AnimationUtils.loadAnimation(this, R.anim.rotate_anim)
        binding.loader.startAnimation(anim)

        navController = findNavController(R.id.stepper_nav_host_fragment)
        appBarConfiguration = AppBarConfiguration(navController.graph)

        navController.addOnDestinationChangedListener { _, destination, _ ->
            with(binding) {
                when (destination.id) {
                    R.id.nav_stepper_phone -> stepperProgress.progress = 100 / 2
                    R.id.nav_stepper_personal_info -> stepperProgress.progress = 100
                }
            }
        }
    }


    override fun setupListeners() {
        binding.close.setOnClickListener {
            this.finish()
        }
    }


    override fun onBackPressed() {}
}