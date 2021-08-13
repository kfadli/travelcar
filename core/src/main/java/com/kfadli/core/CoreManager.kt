package com.kfadli.core

import android.content.Context
import android.content.SharedPreferences
import com.kfadli.core.network.datasource.UserCache
import com.kfadli.core.network.datasource.VehiclesCache
import com.kfadli.core.repositories.UserRepository
import com.kfadli.core.repositories.VehiclesRepository

private const val URL =
    "https://gist.githubusercontent.com//ncltg/6a74a0143a8202a5597ef3451bde0d5a/raw/8fa93591ad4c3415c9e666f888e549fb8f945eb7/"

private const val SHARED_PREFERENCES_NAME = "cache"

class CoreManager(context: Context) {

    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences(SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE)

    val vehiclesRepository: VehiclesRepository by lazy {
        VehiclesRepository(
            service = CoreFactory.createService(URL),
            cache = VehiclesCache(sharedPreferences)
        )
    }

    val userRepository: UserRepository by lazy {
        UserRepository(userCache = UserCache(sharedPreferences))
    }

}