package com.chichi289.paginationapp.models

import com.google.gson.annotations.SerializedName

data class Users(
    @SerializedName("total")
    val total: Int,
    @SerializedName("page")
    val page: Int,
    @SerializedName("limit")
    val limit: Int,
    @SerializedName("data")
    val users: List<User>
)