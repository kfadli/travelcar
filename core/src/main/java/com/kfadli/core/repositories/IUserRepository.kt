package com.kfadli.core.repositories

import com.kfadli.core.account.User

interface IUserRepository {

    suspend fun get(): User?
    suspend fun save(user: User)

}