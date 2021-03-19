package gr.ppzglou.food.ui.dashboard.fragments.recipe

import android.content.Intent
import android.net.Uri
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.navArgs
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexboxLayoutManager
import com.google.android.flexbox.JustifyContent
import gr.ppzglou.food.R
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
    var isFav = false


    override fun getViewBinding(): FragmentRecipeBinding =
        FragmentRecipeBinding.inflate(layoutInflater)

    override fun setupObservers() {
        with(viewModel) {
            successRecipe.observe(this@RecipeFragment, {
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
            fetchDaoIsFav.observe(this@RecipeFragment, {
                isFav = it
                with(binding) {
                    if (isFav) fav.setPic(R.drawable.ic_heart)
                    else fav.setPic(R.drawable.ic_heart_empty)
                }
            })
            successAddFav.observe(this@RecipeFragment, {
                with(binding) {
                    fav.setPic(R.drawable.ic_heart)
                }
            })
            successDelFav.observe(this@RecipeFragment, {
                with(binding) {
                    fav.setPic(R.drawable.ic_heart_empty)
                }
            })
        }
    }

    override fun setupViews() {
        with(viewModel) {
            recipe(args.recipeUri)
            isFav(args.recipeUri)
        }

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

    override fun setupListeners() {
        with(binding) {
            share.setOnClickListener {
                share()
            }
            link.setOnClickListener {
                openBrowser()
            }
            fav.setOnClickListener {
                addDellFav()
            }
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

    private fun addDellFav() {
        with(viewModel) {
            if (isFav) deleteDaoFav(args.recipeUri)
            else addDaoFav(args.recipeUri)
        }
    }

}