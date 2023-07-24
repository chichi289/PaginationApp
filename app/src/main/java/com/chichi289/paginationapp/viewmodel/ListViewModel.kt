package com.chichi289.paginationapp.viewmodel

import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import com.chichi289.paginationapp.api.DummyRepository

class ListViewModel(
    private val dummyRepository: DummyRepository
) : ViewModel() {
    val usersLiveData: MediatorLiveData<Any> by lazy { MediatorLiveData() }
    fun getUsers(page: Int) {
        usersLiveData.addSource(
            dummyRepository.getUsers(page)
        ) {
            usersLiveData.value = it
        }
    }
}