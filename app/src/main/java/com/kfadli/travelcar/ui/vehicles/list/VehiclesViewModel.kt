package com.kfadli.travelcar.ui.vehicles.list

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.kfadli.core.models.RepositoryResponse
import com.kfadli.core.models.Vehicle
import com.kfadli.core.repositories.VehiclesRepository
import com.kfadli.travelcar.TravelCarApplication
import com.kfadli.travelcar.model.UIState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.withContext

@ExperimentalCoroutinesApi
class VehiclesViewModel(application: Application) : AndroidViewModel(application) {

    companion object {
        private val TAG = VehiclesFragment::class.java.simpleName
    }

    private val repository: VehiclesRepository =
        (application as TravelCarApplication).coreManager.vehiclesRepository


    private val _state = MutableLiveData<UIState<List<Vehicle>>>()
    val state: LiveData<UIState<List<Vehicle>>> = _state

    suspend fun loadVehicles(query: String = "") {

        Log.v(TAG, "loadVehicles | query: $query")

        _state.postValue(UIState.Loading)

        val response = withContext(Dispatchers.IO) {
            when (query) {
                "" -> repository.load()
                else -> repository.search(query = query)
            }
        }

        Log.d(TAG, "loadVehicles::response | $response")

        _state.postValue(
            when (response) {
                is RepositoryResponse.Failure -> UIState.Failure(response.throwable)
                is RepositoryResponse.Success -> UIState.Success(
                    data = response.response ?: emptyList()
                )
            }
        )
    }

}