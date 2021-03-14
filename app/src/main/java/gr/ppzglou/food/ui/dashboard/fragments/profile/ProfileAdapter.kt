package gr.ppzglou.food.ui.dashboard.fragments.profile

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.NavDirections
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import gr.ppzglou.food.databinding.ItemProfileBinding
import gr.ppzglou.food.util.MenuButton
import gr.ppzglou.food.util.PROFILE_DIFF_UTIL


class ProfileAdapter(
    private val listener: OnItemClickListener
) : ListAdapter<MenuButton, ProfileAdapter.PointStatesViewHolder>(PROFILE_DIFF_UTIL) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PointStatesViewHolder {
        val itemViewBinding = ItemProfileBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return PointStatesViewHolder(itemViewBinding).apply {
            itemViewBinding.root.setOnClickListener {
                currentList[adapterPosition].direction?.let { it1 -> listener.onMenuItemClick(it1) }
            }
        }
    }


    override fun onBindViewHolder(holder: PointStatesViewHolder, position: Int) {
        return holder.bind(getItem(position))
    }

    inner class PointStatesViewHolder(private val itemViewBinding: ItemProfileBinding) :
        RecyclerView.ViewHolder(itemViewBinding.root) {

        fun bind(btn: MenuButton) {
            with(itemViewBinding) {
                btn.icon?.let { img.setImageResource(it) }
            }
        }
    }

    interface OnItemClickListener {
        fun onMenuItemClick(nav: NavDirections)
    }
}