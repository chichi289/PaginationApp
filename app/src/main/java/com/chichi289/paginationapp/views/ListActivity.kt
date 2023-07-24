package com.chichi289.paginationapp.views

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.chichi289.paginationapp.adapter.UserAdapter
import com.chichi289.paginationapp.api.ApiModule
import com.chichi289.paginationapp.databinding.ActivityListBinding
import com.chichi289.paginationapp.models.Resource
import com.chichi289.paginationapp.models.User
import com.chichi289.paginationapp.models.Users
import com.chichi289.paginationapp.utils.EndlessRecyclerViewScrollListener
import com.chichi289.paginationapp.viewmodel.ListViewModel
import com.chichi289.paginationapp.viewmodel.factory.ListViewModelFactory

class ListActivity : AppCompatActivity() {

    private val factory: ListViewModelFactory by lazy {
        ListViewModelFactory(ApiModule.provideDummyRepository())
    }

    private val viewModel: ListViewModel by lazy {
        ViewModelProvider(this, factory)[ListViewModel::class.java]
    }

    private lateinit var binding: ActivityListBinding

    private val users = ArrayList<User>()

    private val userAdapter: UserAdapter = UserAdapter(users)

    private var mPage = 1

    @SuppressLint("NotifyDataSetChanged")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.recyclerView.apply {
            adapter = userAdapter
            addOnScrollListener(object :
                EndlessRecyclerViewScrollListener(layoutManager as LinearLayoutManager) {
                override fun onLoadMore(page: Int, totalItemsCount: Int, view: RecyclerView?) {
                    userAdapter.addLoadingFooter()
                    mPage += 1
                    viewModel.getUsers(mPage)
                }
            })
        }

        viewModel.usersLiveData.observe(this) {
            when (it) {
                is Resource.Success<*> -> {
                    userAdapter.removeLoadingFooter()
                    val list = it.data as Users
                    users.addAll(list.users)
                    userAdapter.notifyDataSetChanged()
                }
                is Resource.Error<*> -> {
                    Toast.makeText(this, it.message, Toast.LENGTH_SHORT).show()
                }
            }
        }

        viewModel.getUsers(mPage)
    }

    companion object {
        fun start(context: Context) = context.startActivity(
            Intent(context, ListActivity::class.java)
        )
    }
}