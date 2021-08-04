package com.kfadli.core.repositories

import android.util.Log
import com.kfadli.core.network.Api
import com.kfadli.core.network.NetworkBoundResource
import com.kfadli.core.network.NetworkResponse
import com.kfadli.core.network.datasource.HttpCache
import com.kfadli.core.network.responses.ErrorResponse
import com.kfadli.core.network.responses.VehicleResponse
import java.util.*

class HttpRepository(private val service: Api, private val httpCache: HttpCache) {

    companion object {
        private val TAG = HttpRepository::class.java.simpleName
    }

    suspend fun loadItems(): NetworkResponse<List<VehicleResponse>, ErrorResponse> = object :
        NetworkBoundResource<NetworkResponse<List<VehicleResponse>, ErrorResponse>>() {

        override suspend fun saveResult(item: NetworkResponse<List<VehicleResponse>, ErrorResponse>) {
            Log.d(TAG, "saveResult")

            when (item) {
                is NetworkResponse.Success -> httpCache.save(item.body)
                else -> {
                    // Ignore
                }
            }
        }

        override fun shouldFetch(data: NetworkResponse<List<VehicleResponse>, ErrorResponse>?): Boolean {

            Log.d(TAG, "shouldFetch")

            return if (httpCache.isExist()) {
                val calendar = Calendar.getInstance()
                calendar.add(Calendar.HOUR_OF_DAY, -24)

                val yesterday = calendar.time

                return httpCache.getDate().before(yesterday)
            } else {
                true
            }

        }

        override suspend fun loadFromCache(): NetworkResponse<List<VehicleResponse>, ErrorResponse> {
            Log.d(TAG, "loadFromCache")

            return httpCache.readCache()
        }

        override suspend fun createCall(): NetworkResponse<List<VehicleResponse>, ErrorResponse> =
            service.getVehicles()
    }.execute().also { response ->
        when (response) {
            is NetworkResponse.Success -> httpCache.save(response.body)
            else -> {
                // ignore we have nothing to save here
            }
        }
    }
}
