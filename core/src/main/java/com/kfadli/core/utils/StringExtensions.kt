package com.kfadli.core.utils

import com.kfadli.core.models.EquipmentsEnum


fun List<String>.toEquipmentsEnum(): List<EquipmentsEnum> = this.map { it.toEquipmentEnum() }


fun String.toEquipmentEnum(): EquipmentsEnum = when (this) {
    "GPS" -> EquipmentsEnum.GPS
    "Airbags" -> EquipmentsEnum.AIRBAGS
    "Climatisation" -> EquipmentsEnum.CLIMATE
    "Siege enfant" -> EquipmentsEnum.SEAT_CHILD
    "Assistance 24h/24" -> EquipmentsEnum.ASSISTANCE_24_24
    else -> EquipmentsEnum.UNKNOWN

}