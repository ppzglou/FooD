package gr.ppzglou.food.ui.dashboard.fragments.recipe

import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import gr.ppzglou.food.R
import gr.ppzglou.food.base.BaseFragment
import gr.ppzglou.food.databinding.FragmentFavBinding
import gr.ppzglou.food.ext.safeNavigate
import gr.ppzglou.food.ui.dashboard.DashboardViewModel


class FavFragment : BaseFragment<FragmentFavBinding>(), SearchAdapter.OnItemClickListener {

    private val viewModel: DashboardViewModel by activityViewModels()
    private val searchAdapter = SearchAdapter(this)

    override fun getViewBinding(): FragmentFavBinding =
        FragmentFavBinding.inflate(layoutInflater)

    override fun setupObservers() {
        with(viewModel) {
            successFetchFavs.observe(this@FavFragment, {
                recipeList(it)
            })
            successRecipeList.observe(this@FavFragment, {
                searchAdapter.submitList(it)
                with(binding) {
                    recyclerView.apply {
                        adapter?.notifyDataSetChanged()
                        isVisible = it.isNotEmpty()
                    }
                }
            })
        }
    }

    override fun setupViews() {
        viewModel.fetchFavsDao()
        binding.recyclerView.adapter = searchAdapter
    }

    override fun setupListeners() {
    }


    override fun onItemClick(uri: String) {
        findNavController().safeNavigate(
            FavFragmentDirections.actionNavFavToNavRecipe(
                recipeUri = uri
            ),
            R.id.nav_fav
        )
    }


}