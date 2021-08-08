package com.kfadli.core.repositories

import com.kfadli.core.account.User

class UserRepository {

    fun get(): User {
        return User("", "", "", "")
    }

    fun save(user: User) {

    }
}