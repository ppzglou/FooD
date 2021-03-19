package gr.ppzglou.food.ui.dashboard.fragments

import android.view.animation.AnimationUtils
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import gr.ppzglou.food.R
import gr.ppzglou.food.base.BaseFragment
import gr.ppzglou.food.databinding.FragmentSearchBinding
import gr.ppzglou.food.ext.safeNavigate
import gr.ppzglou.food.ui.dashboard.DashboardViewModel


class SearchFragment : BaseFragment<FragmentSearchBinding>(), SearchAdapter.OnItemClickListener {

    private val viewModel: DashboardViewModel by activityViewModels()
    private val searchAdapter = SearchAdapter(this)

    override fun getViewBinding(): FragmentSearchBinding =
        FragmentSearchBinding.inflate(layoutInflater)


    override fun setupListeners() {

    }

    override fun setupObservers() {
        viewModel.successSearch.observe(this, {
            searchAdapter.submitList(it)
            with(binding) {
                recyclerView.adapter?.notifyDataSetChanged()
                recyclerView.isVisible = it.isNotEmpty()
            }
        })
        viewModel.loadSearch.observe(viewLifecycleOwner, { event ->
            event.getContentIfNotHandled()?.let {
                binding.progress.isVisible = it
            }
        })
    }

    override fun setupViews() {
        binding.search.editText?.addTextChangedListener {
            viewModel.search(binding.search.editText?.text.toString())
        }

        binding.recyclerView.adapter = searchAdapter

        binding.recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (!recyclerView.canScrollVertically(1)) {
                    viewModel.updateSearchData(binding.search.editText?.text.toString())
                }
            }
        })

        val anim = AnimationUtils.loadAnimation(context, R.anim.rotate_anim)
        binding.loader.startAnimation(anim)
    }

    override fun onItemClick(uri: String) {
        findNavController().safeNavigate(
            SearchFragmentDirections.actionNavSearchToNavRecipe(
                recipeUri = uri
            ),
            R.id.nav_search
        )
    }


}