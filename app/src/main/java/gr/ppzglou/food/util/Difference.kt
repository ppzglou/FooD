package gr.ppzglou.food.util

import androidx.recyclerview.widget.DiffUtil

val PROFILE_DIFF_UTIL = object : DiffUtil.ItemCallback<MenuButton>() {
    override fun areItemsTheSame(oldItem: MenuButton, newItem: MenuButton): Boolean =
        oldItem.title == newItem.title

    override fun areContentsTheSame(oldItem: MenuButton, newItem: MenuButton): Boolean =
        oldItem == newItem
}