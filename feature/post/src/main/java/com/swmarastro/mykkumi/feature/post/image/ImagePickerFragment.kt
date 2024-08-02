package com.swmarastro.mykkumi.feature.post.image

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.swmarastro.mykkumi.common_ui.base.BaseFragment
import com.swmarastro.mykkumi.common_ui.permission.ImagePermissionUtils
import com.swmarastro.mykkumi.feature.post.R
import com.swmarastro.mykkumi.feature.post.databinding.FragmentImagePickerBinding

class ImagePickerFragment : BaseFragment<FragmentImagePickerBinding>(R.layout.fragment_image_picker) {

    private val viewModel by viewModels<ImagePickerViewModel>()

    private lateinit var imagePickerAdapter: ImagePickerAdapter

    private var navController: NavController? = null

    private val cameraLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            viewModel.doneSetCameraImage(navController)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        navController = view.findNavController()

        // 이미지 선택 완료
        binding.btnDonePicker.setOnClickListener {
            // 선택한 이미지가 있는지 확인
            if(viewModel.imagePickerUiState.value.isNullOrEmpty()) {
                Toast.makeText(requireContext(), R.string.notice_not_select_image, Toast.LENGTH_SHORT).show()
            }
            else {
                viewModel.doneSelectImages(navController)
            }
        }
    }

    override suspend fun initView() {
        bind {
            vm = viewModel
        }

        initImagePickerRecyclerView()
    }

    private fun initImagePickerRecyclerView() {
        imagePickerAdapter = ImagePickerAdapter(
            viewModel,
            captureWithCamera = {
                captureWithCamera()
            }
        )
        binding.recyclerviewImagePicker.layoutManager = GridLayoutManager(requireContext(), 3)
        binding.recyclerviewImagePicker.adapter = imagePickerAdapter

        viewModel.fetchImageItemList(requireContext())
        imagePickerAdapter.imagePickerList.add(CameraBtn())

        viewModel.imagePickerUiState.observe(viewLifecycleOwner) { imagePickerList ->
            imagePickerAdapter.imagePickerList.addAll(imagePickerList)
            imagePickerAdapter.notifyDataSetChanged()
        }
    }

    private fun captureWithCamera() {
        // 카메라로 촬영
        ImagePermissionUtils.captureWithCamera(
            requireContext(),
            cameraLauncher
        ) { uri -> // 카메라에서 선택했을 경우
            viewModel.setCameraImagePath(uri)
        }
    }
}