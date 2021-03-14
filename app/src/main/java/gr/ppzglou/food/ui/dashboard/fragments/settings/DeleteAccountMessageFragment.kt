package gr.ppzglou.food.ui.dashboard.fragments.settings


import androidx.navigation.fragment.findNavController
import gr.ppzglou.food.R
import gr.ppzglou.food.base.BaseFragment
import gr.ppzglou.food.databinding.FragmentDeleteAccountMessageBinding
import gr.ppzglou.food.ext.safeNavigate
import gr.ppzglou.food.ext.setPic

class DeleteAccountMessageFragment : BaseFragment<FragmentDeleteAccountMessageBinding>() {

    override fun getViewBinding(): FragmentDeleteAccountMessageBinding =
        FragmentDeleteAccountMessageBinding.inflate(layoutInflater)


    override fun setupListeners() {
        with(binding) {
            heartGif.setPic(R.mipmap.broken_heart)
            deleteBtn.setOnClickListener {
                findNavController().safeNavigate(
                    DeleteAccountMessageFragmentDirections.actionNavDeleteAccountMessageToNavDeleteAccount(),
                    R.id.nav_delete_account_message
                )
            }
            backBtn.setOnClickListener {
                activity?.onBackPressed()
            }
        }
    }

    override fun setupObservers() {}

    override fun setupViews() {}

}