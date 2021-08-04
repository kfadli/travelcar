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
import com.kfadli.travelcar.databinding.FragmentListBinding
import com.kfadli.travelcar.models.UIState
import com.kfadli.travelcar.ui.home.adapter.VehiclesAdapter

class HomeFragment : Fragment() {

    companion object {
        private val TAG = HomeFragment::class.java.simpleName
    }

    private lateinit var listViewModel: HomeViewModel
    private var _binding: FragmentListBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private val adapter = VehiclesAdapter()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        listViewModel =
            ViewModelProvider(this).get(HomeViewModel::class.java)

        _binding = FragmentListBinding.inflate(inflater, container, false)

        val root: View = binding.root

        binding.vehicleRecycler.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = this@HomeFragment.adapter
        }

        listViewModel.vehicles.observe(viewLifecycleOwner, { state ->
            when (state) {
                is UIState.Failure -> activity?.let {
                    Log.e(TAG, "Failed to get vehicles from api", state.exception)
                    Toast.makeText(it, state.exception.message, Toast.LENGTH_SHORT).show()
                }

                is UIState.Success -> {
                    Log.d(TAG, "get vehicles from api with success")
                    adapter.updateVehicles(state.data)
                }

                UIState.Loading -> {
                    //show loader
                }

            }
        })

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}