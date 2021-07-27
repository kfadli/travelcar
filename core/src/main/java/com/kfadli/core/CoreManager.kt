package com.kfadli.core

import com.kfadli.core.network.Api
import com.kfadli.core.network.responses.VehicleResponse
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class CoreManager {

  object Server {

    private const val URL =
      "https://gist.githubusercontent.com//ncltg/6a74a0143a8202a5597ef3451bde0d5a/raw/8fa93591ad4c3415c9e666f888e549fb8f945eb7/"

    private val service: Api

    init {
      val logging = HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BODY }

      val client = OkHttpClient.Builder()
        .addInterceptor(logging)
        .build()

      val retrofit = Retrofit.Builder()
        .baseUrl(URL)
        .addConverterFactory(GsonConverterFactory.create())
        .client(client)
        .build()

      service = retrofit.create(Api::class.java)
    }

    suspend fun loadItems(): Response<List<VehicleResponse>> = service.getVehicles()

  }

}