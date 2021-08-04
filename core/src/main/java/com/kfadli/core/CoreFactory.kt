package com.kfadli.core

object CoreFactory {
    fun createService(url: String) = HttpBuilder.createServiceApi(api = url)
}
