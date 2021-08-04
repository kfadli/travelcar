package com.kfadli.travelcar

import android.app.Application
import com.kfadli.core.CoreManager

class TravelCarApplication : Application() {

    val coreManager: CoreManager by lazy {
        CoreManager(context = applicationContext)
    }

}