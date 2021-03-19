package gr.ppzglou.food.ui.dashboard.fragments

import android.app.AlertDialog
import androidx.fragment.app.activityViewModels
import gr.ppzglou.food.base.BaseFragment
import gr.ppzglou.food.databinding.FragmentBmrBinding
import gr.ppzglou.food.ext.foodToast
import gr.ppzglou.food.ext.isNotEmpty
import gr.ppzglou.food.ui.dashboard.DashboardViewModel


class BmrFragment : BaseFragment<FragmentBmrBinding>() {

    private val viewModel: DashboardViewModel by activityViewModels()
    private var activityChoice = -1
    private var sexChoice = -1


    override fun getViewBinding(): FragmentBmrBinding =
        FragmentBmrBinding.inflate(layoutInflater)

    override fun setupObservers() {
        viewModel.successCalcBmr.observe(viewLifecycleOwner, {
            with(binding) {
                bmr.text = "Your basal metabolic rate (BMR) is: ${it.bmr} Kcal"
                daily.text = "Daily caloric needs: ${it.daily} Kcal"
            }
        })
    }

    override fun setupViews() {
        with(binding) {
            actvt.editText?.setOnClickListener {
                dialog()
            }
            calcBtn.setOnClickListener {
                if (validate()) {
                    viewModel.calcBMR(
                        sexChoice,
                        height.editText?.text.toString().toInt(),
                        weight.editText?.text.toString().toDouble(),
                        age.editText?.text.toString().toInt(),
                        activityChoice
                    )
                } else activity?.foodToast("complete all fields!")
            }
            radioGroup.setOnCheckedChangeListener { _, _ ->
                sexChoice = if (male.isChecked) 0
                else 1
            }
        }
    }

    override fun setupListeners() {
    }

    private fun dialog() {
        val builder = AlertDialog.Builder(context)
        builder.setTitle("Choose activity")

        val animals = arrayOf(
            "1-2 per week",
            "3 per week",
            "3-5 per week",
            "daily",
            "extreme activity"
        )

        builder.setSingleChoiceItems(animals, -1) { dialog, which ->
            binding.actvt.editText?.setText(animals[which])
            activityChoice = which
            dialog.dismiss()
        }

        val dialog = builder.create()
        dialog.show()
    }

    private fun validate(): Boolean {
        var flag = true
        with(binding) {
            val sex = sexChoice == -1
            val height = !height.isNotEmpty()
            val weight = !weight.isNotEmpty()
            val age = !age.isNotEmpty()
            val act = activityChoice == -1

            if (sex || height || weight || age || act) flag = false

            return flag
        }
    }

}