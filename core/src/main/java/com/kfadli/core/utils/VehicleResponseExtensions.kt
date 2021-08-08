package com.kfadli.core.utils

import com.kfadli.core.models.Vehicle
import com.kfadli.core.network.responses.VehicleResponse


fun List<VehicleResponse>.toVehicles() = this.map { it.toVehicle() }


fun VehicleResponse.toVehicle() = Vehicle(
    brand = make,
    model = model,
    year = year,
    thumbnail = picture,
    equipments = equipments?.toEquipmentsEnum() ?: emptyList()
)