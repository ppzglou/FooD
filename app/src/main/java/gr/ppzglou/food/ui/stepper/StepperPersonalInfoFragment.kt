package gr.ppzglou.food.ui.stepper

import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import gr.ppzglou.food.R
import gr.ppzglou.food.base.BaseFragment
import gr.ppzglou.food.data.models.PersonalDetailsModel
import gr.ppzglou.food.databinding.FragmentStepperPersonalInfoBinding
import gr.ppzglou.food.ext.isNotEmpty
import gr.ppzglou.food.ext.safeNavigateOnBack

class StepperPersonalInfoFragment : BaseFragment<FragmentStepperPersonalInfoBinding>() {

    private val viewModel: StepperViewModel by activityViewModels()

    override fun getViewBinding(): FragmentStepperPersonalInfoBinding =
        FragmentStepperPersonalInfoBinding.inflate(layoutInflater)

    override fun setupObservers() {
        viewModel.successGetName.observe(viewLifecycleOwner) {
            setDataToViews(it)
        }
        viewModel.successSetName.observe(viewLifecycleOwner) {
            activity?.finish()
        }
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
            back.setOnClickListener {
                findNavController().safeNavigateOnBack(
                    StepperPersonalInfoFragmentDirections.actionNavStepperPersonalInfoToNavStepperPhone(),
                    R.id.nav_stepper_personal_info
                )
            }
        }
    }

    override fun setupViews() {
        viewModel.getName()
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