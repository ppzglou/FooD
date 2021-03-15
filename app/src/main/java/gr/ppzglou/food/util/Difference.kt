package gr.ppzglou.food.util

import androidx.recyclerview.widget.DiffUtil
import gr.ppzglou.food.framework.Hits

val PROFILE_DIFF_UTIL = object : DiffUtil.ItemCallback<MenuButton>() {
    override fun areItemsTheSame(oldItem: MenuButton, newItem: MenuButton): Boolean =
        oldItem.title == newItem.title

    override fun areContentsTheSame(oldItem: MenuButton, newItem: MenuButton): Boolean =
        oldItem == newItem
}


val SEARCH_DIFF_UTIL = object : DiffUtil.ItemCallback<Hits>() {
    override fun areItemsTheSame(oldItem: Hits, newItem: Hits): Boolean =
        oldItem.recipe?.uri == newItem.recipe?.uri

    override fun areContentsTheSame(oldItem: Hits, newItem: Hits): Boolean =
        oldItem == newItem
}

