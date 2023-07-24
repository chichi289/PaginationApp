package com.chichi289.paginationapp.viewmodel.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.chichi289.paginationapp.api.DummyApi
import com.chichi289.paginationapp.db.UserDao
import com.chichi289.paginationapp.viewmodel.PagingViewModel

class PagingViewModelFactory(
    private val userDao: UserDao,
    private val dummyApi: DummyApi
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(PagingViewModel::class.java)) {
            return PagingViewModel(dummyApi, userDao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}