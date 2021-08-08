package com.kfadli.core.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Vehicle(
    val brand: String,
    val model: String,
    val year: Int,
    val thumbnail: String,
    val equipments: List<EquipmentsEnum>
) : Parcelable
