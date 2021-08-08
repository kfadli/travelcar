package com.kfadli.travelcar.ui.details

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.google.android.material.chip.Chip
import com.kfadli.travelcar.R
import com.kfadli.travelcar.databinding.FragmentDetailsBinding
import com.kfadli.travelcar.databinding.FragmentVehiclesBinding
import com.kfadli.travelcar.ui.vehicles.VehiclesViewModel
import kotlinx.coroutines.launch

class DetailsFragment : Fragment() {

    companion object {
        fun newInstance() = DetailsFragment()
    }

    private lateinit var viewModel: DetailsViewModel
    private var _binding: FragmentDetailsBinding? = null

    private val binding get() = _binding!!

    val args: DetailsFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewModel = ViewModelProvider(this).get(DetailsViewModel::class.java)
        _binding = FragmentDetailsBinding.inflate(inflater, container, false)

        val root: View = binding.root

        initUI()

        return root
    }

    private fun initUI() {
        val vehicle = args.vehicle

        binding.name.text = "${vehicle.brand} ${vehicle.model}"
        binding.year.text = vehicle.year.toString()

        vehicle.equipments.map {
            Chip(requireContext())
                .apply { text = it.name }
        }.forEach { binding.chips.addView(it) }

        Glide.with(requireActivity())
            .load(vehicle.thumbnail)
            .into(binding.thumbnail)
    }

}