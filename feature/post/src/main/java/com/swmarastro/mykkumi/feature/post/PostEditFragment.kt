package com.swmarastro.mykkumi.feature.post

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.HorizontalScrollView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.net.toUri
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import coil.load
import com.swmarastro.mykkumi.common_ui.base.BaseFragment
import com.swmarastro.mykkumi.common_ui.permission.ImagePermissionUtils
import com.swmarastro.mykkumi.feature.post.databinding.FragmentPostEditBinding
import com.swmarastro.mykkumi.feature.post.image.ImagePickerArgument

class PostEditFragment : BaseFragment<FragmentPostEditBinding>(R.layout.fragment_post_edit){

    private final val MAX_IMAGE_COUNT = 10

    private val viewModel by viewModels<PostEditViewModel>()

    private lateinit var selectPostImageListAdapter: SelectPostImageListAdapter

    private var navController: NavController? = null

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

    override fun onResume() {
        super.onResume()

        // image picker에서 선택한 이미지
        navController?.currentBackStackEntry?.savedStateHandle?.getLiveData<ImagePickerArgument>("selectImages")
            ?.observe(viewLifecycleOwner) { images ->
                for(image in images.selectImages) {
                    viewModel.selectPostImage(image)
                }
            }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        navController = view.findNavController()

        // 추가된 이미지
        viewModel.postEditUiState.observe(viewLifecycleOwner, Observer {
            selectPostImageListAdapter.postImageList = it
            selectPostImageListAdapter.notifyDataSetChanged()

            // 스크롤을 맨 오른쪽으로 이동
            binding.scrollSelectPostImageList.isSmoothScrollingEnabled = true
            binding.scrollSelectPostImageList.fullScroll(HorizontalScrollView.FOCUS_RIGHT)

            if(it.size > 0) binding.imagePostEdit.load(it[it.size - 1]) // 추가된 이미지를 화면에 보여주기

            // 이미지 10개 선택됐으면 추가 버튼 가리기
            if(selectPostImageListAdapter.postImageList.size == MAX_IMAGE_COUNT) {
                binding.btnAddPostImage.visibility = View.GONE
            }
            else {
                binding.btnAddPostImage.visibility = View.VISIBLE
            }
        })

        // 이미지 추가
        binding.btnAddPostImage.setOnClickListener(View.OnClickListener {
            viewModel.openImagePicker(navController)
        })
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
        // 화면 최초 생성 시, 이미지 picker 띄워주기
        if(viewModel.checkCreateView.value) {
            viewModel.openImagePicker(navController)
            viewModel.createViewDone() // 생성 끝
        }

    }

    override suspend fun initView() {
        bind {
            vm = viewModel
        }

        initSelectPostImagesRecyclerView()
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

    // 선택된 이미지 리스트 Recyclerview
    private fun initSelectPostImagesRecyclerView() {
        selectPostImageListAdapter = SelectPostImageListAdapter(
            onClickPostImage = {
                onClickPostImage(it)
            }
        )
        binding.recyclerviewSelectPostImage.layoutManager = LinearLayoutManager(
            context,
            LinearLayoutManager.HORIZONTAL,
            false
        )
        binding.recyclerviewSelectPostImage.adapter = selectPostImageListAdapter
        binding.recyclerviewSelectPostImage.isNestedScrollingEnabled = false
    }

    private fun onClickPostImage(image: Uri) {
        binding.imagePostEdit.load(image)
    }
}