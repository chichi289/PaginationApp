package com.chichi289.paginationapp.db

import androidx.room.*
import com.chichi289.paginationapp.models.User
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {

    @Insert
    suspend fun saveUsers(users: List<User>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addUser(user: User)

    @Query("SELECT * FROM ${UserDatabase.USER_TABLE_NAME}")
    fun getUsers(): Flow<List<User>>

    @Delete
    suspend fun deleteUser(user: User)

    @Query("DELETE FROM ${UserDatabase.USER_TABLE_NAME}")
    suspend fun clearDb()
}