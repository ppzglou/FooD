package gr.ppzglou.food.ui.dashboard.fragments.recipe

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import gr.ppzglou.food.databinding.ItemTagBinding
import gr.ppzglou.food.util.STRINGS_DIFF_UTIL

class TagsAdapter :
    ListAdapter<String, TagsAdapter.ViewHolder>(STRINGS_DIFF_UTIL) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemViewBinding = ItemTagBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(itemViewBinding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        return holder.bind(getItem(position))
    }

    inner class ViewHolder(private val itemViewBinding: ItemTagBinding) :
        RecyclerView.ViewHolder(itemViewBinding.root) {

        fun bind(item: String) {
            with(itemViewBinding) {
                tag.text = item
            }
        }
    }
}