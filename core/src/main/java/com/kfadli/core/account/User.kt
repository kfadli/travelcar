package com.kfadli.core.account

import android.graphics.Bitmap
import java.util.*

data class User(
    val firstName: String = "",
    val lastName: String = "",
    val address: String = "",
    val date: Date? = null,
    val thumbnail: Bitmap? = null
)