package gr.ppzglou.food.ui.authsetup

import android.view.animation.AnimationUtils
import androidx.core.view.isVisible
import dagger.hilt.android.AndroidEntryPoint
import gr.ppzglou.food.R
import gr.ppzglou.food.base.BaseActivity
import gr.ppzglou.food.databinding.ActivityAuthSetupBinding

@AndroidEntryPoint
class AuthSetupActivity :
    BaseActivity<ActivityAuthSetupBinding, AuthSetupViewModel>(AuthSetupViewModel::class.java) {

    override fun getViewBinding(): ActivityAuthSetupBinding {
        return ActivityAuthSetupBinding.inflate(layoutInflater)
    }

    override fun setupObservers() {
        viewModel.load.observe(this@AuthSetupActivity, { event ->
            event.getContentIfNotHandled()?.let {
                binding.progress.isVisible = it
            }
        })
    }

    override fun setupViews() {
        val anim = AnimationUtils.loadAnimation(this, R.anim.rotate_anim)
        binding.loader.startAnimation(anim)
    }


    override fun setupListeners() {}

    override fun onBackPressed() {}
}