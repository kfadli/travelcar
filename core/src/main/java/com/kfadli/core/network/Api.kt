package com.kfadli.core.network

import com.kfadli.core.network.responses.VehicleResponse
import retrofit2.Response
import retrofit2.http.GET

interface Api {
  @GET("tc-test-ios.json")
  suspend fun getVehicles(): Response<List<VehicleResponse>>
}
