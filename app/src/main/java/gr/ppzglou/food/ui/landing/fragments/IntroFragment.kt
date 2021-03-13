package gr.ppzglou.food.ui.landing.fragments

import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import gr.ppzglou.food.ui.landing.LandingViewModel
import gr.ppzglou.food.R
import gr.ppzglou.food.base.BaseFragment
import gr.ppzglou.food.databinding.FragmentIntroBinding
import gr.ppzglou.food.ext.safeNavigate

class IntroFragment : BaseFragment<FragmentIntroBinding>() {

    private val viewModel: LandingViewModel by activityViewModels()

    override fun getViewBinding(): FragmentIntroBinding =
        FragmentIntroBinding.inflate(layoutInflater)


    override fun setupListeners() {
        with(binding) {
            loginBtn.setOnClickListener {
                findNavController().safeNavigate(
                    IntroFragmentDirections.actionNavIntroToNavLogin(),
                    R.id.nav_intro
                )
            }
            signupBtn.setOnClickListener {
                findNavController().safeNavigate(
                    IntroFragmentDirections.actionNavIntroToNavRegister(),
                    R.id.nav_intro
                )
            }
        }
    }

    override fun setupObservers() {
    }

    override fun setupViews() {
    }

}