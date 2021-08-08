package com.kfadli.core.models

data class Vehicle(
    val brand: String,
    val model: String,
    val year: Int,
    val thumbnail: String,
    val equipments: List<EquipmentsEnum>
)
