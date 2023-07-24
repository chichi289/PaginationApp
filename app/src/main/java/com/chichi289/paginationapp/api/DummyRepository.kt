package com.chichi289.paginationapp.api

import androidx.lifecycle.MutableLiveData
import com.chichi289.paginationapp.models.Resource
import com.chichi289.paginationapp.models.Users
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DummyRepository(
    private val dummyApi: DummyApi
) {

    fun getUsers(page: Int): MutableLiveData<Any> {
        val data = MutableLiveData<Any>()
        val call = dummyApi.getUsers(page = page)
        call.enqueue(object : Callback<Users> {
            override fun onResponse(call: Call<Users>, response: Response<Users>) {
                val mBean = response.body()
                if (response.isSuccessful && mBean != null) {
                    data.value = Resource.Success(mBean)
                } else {
                    data.value = Resource.Error<String>(response.message())
                }
            }

            override fun onFailure(call: Call<Users>, t: Throwable) {
                data.value = Resource.Error<String>(t.message ?: "onFailure")
            }

        })
        return data
    }
}