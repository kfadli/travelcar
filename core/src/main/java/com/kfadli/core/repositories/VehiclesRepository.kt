package com.kfadli.core.repositories

import android.util.Log
import com.kfadli.core.models.RepositoryResponse
import com.kfadli.core.models.Vehicle
import com.kfadli.core.network.Api
import com.kfadli.core.network.NetworkBoundResource
import com.kfadli.core.network.NetworkResponse
import com.kfadli.core.network.datasource.VehiclesCache
import com.kfadli.core.network.responses.ErrorResponse
import com.kfadli.core.network.responses.VehicleResponse
import com.kfadli.core.utils.toVehicles
import java.io.IOException
import java.util.*

class VehiclesRepository(private val service: Api, private val cache: VehiclesCache) :
    IVehiclesRepository {

    companion object {
        private val TAG = VehiclesRepository::class.java.simpleName
    }

    override suspend fun search(query: String): RepositoryResponse<List<Vehicle>, Throwable> {
        Log.v(TAG, "searchItems | query: $query")

        return when (val vehicles = load()) {
            is RepositoryResponse.Failure -> vehicles
            is RepositoryResponse.Success -> {

                val filtered = vehicles.response?.filter { vehicle ->
                    vehicle.model.contains(query, true) || vehicle.brand.contains(query, true)
                }

                return RepositoryResponse.Success(filtered)
            }
        }.also {
            Log.v(TAG, "searchItems::result | query: $query, $it")
        }
    }

    override suspend fun load(): RepositoryResponse<List<Vehicle>, Throwable> = object :
        NetworkBoundResource<RepositoryResponse<List<Vehicle>, Throwable>,
                NetworkResponse<List<VehicleResponse>, ErrorResponse>>() {

        override suspend fun saveResult(item: NetworkResponse<List<VehicleResponse>, ErrorResponse>) {
            Log.v(TAG, "saveResult | item: $item")

            when (item) {
                is NetworkResponse.Success -> cache.save(item.body.toVehicles()).also {
                    Log.d(TAG, "saveResult::result | saved")
                }
                else -> {
                    // Ignore
                    Log.w(TAG, "saveResult::result | ignored")
                }
            }
        }

        override fun shouldFetch(data: RepositoryResponse<List<Vehicle>, Throwable>?): Boolean {
            Log.v(TAG, "shouldFetch")

            return if (cache.isExist()) {
                val calendar = Calendar.getInstance()
                calendar.add(Calendar.HOUR_OF_DAY, -24)

                val yesterday = calendar.time

                cache.getDate().before(yesterday)
            } else {
                true
            }.also {
                Log.d(TAG, "shouldFetch::result | $it")
            }
        }

        override suspend fun loadFromCache(): RepositoryResponse<List<Vehicle>, Throwable> {
            Log.v(TAG, "loadFromCache")

            return cache.readCache().also {
                Log.d(TAG, "loadFromCache::result | $it")
            }
        }

        override suspend fun createCall(): NetworkResponse<List<VehicleResponse>, ErrorResponse> =
            service.getVehicles()

        override fun transformResponse(data: NetworkResponse<List<VehicleResponse>, ErrorResponse>): RepositoryResponse<List<Vehicle>, Throwable> =
            when (data) {
                is NetworkResponse.Success -> RepositoryResponse.Success(data.body.toVehicles())
                is NetworkResponse.ApiError -> RepositoryResponse.Failure(IOException("message: ${data.body}, code: ${data.code}"))
                is NetworkResponse.NetworkError -> RepositoryResponse.Failure(data.error)
                is NetworkResponse.UnknownError -> RepositoryResponse.Failure(
                    data.error ?: UnknownError()
                )
            }

    }.execute().also { response ->
        when (response) {
            is RepositoryResponse.Success -> cache.save(response.response ?: emptyList())
            else -> {
                // ignore we have nothing to save here
            }
        }
    }
}
