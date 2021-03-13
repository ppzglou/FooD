package gr.ppzglou.food.ui.splash

import android.content.Intent
import android.view.animation.AnimationUtils
import androidx.lifecycle.Observer
import dagger.hilt.android.AndroidEntryPoint
import gr.ppzglou.food.R
import gr.ppzglou.food.base.BaseActivity
import gr.ppzglou.food.databinding.ActivitySplashBinding
import gr.ppzglou.food.ui.landing.LandingActivity

@AndroidEntryPoint
class SplashActivity :
        BaseActivity<ActivitySplashBinding, SplashViewModel>(SplashViewModel::class.java) {

    override fun getViewBinding(): ActivitySplashBinding {
        return ActivitySplashBinding.inflate(layoutInflater)
    }

    override fun setupObservers() {
        with(viewModel) {
            navigateToLoginScreen.observe(this@SplashActivity, Observer{
                showLoginScreen()
            })
            navigateToAuthenticationScreen.observe(this@SplashActivity, Observer{
                viewModel.fetchDaoUserPin()
            })
           /* fetchDaoUserPin.observe(this@SplashActivity, Observer{
                //showAuthenticationScreen()
            })*/
            fetchDaoUserPinError.observe(this@SplashActivity, Observer{
                showSetupAuthScreen()
            })
            connectivityLiveData.observe(this@SplashActivity, Observer{ event ->
                event.getContentIfNotHandled()?.let(this@SplashActivity::connectivityChange)
            })
        }
    }

    override fun setupViews() {
        logoAnimate()
        viewModel.checkUserExistence()
    }

    private fun logoAnimate() {
        val anim1 = AnimationUtils.loadAnimation(this, R.anim.f_anim)
        val anim2 = AnimationUtils.loadAnimation(this, R.anim.o1_anim)
        val anim3 = AnimationUtils.loadAnimation(this, R.anim.o2_anim)
        val anim4 = AnimationUtils.loadAnimation(this, R.anim.d_anim)


        with(binding) {
            f.startAnimation(anim1)
            o1.startAnimation(anim2)
            o2.startAnimation(anim3)
            d.startAnimation(anim4)


        }
    }

    private fun showLoginScreen() {
        val intent = Intent(this, LandingActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun showAuthenticationScreen() {
        /*val intent = Intent(this, AuthenticationActivity::class.java)
        startActivity(intent)
        finish()*/
    }

    private fun showSetupAuthScreen() {
       /* val intent = Intent(this, AuthSetupActivity::class.java)
        startActivity(intent)
        finish()*/
    }

    override fun setupListeners() {}
}