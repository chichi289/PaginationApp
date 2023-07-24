package com.chichi289.paginationapp.data_source

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.chichi289.paginationapp.api.DummyApi
import com.chichi289.paginationapp.db.UserDao
import com.chichi289.paginationapp.models.User
import com.chichi289.paginationapp.models.Users
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import java.io.IOException
import java.net.UnknownHostException

class UserDataSource(
    private val dummyApi: DummyApi,
    private val userDao: UserDao
) : PagingSource<Int, User>() {

    private var currentPage = 1

    /**
     * Replaces ItemKeyedDataSource.
     * */
    /*override fun getRefreshKey(state: PagingState<Int, User>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }*/

    /**
     * Replacing PositionalDataSource.
     * */
    /*override fun getRefreshKey(state: PagingState<Int, User>): Int? {
        return state.anchorPosition
    }

    override val keyReuseSupported: Boolean
        get() = !super.keyReuseSupported*/

    override fun getRefreshKey(state: PagingState<Int, User>): Int = 1

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, User> {

        /*return LoadResult.Page(
            data = emptyList(),
            prevKey = null,
            nextKey = null
        )*/

        currentPage = params.key ?: 1

        try {
            val call = dummyApi.getUsers(page = currentPage)
            val response = withContext(Dispatchers.IO) {
                call.execute()
            }
            val mBean: Users? = response.body()
            val users = mBean?.users

            return if (response.isSuccessful && mBean != null && users != null) {
                withContext(Dispatchers.IO) {
                    userDao.saveUsers(users)
                }
                LoadResult.Page(
                    data = users,
                    prevKey = if (currentPage > 1) currentPage - 1 else null,
                    nextKey = if (users.isNotEmpty()) currentPage + 1 else null
                )
            } else {
                LoadResult.Error(Exception(response.message()))
            }
        } catch (e: IOException) {
            if (e is UnknownHostException) {
                return LoadResult.Error(Exception("No internet connection."))
            }
            return LoadResult.Error(e)
        } catch (e: HttpException) {
            return LoadResult.Error(e)
        }
    }
}