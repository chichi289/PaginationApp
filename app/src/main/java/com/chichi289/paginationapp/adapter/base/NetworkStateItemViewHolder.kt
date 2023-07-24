package com.chichi289.paginationapp.adapter.base

import androidx.core.view.isVisible
import androidx.paging.LoadState
import androidx.recyclerview.widget.RecyclerView
import com.chichi289.paginationapp.databinding.LayoutNetworkStateItemBinding
import com.chichi289.paginationapp.extentions.visibleIf

class NetworkStateItemViewHolder(
    binding: LayoutNetworkStateItemBinding,
    private val retryCallback: () -> Unit
) : RecyclerView.ViewHolder(binding.root) {

    private val progressBar = binding.progressView
    private val errorMessage = binding.textViewError
    private val retry = binding.buttonRetry.also {
        it.setOnClickListener {
            retryCallback.invoke()
        }
    }

    fun bind(loadState: LoadState) {
        progressBar.isVisible = loadState is LoadState.Loading
        retry.visibleIf(loadState is LoadState.Error)
        errorMessage.isVisible = !(loadState as? LoadState.Error)?.error?.message.isNullOrBlank()
        errorMessage.text = (loadState as? LoadState.Error)?.error?.message
    }

}