package com.kfadli.core

import com.kfadli.core.network.utils.HttpBuilder

object CoreFactory {
    fun createService(url: String) = HttpBuilder.createServiceApi(api = url)
}
