package com.kfadli.travelcar.ui.home

import android.util.Log
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

    companion object {
        private val TAG = HomeFragment::class.java.simpleName
    }

    private val server: CoreManager = CoreManager()

    private val _state = MutableLiveData<UIState<List<VehicleResponse>>>()
    val state: LiveData<UIState<List<VehicleResponse>>> = _state

    suspend fun loadVehicles() {
        _state.postValue(UIState.Loading)

        val response = withContext(Dispatchers.IO) {
            server.loadItems()
        }

        Log.d(TAG, "response: $response")

        _state.postValue(
            when (response) {
                is NetworkResponse.Success -> UIState.Success(response.body)
                is NetworkResponse.ApiError -> UIState.Failure(IOException("message: ${response.body}, code: ${response.code}"))
                is NetworkResponse.NetworkError -> UIState.Failure(response.error)
                is NetworkResponse.UnknownError -> UIState.Failure(response.error ?: UnknownError())
            }
        )
    }

}