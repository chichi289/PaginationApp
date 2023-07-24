package com.chichi289.paginationapp.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingData
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.chichi289.paginationapp.R
import com.chichi289.paginationapp.databinding.ItemUserBinding
import com.chichi289.paginationapp.databinding.ItemUserGridBinding
import com.chichi289.paginationapp.extentions.loadImage
import com.chichi289.paginationapp.models.User
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class UserPagingAdapter(
    private val isGrid: Boolean
) : PagingDataAdapter<User, RecyclerView.ViewHolder>(USER_COMPARATOR) {

    fun getItemAt(index: Int): User = snapshot().items.toMutableList()[index]

    fun updateItem(index: Int) {
        getItem(index)?.let {
            it.firstName = "Chirag"
            it.lastName = "Prajapati"
            it.picture = "https://avatars.githubusercontent.com/u/20377949?v=4"
        }
        notifyItemChanged(index)
    }

    fun removeItem(index: Int) {
        snapshot().items.toMutableList().removeAt(index)
        notifyItemRemoved(index)
    }

    @Suppress("unused")
    fun addItem(user: User, index: Int) {
        val list = snapshot().items.toMutableList()
        val newList = ArrayList<User>().apply {
            addAll(list)
            add(index, user)
        }
        CoroutineScope(Dispatchers.Main).launch {
            submitData(PagingData.from(newList))
        }
        notifyItemInserted(index)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            ViewType.ITEM.value -> {
                ViewHolder(
                    ItemUserBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    )
                )
            }
            ViewType.GRID.value -> {
                GridViewHolder(
                    ItemUserGridBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    )
                )
            }
            else -> throw IllegalArgumentException("Invalid viewType $viewType")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        getItem(position)?.let {
            when (holder) {
                is ViewHolder -> holder.bind(it)
                is GridViewHolder -> holder.bind(it)
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (isGrid) ViewType.GRID.value else ViewType.ITEM.value
    }

    inner class ViewHolder(
        private val binding: ItemUserBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(dataBean: User) {
            binding.apply {
                imgUser.loadImage(value = dataBean.picture, roundRadius = 8f)
                tvUserName.text = tvUserName.context.getString(
                    R.string.txt_full_name,
                    dataBean.firstName, dataBean.lastName
                )
            }
        }
    }

    inner class GridViewHolder(
        private val binding: ItemUserGridBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(dataBean: User) {
            binding.apply {
                imgUser.loadImage(value = dataBean.picture, roundRadius = 4f)
                tvUserName.text = tvUserName.context.getString(
                    R.string.txt_full_name,
                    dataBean.firstName, dataBean.lastName
                )
            }
        }
    }

    companion object {

        private enum class ViewType(val value: Int) {
            ITEM(0), GRID(1)
        }

        private val USER_COMPARATOR = object : DiffUtil.ItemCallback<User>() {
            override fun areItemsTheSame(oldItem: User, newItem: User): Boolean =
                oldItem.id == newItem.id

            override fun areContentsTheSame(oldItem: User, newItem: User): Boolean =
                oldItem == newItem
        }
    }

}