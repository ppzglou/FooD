package gr.ppzglou.food.ui.landing

import android.view.animation.AnimationUtils
import androidx.core.view.isVisible
import dagger.hilt.android.AndroidEntryPoint
import gr.ppzglou.food.R
import gr.ppzglou.food.base.BaseActivity
import gr.ppzglou.food.databinding.ActivityLandingBinding


@AndroidEntryPoint
class LandingActivity :
    BaseActivity<ActivityLandingBinding, LandingViewModel>(LandingViewModel::class.java) {

    override fun getViewBinding(): ActivityLandingBinding {
        return ActivityLandingBinding.inflate(layoutInflater)
    }

    override fun setupObservers() {
        with(viewModel) {
            connectivityLiveData.observe(this@LandingActivity) { event ->
                event.getContentIfNotHandled()?.let(this@LandingActivity::connectivityChange)
            }

            connectivityUI.observe(this@LandingActivity) { event ->
                event.getContentIfNotHandled()?.let(this@LandingActivity::connectivityChange)
            }

            load.observe(this@LandingActivity) { event ->
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
    }

    override fun onBackPressed() {
        if (!binding.progress.isVisible)
            super.onBackPressed()
    }

    override fun setupListeners() {

    }
}