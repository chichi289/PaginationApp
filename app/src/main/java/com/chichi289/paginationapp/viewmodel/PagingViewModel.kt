package com.chichi289.paginationapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.chichi289.paginationapp.api.DummyApi
import com.chichi289.paginationapp.data_source.UserDataSource
import com.chichi289.paginationapp.db.UserDao
import com.chichi289.paginationapp.models.User
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class PagingViewModel(
    private val dummyApi: DummyApi,
    private val userDao: UserDao
) : ViewModel() {

    val users: Flow<PagingData<User>> = Pager(
        config = PagingConfig(
            pageSize = 10,
            maxSize = 100,
            enablePlaceholders = false
        )
    ) {
        UserDataSource(dummyApi, userDao)
    }
        .flow
        .cachedIn(viewModelScope)

    // DB
    fun getUsersFromDb() = userDao.getUsers()

    fun removeUserFromDb(user: User) {
        viewModelScope.launch(Dispatchers.IO) {
            userDao.deleteUser(user)
        }
    }

    fun addUser(user: User) {
        viewModelScope.launch(Dispatchers.IO) {
            userDao.addUser(user)
        }
    }

    fun clearDb() {
        viewModelScope.launch(Dispatchers.IO) {
            userDao.clearDb()
        }
    }

}