package com.kfadli.core.repositories

import android.util.Log
import com.kfadli.core.account.User
import com.kfadli.core.models.RepositoryResponse
import com.kfadli.core.network.datasource.UserCache

class UserRepository(private val userCache: UserCache) : IUserRepository {

    private var user: User? = null

    companion object {
        private val TAG = VehiclesRepository::class.java.simpleName
    }

    override suspend fun get(): User? {

        // avoid to load n times User from cache
        if (this.user != null) {
            return this.user
        }

        return when (val body = userCache.readCache()) {
            is RepositoryResponse.Failure -> {
                Log.w(TAG, "failed to get User in cache", body.throwable)

                null
            }

            is RepositoryResponse.Success -> {
                this.user = body.response

                body.response
            }
        }
    }

    override suspend fun save(user: User) {
        userCache.save(user)
            .also { isSuccess ->
                // assign user instance only when save is a success

                if (isSuccess) {
                    this.user = user
                }
            }
    }
}