package com.chichi289.paginationapp.utils

import com.chichi289.paginationapp.models.User

@Suppress("unused")
object UsersGenerator {
    fun getUsers(): ArrayList<User> {
        val mList = ArrayList<User>()

        (1..10).forEach {
            mList.add(
                User(
                    id = it.toString(),
                    firstName = "First Name $it",
                    lastName = "Last Name $it",
                    picture = "https://www.w3schools.com/w3images/avatar2.png",
                    title = "Title $it"
                )
            )
        }

        return mList
    }
}