package com.kfadli.core

import com.kfadli.core.network.Api
import com.kfadli.core.network.NetworkResponse
import com.kfadli.core.network.responses.ErrorResponse
import com.kfadli.core.network.responses.VehicleResponse


private const val URL =
    "https://gist.githubusercontent.com//ncltg/6a74a0143a8202a5597ef3451bde0d5a/raw/8fa93591ad4c3415c9e666f888e549fb8f945eb7/"


class CoreManager() {

    private val service: Api by lazy {
        CoreFactory.createService(URL)
    }

    suspend fun loadItems(): NetworkResponse<List<VehicleResponse>, ErrorResponse> = service.getVehicles()
}