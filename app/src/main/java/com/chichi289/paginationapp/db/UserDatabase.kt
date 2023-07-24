package com.chichi289.paginationapp.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.chichi289.paginationapp.models.User

@Database(entities = [User::class], version = 1)
abstract class UserDatabase : RoomDatabase() {

    abstract fun userDao(): UserDao

    companion object {

        const val USER_TABLE_NAME = "user_table"

        private var instance: UserDatabase? = null

        @Synchronized
        fun getInstance(context: Context): UserDatabase {
            if (instance == null) {
                instance = Room.databaseBuilder(
                    context,
                    UserDatabase::class.java,
                    "user_database"
                )
                    .fallbackToDestructiveMigration()
                    .build()
            }
            return instance!!
        }

    }
}

