package com.chichi289.paginationapp.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.chichi289.paginationapp.db.UserDatabase.Companion.USER_TABLE_NAME
import com.google.gson.annotations.SerializedName

@Entity(tableName = USER_TABLE_NAME)
data class User(

    @ColumnInfo(name = "first_name")
    @SerializedName("firstName")
    var firstName: String? = null,

    @PrimaryKey
    @SerializedName("id")
    val id: String,

    @ColumnInfo(name = "last_name")
    @SerializedName("lastName")
    var lastName: String? = null,

    @ColumnInfo(name = "profile")
    @SerializedName("picture")
    var picture: String? = null,

    @ColumnInfo(name = "title")
    @SerializedName("title")
    val title: String? = null
)