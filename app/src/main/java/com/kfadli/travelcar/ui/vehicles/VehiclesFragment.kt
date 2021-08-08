package com.kfadli.travelcar.ui.vehicles

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.transition.MaterialElevationScale
import com.kfadli.core.models.Vehicle
import com.kfadli.travelcar.R
import com.kfadli.travelcar.databinding.FragmentVehiclesBinding
import com.kfadli.travelcar.extensions.getQueryTextChangeStateFlow
import com.kfadli.travelcar.models.UIState
import com.kfadli.travelcar.ui.vehicles.adapter.VehiclesAdapter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

@ExperimentalCoroutinesApi
class VehiclesFragment : Fragment() {

    companion object {
        private val TAG = VehiclesFragment::class.java.simpleName
    }

    private lateinit var viewModel: VehiclesViewModel
    private var _binding: FragmentVehiclesBinding? = null

    private val scope = CoroutineScope(Dispatchers.Main)

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private val adapter = VehiclesAdapter(object : VehiclesAdapter.SelectVehicleListener {
        override fun onClick(vehicle: Vehicle) {

            exitTransition = MaterialElevationScale(false).apply {
                duration = 200L
            }
            reenterTransition = MaterialElevationScale(true).apply {
                duration = 200L
            }

            val action =
                VehiclesFragmentDirections.actionNavigationVehiclesToNavigationDetail(vehicle)
            findNavController().navigate(action)
        }
    })

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        viewModel = ViewModelProvider(this).get(VehiclesViewModel::class.java)

        _binding = FragmentVehiclesBinding.inflate(inflater, container, false)

        val root: View = binding.root

        initUI()
        initObserver()

        scope.launch { initInstantSearch() }

        return root
    }

    private suspend fun initInstantSearch() {
        binding.search.searchView.getQueryTextChangeStateFlow()
            .debounce(300)
            .distinctUntilChanged()
            .flowOn(Dispatchers.Default)
            .collect { result -> viewModel.loadVehicles(result) }
    }

    private fun initUI() {
        binding.vehicleRecycler.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = this@VehiclesFragment.adapter
        }

        binding.empty.root.visibility = View.INVISIBLE
        binding.error.root.visibility = View.INVISIBLE
        binding.vehicleRecycler.visibility = View.INVISIBLE
        binding.loader.root.visibility = View.VISIBLE

        binding.error.retryButton.setOnClickListener {
            scope.launch { viewModel.loadVehicles() }
        }
    }

    private fun initObserver() {
        viewModel.state.observe(viewLifecycleOwner, { state ->
            when (state) {
                is UIState.Failure -> {
                    Log.e(TAG, "Failed to get vehicles from repository", state.exception)

                    onFailure(state.exception)
                }

                is UIState.Success -> {
                    Log.d(
                        TAG, "get vehicles from repository" +
                                " with success"
                    )

                    onSuccess(state.data)
                }

                UIState.Loading -> {
                    Log.d(TAG, "loading in progress")

                    onLoading()
                }

            }
        })
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        scope.launch {
            viewModel.loadVehicles()
        }
    }

    private fun onFailure(exception: Throwable) {
        binding.search.searchView.visibility = View.INVISIBLE
        binding.empty.root.visibility = View.INVISIBLE
        binding.vehicleRecycler.visibility = View.INVISIBLE
        binding.loader.root.visibility = View.INVISIBLE

        binding.error.errorMsgText.text = exception.message
        binding.error.root.visibility = View.VISIBLE
    }

    private fun onSuccess(data: List<Vehicle>) {
        binding.error.root.visibility = View.INVISIBLE
        binding.loader.root.visibility = View.INVISIBLE

        if (data.isEmpty()) {
            binding.empty.root.visibility = View.VISIBLE
        } else {
            adapter.updateVehicles(data)
            binding.vehicleRecycler.visibility = View.VISIBLE
            binding.search.searchView.visibility = View.VISIBLE
        }
    }

    private fun onLoading() {
        binding.search.searchView.visibility = View.INVISIBLE
        binding.empty.root.visibility = View.INVISIBLE
        binding.error.root.visibility = View.INVISIBLE
        binding.vehicleRecycler.visibility = View.INVISIBLE
        binding.loader.root.visibility = View.VISIBLE
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}