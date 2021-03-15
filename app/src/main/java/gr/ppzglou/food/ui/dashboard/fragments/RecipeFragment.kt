package gr.ppzglou.food.ui.dashboard.fragments

import android.content.Intent
import android.net.Uri
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.navArgs
import gr.ppzglou.food.base.BaseFragment
import gr.ppzglou.food.databinding.FragmentRecipeBinding
import gr.ppzglou.food.ext.setPic
import gr.ppzglou.food.ui.dashboard.DashboardViewModel


class RecipeFragment : BaseFragment<FragmentRecipeBinding>() {

    private val viewModel: DashboardViewModel by activityViewModels()
    private val args: RecipeFragmentArgs by navArgs()
    private val ingredientLinesAdapter = IngredientLinesAdapter()

    var shareUrl = ""
    var url = ""


    override fun getViewBinding(): FragmentRecipeBinding =
        FragmentRecipeBinding.inflate(layoutInflater)


    override fun setupListeners() {
        binding.share.setOnClickListener {
            share()
        }
        binding.link.setOnClickListener {
            openBrowser()
        }
    }

    override fun setupObservers() {
        viewModel.successRecipe.observe(this, {
            with(binding) {
                title.text = it.label
                it.image?.let { it1 -> binding.img.setPic(it1) }
                ingredientLinesAdapter.submitList(it.ingredientLines)
                recyclerView.adapter?.notifyDataSetChanged()
                shareUrl = it.shareAs.toString()
                url = it.url.toString()
                type.text = "${it.cuisineType?.get(0)}, ${it.dishType?.get(0)}"
                calories.text = "${it.calories?.toInt()} kcal"
            }
        })
    }

    override fun setupViews() {
        viewModel.recipe(args.recipeUri)
        binding.recyclerView.adapter = ingredientLinesAdapter
    }

    private fun share() {
        val intent = Intent()
        intent.action = Intent.ACTION_SEND

        intent.type = "text/plain"
        intent.putExtra(Intent.EXTRA_TEXT, shareUrl)
        startActivity(Intent.createChooser(intent, "Share"))
    }

    private fun openBrowser() {
        val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        startActivity(browserIntent)
    }

}