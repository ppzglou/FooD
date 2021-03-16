package gr.ppzglou.food.ui.dashboard.fragments.settings

import android.view.animation.AnimationUtils
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import gr.ppzglou.food.R
import gr.ppzglou.food.base.BaseFragment
import gr.ppzglou.food.data.models.PersonalDetailsModel
import gr.ppzglou.food.databinding.FragmentChangePersonalInfoBinding
import gr.ppzglou.food.ext.isNotEmpty
import gr.ppzglou.food.ui.stepper.StepperViewModel

class ChangePersonalInfoFragment : BaseFragment<FragmentChangePersonalInfoBinding>() {

    private val viewModel: StepperViewModel by activityViewModels()

    override fun getViewBinding(): FragmentChangePersonalInfoBinding =
        FragmentChangePersonalInfoBinding.inflate(layoutInflater)

    override fun setupObservers() {
        with(viewModel) {
            successGetName.observe(viewLifecycleOwner) {
                setDataToViews(it)
            }
            successSetName.observe(viewLifecycleOwner) {
                activity?.onBackPressed()
            }
            load.observe(viewLifecycleOwner) { event ->
                event.getContentIfNotHandled()?.let {
                    binding.progress.isVisible = it
                }
            }
        }
    }

    override fun setupViews() {
        val anim = AnimationUtils.loadAnimation(context, R.anim.rotate_anim)
        binding.loader.startAnimation(anim)

        viewModel.getName()
    }

    override fun setupListeners() {
        with(binding) {
            next.setOnClickListener {
                if (validate()) {
                    viewModel.setName(
                        name.editText?.text.toString(),
                        sname.editText?.text.toString()
                    )
                }
            }
        }
    }

    private fun setDataToViews(personalDetailsModel: PersonalDetailsModel) {
        with(binding) {
            name.editText?.setText(personalDetailsModel.name)
            sname.editText?.setText(personalDetailsModel.sname)
        }
    }

    private fun validate(): Boolean {
        var flag = true
        with(binding) {
            val name = !name.isNotEmpty()
            val sname = !sname.isNotEmpty()

            if (name || sname) flag = false

            return flag
        }
    }

}