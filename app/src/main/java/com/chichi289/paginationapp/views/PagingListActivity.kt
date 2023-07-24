package com.chichi289.paginationapp.views

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.chichi289.paginationapp.PaginationApp
import com.chichi289.paginationapp.R
import com.chichi289.paginationapp.adapter.UserPagingAdapter
import com.chichi289.paginationapp.adapter.base.BaseLoadStateAdapter
import com.chichi289.paginationapp.api.ApiModule
import com.chichi289.paginationapp.databinding.ActivityPagingListBinding
import com.chichi289.paginationapp.extentions.inVisible
import com.chichi289.paginationapp.extentions.visible
import com.chichi289.paginationapp.extentions.visibleIf
import com.chichi289.paginationapp.extentions.whenCreated
import com.chichi289.paginationapp.extentions.whenResumed
import com.chichi289.paginationapp.extentions.whenStarted
import com.chichi289.paginationapp.viewmodel.PagingViewModel
import com.chichi289.paginationapp.viewmodel.factory.PagingViewModelFactory
import com.facebook.shimmer.ShimmerFrameLayout
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import java.net.UnknownHostException

class PagingListActivity : AppCompatActivity() {

    private val factory: PagingViewModelFactory by lazy {
        val userDao = (application as PaginationApp).userDao
        lifecycleScope.launch(Dispatchers.IO) {
            userDao.clearDb()
        }
        PagingViewModelFactory(
            dummyApi = ApiModule.provideDummyApi(),
            userDao = userDao
        )
    }

    private val viewModel: PagingViewModel by lazy {
        ViewModelProvider(this, factory)[PagingViewModel::class.java]
    }

    private val isGrid: Boolean by lazy {
        if (intent.hasExtra(KEY_IS_GRID)) {
            intent.getBooleanExtra(KEY_IS_GRID, false)
        } else {
            false
        }
    }

    private lateinit var binding: ActivityPagingListBinding

    private val userAdapter: UserPagingAdapter by lazy { UserPagingAdapter(isGrid) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPagingListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val footerAdapter = BaseLoadStateAdapter(userAdapter)
        binding.recyclerView.adapter = userAdapter.withLoadStateFooter(footerAdapter)

        if (isGrid) {
            binding.recyclerView.layoutManager =
                GridLayoutManager(this, 2, GridLayoutManager.VERTICAL, false)
            (binding.recyclerView.layoutManager as GridLayoutManager).spanSizeLookup =
                object : GridLayoutManager.SpanSizeLookup() {
                    override fun getSpanSize(position: Int): Int {
                        return if (position == userAdapter.itemCount && footerAdapter.itemCount > 0) {
                            2
                        } else {
                            1
                        }
                    }
                }
        } else {
            binding.recyclerView.layoutManager =
                LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        }

        whenCreated {
            userAdapter.loadStateFlow.collectLatest { loadStates ->
                when (loadStates.refresh) {
                    is LoadState.Loading -> {
                        manageShimmerEffect(
                            userAdapter.itemCount == 0,
                            binding.shimmerFrameLayout,
                            binding.recyclerView
                        )
                    }

                    is LoadState.NotLoading -> {

                        binding.emptyView.root.visibleIf(
                            loadStates.append.endOfPaginationReached
                                    &&
                                    userAdapter.itemCount < 1
                        )

                        manageShimmerEffect(
                            false,
                            binding.shimmerFrameLayout,
                            binding.recyclerView
                        )
                    }

                    is LoadState.Error -> {
                        manageShimmerEffect(
                            false,
                            binding.shimmerFrameLayout,
                            binding.recyclerView
                        )
                        when ((loadStates.refresh as LoadState.Error).error) {
                            is UnknownHostException -> {}
                        }
                    }
                }
            }
        }

        whenStarted {
            viewModel.users.distinctUntilChanged().collectLatest {
                userAdapter.submitData(it)
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_update -> {
                if (userAdapter.itemCount > 1) {
                    userAdapter.updateItem(0)
                }
                return true
            }

            R.id.menu_remove -> {
                if (userAdapter.itemCount > 1) {
                    userAdapter.removeItem(1)
                }
                return true
            }

            R.id.menu_add -> {
                /*if (userAdapter.itemCount > 1) {
                    userAdapter.addItem(PaginationApp.getUser(), 2)
                }*/
                // TODO("Add item")
                return true
            }

            R.id.menu_refresh -> {
                viewModel.clearDb()
                userAdapter.refresh()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun manageShimmerEffect(
        show: Boolean,
        shimmer: ShimmerFrameLayout,
        recyclerView: ViewGroup,
    ) {
        if (show) {
            recyclerView.inVisible()
            shimmer.visible()
            shimmer.startShimmer()
        } else {
            whenResumed {
                delay(300)
                recyclerView.visible()
                shimmer.inVisible()
                shimmer.stopShimmer()
            }
        }
    }

    companion object {

        private const val KEY_IS_GRID = "is_grid"

        fun start(context: Context, isGrid: Boolean) = context.startActivity(
            Intent(context, PagingListActivity::class.java).apply {
                putExtra(KEY_IS_GRID, isGrid)
            }
        )
    }
}