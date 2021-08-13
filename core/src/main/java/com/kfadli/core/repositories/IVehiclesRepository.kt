package com.kfadli.core.repositories

import com.kfadli.core.models.RepositoryResponse
import com.kfadli.core.models.Vehicle

interface IVehiclesRepository {

    suspend fun search(query: String): RepositoryResponse<List<Vehicle>, Throwable>
    suspend fun load(): RepositoryResponse<List<Vehicle>, Throwable>
}