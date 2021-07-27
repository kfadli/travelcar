package com.kfadli.travelcar.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.kfadli.core.CoreManager
import com.kfadli.core.network.responses.VehicleResponse
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class HomeViewModel : ViewModel() {

  private val scope = CoroutineScope(Dispatchers.Main)
  private val server: CoreManager.Server = CoreManager.Server

  private val _vehicles = MutableLiveData<List<VehicleResponse>>().apply {
    scope.launch {
      value = load()
    }
  }

  private suspend fun load(): List<VehicleResponse>? {

    val result = withContext(Dispatchers.IO) {
      server.loadItems()
    }

    return if (result.isSuccessful) {
      result.body()!!
    } else {
      null
    }
  }

  val vehicles: LiveData<List<VehicleResponse>> = _vehicles
}