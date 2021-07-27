package com.kfadli.travelcar.ui.list

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.kfadli.core.CoreManager
import com.kfadli.core.network.responses.VehicleResponse
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ListViewModel : ViewModel() {

  private val scope = CoroutineScope(Dispatchers.Main)
  private val server: CoreManager.Server = CoreManager.Server

  private val _text = MutableLiveData<String>().apply {
    scope.launch {
      value = load().toString()
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

  val text: LiveData<String> = _text
}