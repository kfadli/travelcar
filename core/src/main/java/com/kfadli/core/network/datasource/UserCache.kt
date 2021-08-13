package com.kfadli.core.network.datasource

import android.content.SharedPreferences
import com.google.gson.Gson
import com.kfadli.core.account.User
import com.kfadli.core.models.RepositoryResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext


class UserCache(private val preferences: SharedPreferences) {

    private val USER_KEY = "user_key"

    private val gson = Gson()

    suspend fun save(user: User) =
        withContext(Dispatchers.IO) {
            preferences.edit()
                .putString(USER_KEY, gson.toJson(user))
                .commit()
        }

    suspend fun readCache(): RepositoryResponse<User, Throwable> {
        return runCatching {
            withContext(Dispatchers.IO) {
                val json = preferences.getString(USER_KEY, "")
                gson.fromJson(json, User::class.java)
            }
        }.fold(
            { user ->
                RepositoryResponse.Success(response = user)
            },
            { throwable ->
                RepositoryResponse.Failure(throwable = throwable)
            })
    }
}