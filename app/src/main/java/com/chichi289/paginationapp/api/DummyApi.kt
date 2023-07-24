package com.chichi289.paginationapp.api

import com.chichi289.paginationapp.PaginationApp.Companion.APP_ID
import com.chichi289.paginationapp.models.Users
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query

interface DummyApi {

    // https://dummyapi.io/docs

    @Headers("app-id:$APP_ID")
    @GET("user")
    fun getUsers(
        @Query("page") page: Int,
        @Query("limit") limit: Int = 10,
    ): Call<Users>

}