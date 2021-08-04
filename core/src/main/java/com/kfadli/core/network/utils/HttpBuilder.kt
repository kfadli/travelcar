package com.kfadli.core.network.utils

import com.kfadli.core.network.Api
import com.kfadli.core.network.retrofit.NetworkResponseAdapterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object HttpBuilder {

    private var client: OkHttpClient

    init {
        val logging =
            HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BODY }

        client = OkHttpClient.Builder()
            .addInterceptor(logging)
            .build()
    }

    fun createServiceApi(api: String): Api {

        val retrofit = Retrofit.Builder()
            .baseUrl(api)
            .addCallAdapterFactory(NetworkResponseAdapterFactory())
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()

        return retrofit.create(Api::class.java)
    }
}