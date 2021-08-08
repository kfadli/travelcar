package com.kfadli.core.network.responses

data class VehicleResponse(
  val make: String,
  val model: String,
  val year: Int,
  val picture: String,
  val equipments: List<String>?
)