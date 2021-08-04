package com.kfadli.travelcar.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.kfadli.core.CoreManager
import com.kfadli.core.network.NetworkResponse
import com.kfadli.core.network.responses.ErrorResponse
import com.kfadli.core.network.responses.VehicleResponse
import com.kfadli.travelcar.models.UIState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.IOException

class HomeViewModel : ViewModel() {

    private val scope = CoroutineScope(Dispatchers.Main)
    private val server: CoreManager = CoreManager()

    private val _vehicles = MutableLiveData<UIState<List<VehicleResponse>>>().apply {
        scope.launch {
            value = when (val result = load()) {
                is NetworkResponse.Success -> UIState.Success(result.body)
                is NetworkResponse.ApiError -> UIState.Failure(IOException("message: ${result.body}, code: ${result.code}"))
                is NetworkResponse.NetworkError -> UIState.Failure(result.error)
                is NetworkResponse.UnknownError -> UIState.Failure(result.error ?: UnknownError())
            }
        }
    }

    private suspend fun load(): NetworkResponse<List<VehicleResponse>, ErrorResponse> {
        _vehicles.postValue(UIState.Loading)

        return withContext(Dispatchers.IO) {
            server.loadItems()
        }
    }

    val vehicles: LiveData<UIState<List<VehicleResponse>>> = _vehicles
}