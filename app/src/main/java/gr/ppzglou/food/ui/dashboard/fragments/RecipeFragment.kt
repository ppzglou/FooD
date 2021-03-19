package gr.ppzglou.food.ui.dashboard.fragments

import android.content.Intent
import android.net.Uri
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.navArgs
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexboxLayoutManager
import com.google.android.flexbox.JustifyContent
import gr.ppzglou.food.base.BaseFragment
import gr.ppzglou.food.databinding.FragmentRecipeBinding
import gr.ppzglou.food.ext.setPic
import gr.ppzglou.food.ui.dashboard.DashboardViewModel


class RecipeFragment : BaseFragment<FragmentRecipeBinding>() {

    private val viewModel: DashboardViewModel by activityViewModels()
    private val args: RecipeFragmentArgs by navArgs()
    private val ingredientLinesAdapter = IngredientLinesAdapter()
    private val cuisineAdapter = TagsAdapter()
    private val mealAdapter = TagsAdapter()
    private val tagsAdapter = TagsAdapter()

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
                shareUrl = it.shareAs.toString()
                url = it.url.toString()
                calories.text = "${it.calories?.toInt()} kcal"

                ingredientLinesAdapter.submitList(it.ingredientLines)
                cuisineAdapter.submitList(it.cuisineType)
                mealAdapter.submitList(it.mealType)
                tagsAdapter.submitList(it.healthLabels)
            }
        })
    }

    override fun setupViews() {
        viewModel.recipe(args.recipeUri)

        with(binding) {
            val layoutManagerCuisine = FlexboxLayoutManager(context)
            layoutManagerCuisine.flexDirection = FlexDirection.ROW
            layoutManagerCuisine.justifyContent = JustifyContent.FLEX_START

            val layoutManagerMeal = FlexboxLayoutManager(context)
            layoutManagerMeal.flexDirection = FlexDirection.ROW
            layoutManagerMeal.justifyContent = JustifyContent.FLEX_START

            val layoutManagerTags = FlexboxLayoutManager(context)
            layoutManagerTags.flexDirection = FlexDirection.ROW
            layoutManagerTags.justifyContent = JustifyContent.FLEX_START

            recyclerViewCuisine.layoutManager = layoutManagerCuisine
            recyclerViewMeal.layoutManager = layoutManagerMeal
            recyclerViewTags.layoutManager = layoutManagerTags

            recyclerViewIngredients.adapter = ingredientLinesAdapter
            recyclerViewCuisine.adapter = cuisineAdapter
            recyclerViewMeal.adapter = mealAdapter
            recyclerViewTags.adapter = tagsAdapter

            recyclerViewIngredients.adapter?.notifyDataSetChanged()
            recyclerViewCuisine.adapter?.notifyDataSetChanged()
            recyclerViewMeal.adapter?.notifyDataSetChanged()
            recyclerViewTags.adapter?.notifyDataSetChanged()
        }
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