package com.chichi289.paginationapp.viewmodel.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.chichi289.paginationapp.api.DummyRepository

class ListViewModelFactory(
    private val dummyRepository: DummyRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return modelClass.getConstructor(DummyRepository::class.java)
            .newInstance(dummyRepository)
    }
}