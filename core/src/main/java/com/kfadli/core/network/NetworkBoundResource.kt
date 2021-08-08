package com.kfadli.core.network

import androidx.annotation.MainThread
import androidx.annotation.WorkerThread

// ResultType: Type for the Resource data.
// RequestType: Type for the API response.
abstract class NetworkBoundResource<ResultType, RequestType> {
    // Called to save the result of the API response into the database
    @WorkerThread
    protected abstract suspend fun saveResult(item: RequestType)

    // Called with the data in the database to decide whether to fetch
    // potentially updated data from the network.
    @MainThread
    protected abstract fun shouldFetch(data: ResultType?): Boolean

    // Called to get the cached data
    @MainThread
    protected abstract suspend fun loadFromCache(): ResultType

    // Called to create the API call.
    @MainThread
    protected abstract suspend fun createCall(): RequestType

    @MainThread
    protected abstract fun transformResponse(data: RequestType): ResultType

    suspend fun execute(): ResultType {
        val data = loadFromCache()
        return if (data != null) {
            // if data found in local
            if (shouldFetch(data)) {
                // if data is older than 24H
                return transformResponse(createCall())
            } else {
                data
            }
        } else {
            // data not found in local, fetch it from Network
            return transformResponse(createCall())
        }
    }
}


