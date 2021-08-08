package com.kfadli.travelcar.ui.home

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.kfadli.core.models.Vehicle
import com.kfadli.core.models.RepositoryResponse
import com.kfadli.core.repositories.VehiclesRepository
import com.kfadli.travelcar.TravelCarApplication
import com.kfadli.travelcar.models.UIState
import kotlinx.coroutines.*

@ExperimentalCoroutinesApi
class HomeViewModel(application: Application) : AndroidViewModel(application) {

    companion object {
        private val TAG = HomeFragment::class.java.simpleName
    }

    private val vehiclesRepository: VehiclesRepository =
        (application as TravelCarApplication).coreManager.httpRepository


    private val _state = MutableLiveData<UIState<List<Vehicle>>>()
    val state: LiveData<UIState<List<Vehicle>>> = _state

    suspend fun loadVehicles(query: String = "") {

        Log.v(TAG, "loadVehicles | query: $query")

        _state.postValue(UIState.Loading)

        val response = withContext(Dispatchers.IO) {
            when (query) {
                "" -> vehiclesRepository.load()
                else -> vehiclesRepository.search(query = query)
            }
        }

        Log.d(TAG, "loadVehicles::response | $response")

        _state.postValue(
            when (response) {
                is RepositoryResponse.Failure -> UIState.Failure(response.throwable)
                is RepositoryResponse.Success -> UIState.Success(data = response.body)
            }
        )
    }

}