package gr.ppzglou.food.ui.dashboard.fragments

import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.navArgs
import gr.ppzglou.food.base.BaseFragment
import gr.ppzglou.food.databinding.FragmentRecipeBinding
import gr.ppzglou.food.ext.setPic
import gr.ppzglou.food.ui.dashboard.DashboardViewModel


class RecipeFragment : BaseFragment<FragmentRecipeBinding>() {

    private val viewModel: DashboardViewModel by activityViewModels()
    private val args: RecipeFragmentArgs by navArgs()

    override fun getViewBinding(): FragmentRecipeBinding =
        FragmentRecipeBinding.inflate(layoutInflater)


    override fun setupListeners() {

    }

    override fun setupObservers() {
        viewModel.successRecipe.observe(this, {
            binding.title.text = it.label
            it.image?.let { it1 -> binding.img.setPic(it1) }
        })
    }

    override fun setupViews() {
        viewModel.recipe(args.recipeUri)
    }

}