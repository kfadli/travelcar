package com.kfadli.travelcar.ui.account

import android.app.DatePickerDialog
import android.app.DatePickerDialog.OnDateSetListener
import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.kfadli.travelcar.databinding.FragmentAccountEditBinding
import com.kfadli.travelcar.helper.ImageInputHelper
import com.kfadli.travelcar.model.UIState
import com.kfadli.travelcar.ui.account.form.FormModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

const val READ_STORAGE_PERMISSION_REQUEST_CODE = 1005

class AccountEditFragment : Fragment() {

    private lateinit var viewModel: AccountViewModel
    private var _binding: FragmentAccountEditBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private val scope = CoroutineScope(Dispatchers.Main)
    private val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.FRANCE)


    private val imageHelper: ImageInputHelper by lazy {
        ImageInputHelper(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewModel =
            ViewModelProvider(this).get(AccountViewModel::class.java)

        _binding = FragmentAccountEditBinding.inflate(inflater, container, false)

        initUI()
        initObserver()

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


    private fun initUI() {
        val calendar = Calendar.getInstance()

        val listener =
            OnDateSetListener { _, year, monthOfYear, dayOfMonth ->
                calendar.set(Calendar.YEAR, year)
                calendar.set(Calendar.MONTH, monthOfYear)
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)

                binding.dobInput.setText(sdf.format(calendar.time))
            }

        binding.dobInput.setOnClickListener {
            DatePickerDialog(
                requireActivity(),
                listener,
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
            ).show()
        }

        imageHelper.setImageActionListener(object : ImageInputHelper.ImageActionListener {
            override fun onImageSelectedFromGallery(uri: Uri?, imageFile: File?) {
                val image = BitmapFactory.decodeFile(imageFile?.absolutePath)
                binding.form?.thumbnail = image
            }

            override fun onImageTakenFromCamera(uri: Uri?, imageFile: File?) {
                val image = BitmapFactory.decodeFile(imageFile?.absolutePath)
                binding.form?.thumbnail = image
            }

            override fun onImageCropped(uri: Uri?, imageFile: File?) {

            }

        })

        binding.profileImageview.setOnClickListener { showPickImageDialog() }

        // finally we get User data
        scope.launch {
            viewModel.getUser()
        }
    }

    private fun initObserver() {
        viewModel.state.observe(viewLifecycleOwner, { state ->
            when (state) {
                UIState.Loading -> {
                }

                is UIState.NavigationAction.ReadForm -> {
                    findNavController().popBackStack()
                }

                is UIState.Success -> {
                    binding.form = FormModel(state.data)
                    binding.viewmodel = viewModel
                }
                else -> {
                    // Nothing to do
                }
            }
        })
    }

    private fun showPickImageDialog() {
        val options = listOf("Pick Image from Gallery", "Take Picture with Camera").toTypedArray()
        val builder = androidx.appcompat.app.AlertDialog.Builder(requireActivity())

        builder.setTitle("Pick an Image")
        builder.setItems(options) { _, option ->
            when (option) {
                0 -> imageHelper.selectImageFromGallery()
                1 -> imageHelper.takePhotoWithCamera()
            }
        }

        builder.show()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        // not good
        super.onActivityResult(requestCode, resultCode, data)

        if (data != null) {
            imageHelper.onActivityResult(requestCode, resultCode, data)
        }
    }
}