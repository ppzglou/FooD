package gr.ppzglou.food.ui.dashboard.fragments.settings

import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import com.google.android.material.radiobutton.MaterialRadioButton
import gr.ppzglou.food.R
import gr.ppzglou.food.base.BaseFragment
import gr.ppzglou.food.databinding.FragmentDeleteAccountBinding
import gr.ppzglou.food.ext.changeButtonEnable
import gr.ppzglou.food.ui.dashboard.DashboardViewModel


class DeleteAccountFragment : BaseFragment<FragmentDeleteAccountBinding>() {

    private val viewModel: DashboardViewModel by activityViewModels()
    private var deleteDescription = ""

    override fun getViewBinding(): FragmentDeleteAccountBinding =
        FragmentDeleteAccountBinding.inflate(layoutInflater)


    override fun setupListeners() {
        with(binding) {
            /*deleteBtn.setOnClickListener {
                if (radio6.isChecked) {
                    if (description.isNotEmpty() && password.isNotEmpty()) {


                    }
                } else if (isChecked() && password.isNotEmpty())
                    viewModel.delete(password.editText?.text.toString(), deleteDescription)
            }*/
            radio1.setOnClickListener { radioListener(radio1) }
            radio2.setOnClickListener { radioListener(radio2) }
            radio3.setOnClickListener { radioListener(radio3) }
            radio4.setOnClickListener { radioListener(radio4) }
            radio5.setOnClickListener { radioListener(radio5) }
            radio6.setOnClickListener {
                radioListener(radio6)
                description.requestFocus()
            }
        }
    }

    override fun setupObservers() {
        /* viewModel.successDeleted.observe(viewLifecycleOwner) {
             activity?.foodToast("epitixis diagrafi")
             val intent = Intent(context, LandingActivity::class.java)
             startActivity(intent)
             activity?.finish()
         }*/
    }

    override fun setupViews() {
    }

    private fun radioListener(radio: MaterialRadioButton) {
        with(binding) {
            val radios = mutableListOf(radio1, radio2, radio3, radio4, radio5, radio6)
            radio.changeButtonEnable(radios)
            enableDescription(radio6.isChecked)
            when (radio) {
                radio1 -> deleteDescription = getString(R.string.delete_desc_1)
                radio2 -> deleteDescription = getString(R.string.delete_desc_2)
                radio3 -> deleteDescription = getString(R.string.delete_desc_3)
                radio4 -> deleteDescription = getString(R.string.delete_desc_4)
                radio5 -> deleteDescription = getString(R.string.delete_desc_5)
            }
            message.setTextColor(
                ContextCompat.getColor(
                    binding.message.context,
                    R.color.colorBlack
                )
            )
            description.error = null
        }
    }

    private fun isChecked(): Boolean {
        if (deleteDescription == "") {
            binding.message.setTextColor(
                ContextCompat.getColor(
                    binding.message.context,
                    R.color.colorPrimaryDark
                )
            )
            return false
        }
        return true
    }

    private fun enableDescription(enable: Boolean) {
        binding.description.isVisible = enable
        binding.radioTv6.isVisible = !enable
    }


}