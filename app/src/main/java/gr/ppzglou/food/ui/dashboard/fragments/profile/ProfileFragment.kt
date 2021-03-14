package gr.ppzglou.food.ui.dashboard.fragments.profile

import androidx.fragment.app.activityViewModels
import androidx.navigation.NavDirections
import androidx.navigation.fragment.findNavController
import gr.ppzglou.food.R
import gr.ppzglou.food.base.BaseFragment
import gr.ppzglou.food.databinding.FragmentProfileBinding
import gr.ppzglou.food.ext.safeNavigate
import gr.ppzglou.food.ui.dashboard.DashboardViewModel

class ProfileFragment : BaseFragment<FragmentProfileBinding>(), ProfileAdapter.OnItemClickListener {

    private val viewModel: DashboardViewModel by activityViewModels()
    private val profileAdapter = ProfileAdapter(this)

    override fun getViewBinding(): FragmentProfileBinding =
        FragmentProfileBinding.inflate(layoutInflater)


    override fun setupListeners() {

    }

    override fun setupObservers() {

    }

    override fun setupViews() {
        profileAdapter.submitList(viewModel.getMenu())
        binding.recyclerView.adapter = profileAdapter
    }

    override fun onMenuItemClick(nav: NavDirections) {
        findNavController().safeNavigate(
            nav,
            R.id.nav_profile
        )
    }


}