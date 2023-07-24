package com.chichi289.paginationapp

import android.app.Application
import com.chichi289.paginationapp.db.UserDao
import com.chichi289.paginationapp.db.UserDatabase
import com.chichi289.paginationapp.models.User
import kotlin.random.Random

class PaginationApp : Application() {

    companion object {

        // Obtain you app id from https://dummyapi.io/docs
        const val APP_ID = "YOUR_APP_ID"

        fun getUser() = User(
            id = "60d54rtf5311236168a10a${Random(2)}",
            firstName = "Chirag",
            lastName = "Prajapati",
            picture = "https://avatars.githubusercontent.com/u/20377949?v=4",
            title = "mr"
        )
    }

    private val userDataBase: UserDatabase by lazy {
        UserDatabase.getInstance(this)
    }

    val userDao: UserDao by lazy {
        userDataBase.userDao()
    }
}