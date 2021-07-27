package com.kfadli.travelcar.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.kfadli.travelcar.databinding.FragmentListBinding
import com.kfadli.travelcar.ui.home.adapter.VehiclesAdapter

class HomeFragment : Fragment() {

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

    listViewModel.vehicles.observe(viewLifecycleOwner, { vehicles ->
      adapter.updateVehicles(vehicles)
    })

    return root
  }

  override fun onDestroyView() {
    super.onDestroyView()
    _binding = null
  }
}