package com.chichi289.paginationapp.views

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.paging.PagingData
import com.chichi289.paginationapp.PaginationApp
import com.chichi289.paginationapp.R
import com.chichi289.paginationapp.adapter.UserPagingAdapter
import com.chichi289.paginationapp.adapter.base.BaseLoadStateAdapter
import com.chichi289.paginationapp.api.ApiModule
import com.chichi289.paginationapp.databinding.ActivityPagingListDbBinding
import com.chichi289.paginationapp.extentions.visibleIf
import com.chichi289.paginationapp.extentions.whenStarted
import com.chichi289.paginationapp.viewmodel.PagingViewModel
import com.chichi289.paginationapp.viewmodel.factory.PagingViewModelFactory
import kotlinx.coroutines.flow.collectLatest

class PagingListDbActivity : AppCompatActivity() {

    private val factory: PagingViewModelFactory by lazy {
        PagingViewModelFactory(
            userDao = (application as PaginationApp).userDao,
            dummyApi = ApiModule.provideDummyApi()
        )
    }

    private val viewModel: PagingViewModel by lazy {
        ViewModelProvider(this, factory)[PagingViewModel::class.java]
    }

    private lateinit var binding: ActivityPagingListDbBinding

    private val userAdapter: UserPagingAdapter by lazy { UserPagingAdapter(false) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPagingListDbBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val footerAdapter = BaseLoadStateAdapter(userAdapter)
        binding.recyclerView.adapter = userAdapter.withLoadStateFooter(footerAdapter)

        whenStarted {
            viewModel.getUsersFromDb().collectLatest {
                userAdapter.submitData(PagingData.from(it))
                binding.emptyView.root.visibleIf(userAdapter.itemCount == 0)
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_db, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_add -> {
                viewModel.addUser(PaginationApp.getUser())
                return true
            }

            R.id.menu_remove -> {
                if (userAdapter.itemCount > 0) {
                    viewModel.removeUserFromDb(
                        userAdapter.getItemAt(0)
                    )
                }
                return true
            }

            R.id.menu_clear_db -> {
                viewModel.clearDb()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    companion object {

        fun start(context: Context) = context.startActivity(
            Intent(context, PagingListDbActivity::class.java)
        )
    }
}