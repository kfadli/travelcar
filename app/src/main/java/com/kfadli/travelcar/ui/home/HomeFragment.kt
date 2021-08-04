package com.kfadli.travelcar.ui.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.kfadli.core.network.responses.VehicleResponse
import com.kfadli.travelcar.MainActivity
import com.kfadli.travelcar.databinding.FragmentListBinding
import com.kfadli.travelcar.models.UIState
import com.kfadli.travelcar.ui.home.adapter.VehiclesAdapter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class HomeFragment : Fragment() {

    companion object {
        private val TAG = HomeFragment::class.java.simpleName
    }

    private lateinit var listViewModel: HomeViewModel
    private var _binding: FragmentListBinding? = null

    private val scope = CoroutineScope(Dispatchers.Main)

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private val adapter = VehiclesAdapter()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        listViewModel = ViewModelProvider(this).get(HomeViewModel::class.java)


        _binding = FragmentListBinding.inflate(inflater, container, false)

        val root: View = binding.root

        binding.vehicleRecycler.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = this@HomeFragment.adapter
        }

        binding.empty.root.visibility = View.INVISIBLE
        binding.error.root.visibility = View.INVISIBLE
        binding.vehicleRecycler.visibility = View.INVISIBLE
        binding.loader.root.visibility = View.VISIBLE

        binding.error.retryButton.setOnClickListener {
            scope.launch {
                listViewModel.loadVehicles()
            }
        }

        listViewModel.state.observe(viewLifecycleOwner, { state ->
            when (state) {
                is UIState.Failure -> {
                    Log.e(TAG, "Failed to get vehicles from api", state.exception)

                    onFailure(state.exception)
                }

                is UIState.Success -> {
                    Log.d(TAG, "get vehicles from api with success")

                    onSuccess(state.data)
                }

                UIState.Loading -> {
                    Log.d(TAG, "loading in progress")

                    onLoading()
                }

            }
        })

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        scope.launch {
            listViewModel.loadVehicles()
        }
    }

    private fun onFailure(exception: Throwable) {
        binding.empty.root.visibility = View.INVISIBLE
        binding.vehicleRecycler.visibility = View.INVISIBLE
        binding.loader.root.visibility = View.INVISIBLE

        binding.error.errorMsgText.text = exception.message
        binding.error.root.visibility = View.VISIBLE
    }

    private fun onSuccess(data: List<VehicleResponse>) {
        binding.error.root.visibility = View.INVISIBLE
        binding.loader.root.visibility = View.INVISIBLE

        if (data.isEmpty()) {
            binding.empty.root.visibility = View.VISIBLE
        } else {
            adapter.updateVehicles(data)
            binding.vehicleRecycler.visibility = View.VISIBLE
        }
    }

    private fun onLoading() {
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