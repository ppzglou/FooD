package gr.ppzglou.food.ui.dashboard

import android.view.animation.AnimationUtils
import androidx.core.view.isVisible
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import dagger.hilt.android.AndroidEntryPoint
import gr.ppzglou.food.R
import gr.ppzglou.food.base.BaseActivity
import gr.ppzglou.food.databinding.ActivityDashboardBinding


@AndroidEntryPoint
class DashboardActivity :
    BaseActivity<ActivityDashboardBinding, DashboardViewModel>(DashboardViewModel::class.java) {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var navController: NavController

    override fun getViewBinding(): ActivityDashboardBinding {
        return ActivityDashboardBinding.inflate(layoutInflater)
    }

    override fun setupObservers() {
        with(viewModel) {
            connectivityLiveData.observe(this@DashboardActivity) { event ->
                event.getContentIfNotHandled()?.let(this@DashboardActivity::connectivityChange)
            }

            connectivityUI.observe(this@DashboardActivity) { event ->
                event.getContentIfNotHandled()?.let(this@DashboardActivity::connectivityChange)
            }

            load.observe(this@DashboardActivity) { event ->
                event.getContentIfNotHandled()?.let {
                    binding.progress.isVisible = it
                }
            }

            checkConnectivity()
        }
    }

    override fun setupViews() {
        navController = findNavController(R.id.nav_host_fragment)
        appBarConfiguration = AppBarConfiguration(navController.graph)
        binding.navView.setupWithNavController(navController)

        binding.navView.elevation = 0f
        binding.navView.setOnNavigationItemReselectedListener {}

        val menuList = mutableListOf(
            R.id.nav_profile,
            R.id.nav_settings
        )

        navController.addOnDestinationChangedListener { _, destination, _ ->
            var isOnMenu = false

            for (id in menuList) if (id == destination.id) {
                isOnMenu = true
                break
            }
            binding.navView.isVisible = isOnMenu

        }

        val anim = AnimationUtils.loadAnimation(this, R.anim.rotate_anim)
        binding.loader.startAnimation(anim)
    }

    override fun setupListeners() {
    }

}
