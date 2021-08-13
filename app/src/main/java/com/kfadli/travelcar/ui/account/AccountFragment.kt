package com.kfadli.travelcar.ui.account

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.kfadli.travelcar.R
import com.kfadli.travelcar.databinding.FragmentAccountBinding
import com.kfadli.travelcar.model.UIState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class AccountFragment : Fragment() {

    private lateinit var viewModel: AccountViewModel
    private var _binding: FragmentAccountBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private val scope = CoroutineScope(Dispatchers.Main)

    private val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.FRANCE)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewModel = ViewModelProvider(this).get(AccountViewModel::class.java)

        _binding = FragmentAccountBinding.inflate(inflater, container, false)

        initUI()
        initObserver()

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun initUI() {
        binding.editButton.setOnClickListener {
            findNavController().navigate(AccountFragmentDirections.actionNavigationAccountToNavigationAccountEdit())
        }

        scope.launch {
            viewModel.getUser()
        }
    }

    private fun initObserver() {
        viewModel.state.observe(viewLifecycleOwner, { state ->
            when (state) {
                UIState.Loading -> {

                }

                is UIState.Success -> {
                    with(state.data) {
                        binding.firstnameValue.text = this?.firstName ?: "N/A"
                        binding.lastnameValue.text = this?.lastName ?: "N/A"
                        binding.dobValue.text = if (this?.date != null) {
                            sdf.format(date!!)
                        } else {
                            "N/A"
                        }

                        binding.addressValue.text = this?.address ?: "N/A"

                        if (this?.thumbnail != null) {
                            binding.profileImage.setImageBitmap(this.thumbnail)
                        } else {
                            binding.profileImage.setImageDrawable(
                                ContextCompat.getDrawable(
                                    requireActivity(),
                                    R.drawable.ic_baseline_no_photography_24
                                )
                            )
                        }
                    }
                }
                else -> {
                    // Nothing to do
                }
            }
        })
    }
}