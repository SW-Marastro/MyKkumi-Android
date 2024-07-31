package com.swmarastro.mykkumi.feature.post

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.swmarastro.mykkumi.common_ui.base.BaseFragment
import com.swmarastro.mykkumi.common_ui.permission.ImagePermissionUtils
import com.swmarastro.mykkumi.feature.post.databinding.FragmentPostEditBinding
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.launch


class PostEditFragment : BaseFragment<FragmentPostEditBinding>(R.layout.fragment_post_edit){

    private val viewModel by viewModels<PostEditViewModel>()

    private val imagePickerLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val data: Intent? = result.data
            val selectedImageUri: Uri? = data?.data

            // 갤러리에서 이미지를 선택했을 경우
            if (selectedImageUri != null) {
                viewModel.selectPostImage(selectedImageUri)
            }
            // 카메라로 촬영했을 경우
            else if (viewModel.cameraImagePath.value != null) {
                viewModel.selectPostImage(viewModel.cameraImagePath.value!!)
            }
        }
    }

    override suspend fun initView() {
        bind {
            vm = viewModel
        }
        pickPostImage()
    }

    private fun pickPostImage() {
        // 이미지 가져오기
        ImagePermissionUtils.chooserImageIntent(
            requireContext(),
            imagePickerLauncher
        ) { uri -> // 카메라에서 선택했을 경우
            viewModel.setCameraImagePath(uri)
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == ImagePermissionUtils.MULTIPLE_PERMISSION) {
            if (grantResults.isNotEmpty() && grantResults.all { it == PackageManager.PERMISSION_GRANTED }) {
                // 이미지 가져오기
                ImagePermissionUtils.chooserImageIntent(
                    requireContext(),
                    imagePickerLauncher
                ) { uri -> // 카메라에서 선택했을 경우
                    viewModel.setCameraImagePath(uri)
                }
            } else {
                // 권한이 거부된 경우 처리
                Toast.makeText(requireContext(), "권한이 거부되었습니다.", Toast.LENGTH_SHORT).show()
            }
        }
    }
}