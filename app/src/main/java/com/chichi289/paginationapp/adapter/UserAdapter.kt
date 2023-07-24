package com.chichi289.paginationapp.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.chichi289.paginationapp.R
import com.chichi289.paginationapp.databinding.ItemLoadingBinding
import com.chichi289.paginationapp.databinding.ItemUserBinding
import com.chichi289.paginationapp.extentions.loadImage
import com.chichi289.paginationapp.extentions.visible
import com.chichi289.paginationapp.models.User

class UserAdapter(
    private val mList: ArrayList<User>
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

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
            ViewType.LOADING.value -> {
                LoadingViewHolder(
                    ItemLoadingBinding.inflate(
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
        when (holder) {
            is ViewHolder -> {
                holder.bind(mList[position])
            }
            is LoadingViewHolder -> {
                holder.bind()
            }
        }
    }

    override fun getItemCount(): Int = mList.size

    override fun getItemViewType(position: Int): Int {
        return if (position == mList.size - 1 && isLoadingAdded) {
            ViewType.LOADING.value
        } else {
            ViewType.ITEM.value
        }
    }

    fun addLoadingFooter() {
        isLoadingAdded = true
    }

    fun removeLoadingFooter() {
        isLoadingAdded = false
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

    inner class LoadingViewHolder(
        private val binding: ItemLoadingBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind() {
            binding.apply {
                progressBar.visible()
            }
        }
    }

    companion object {
        private enum class ViewType(val value: Int) {
            LOADING(0),
            ITEM(1)
        }

        private var isLoadingAdded = false
    }
}