package com.kfadli.core.network.datasource

import android.content.SharedPreferences
import com.google.gson.Gson
import com.kfadli.core.network.NetworkResponse
import com.kfadli.core.network.responses.ErrorResponse
import com.kfadli.core.network.responses.VehicleResponse
import java.util.*
import com.google.gson.reflect.TypeToken


class HttpCache(val preferences: SharedPreferences) {

    private val VEHICLES_KEY = "vehicles_key"
    private val DATE_KEY = "date_key"

    private val gson = Gson()

    fun save(body: List<VehicleResponse>) {
        preferences.edit()
            .putString(VEHICLES_KEY, gson.toJson(body))
            .putLong(DATE_KEY, Date().time)
            .apply()
    }

    fun isExist(): Boolean = preferences.contains(VEHICLES_KEY)

    fun readCache(): NetworkResponse<List<VehicleResponse>, ErrorResponse> =
        NetworkResponse.Success(
            if (isExist()) {
                val type = object : TypeToken<List<VehicleResponse?>?>() {}.type
                val json = preferences.getString(VEHICLES_KEY, "{}")
                gson.fromJson(json, type)
            } else {
                // may never happen but i need to find a better way to handle the impossible way :p
                listOf()
            }
        )

    fun getDate(): Date = Date(preferences.getLong(DATE_KEY, 0L))
}