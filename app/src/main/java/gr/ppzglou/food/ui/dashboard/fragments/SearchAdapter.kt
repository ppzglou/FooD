package gr.ppzglou.food.ui.dashboard.fragments

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import gr.ppzglou.food.databinding.ItemSearchBinding
import gr.ppzglou.food.ext.isNullOrEmptyOrNullString
import gr.ppzglou.food.ext.setPic
import gr.ppzglou.food.framework.Hits
import gr.ppzglou.food.util.SEARCH_DIFF_UTIL


class SearchAdapter(
    private val listener: OnItemClickListener
) : ListAdapter<Hits, SearchAdapter.SearchViewHolder>(SEARCH_DIFF_UTIL) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchViewHolder {
        val itemViewBinding = ItemSearchBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return SearchViewHolder(itemViewBinding).apply {
            itemViewBinding.root.setOnClickListener {
                currentList[adapterPosition].recipe?.uri?.let { it1 -> listener.onItemClick(it1) }
            }
        }
    }


    override fun onBindViewHolder(holder: SearchViewHolder, position: Int) {
        return holder.bind(getItem(position))
    }

    inner class SearchViewHolder(private val itemViewBinding: ItemSearchBinding) :
        RecyclerView.ViewHolder(itemViewBinding.root) {

        fun bind(item: Hits) {
            with(itemViewBinding) {
                item.recipe?.image?.let { img.setPic(it) }
                title.text = item.recipe?.label
                var kcal = item.recipe?.calories?.toInt().toString()
                if (!kcal.isNullOrEmptyOrNullString) {
                    kcal += " kcal"
                    calories.text = kcal
                }
            }
        }
    }

    interface OnItemClickListener {
        fun onItemClick(uri: String)
    }
}