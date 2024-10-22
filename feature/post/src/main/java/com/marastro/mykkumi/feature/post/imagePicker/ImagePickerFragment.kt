package com.marastro.mykkumi.feature.post.imagePicker

import android.app.Activity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.GridLayoutManager
import com.marastro.mykkumi.analytics.AnalyticsHelper
import com.marastro.mykkumi.common_ui.base.BaseFragment
import com.marastro.mykkumi.common_ui.permission.ImagePermissionUtils
import com.marastro.mykkumi.feature.post.R
import com.marastro.mykkumi.feature.post.databinding.FragmentImagePickerBinding
import javax.inject.Inject
import com.marastro.mykkumi.common_ui.R as StringR

class ImagePickerFragment : BaseFragment<FragmentImagePickerBinding>(R.layout.fragment_image_picker) {

    @Inject
    lateinit var analyticsHelper: AnalyticsHelper

    private val viewModel by viewModels<ImagePickerViewModel>()
    private val args: ImagePickerFragmentArgs by navArgs()

    private lateinit var imagePickerAdapter: ImagePickerAdapter
    private var navController: NavController? = null

    private var maxImageCount: Int = 10

    private val cameraLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            viewModel.doneSetCameraImage(navController)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Firebase Analytics 화면 이름 로깅
        analyticsHelper.logScreenView(getString(com.marastro.mykkumi.analytics.R.string.image_picker_screen))

        activity?.window?.statusBarColor = ContextCompat.getColor(requireContext(), com.marastro.mykkumi.common_ui.R.color.neutral_900)
        activity?.window?.decorView?.systemUiVisibility =
            activity?.window?.decorView?.systemUiVisibility?.and(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR.inv()) ?: 0

        navController = view.findNavController()
        maxImageCount = args.maxImageCount // 최대 선택 가능한 이미지 개수

        // 이미지 선택 완료
        binding.btnDonePicker.setOnClickListener(View.OnClickListener {
            // 선택한 이미지가 있는지 확인
            if(viewModel.selectImage.value.isNullOrEmpty()) {
                Toast.makeText(requireContext(), StringR.string.notice_not_select_image, Toast.LENGTH_SHORT).show()
            }
            else {
                viewModel.doneSelectImages(navController)
            }
        })

        // 이전 버튼
        binding.btnBack.setOnClickListener {
            navController?.popBackStack()
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
            requireContext(),
            viewModel,
            captureWithCamera = {
                captureWithCamera()
            },
            isAllowSelectImages = {
                isAllowSelectImage()
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

    fun isAllowSelectImage(): Boolean {
        return (viewModel.selectImage.value?.size ?: 0) < maxImageCount
    }

    override fun onDestroyView() {
        super.onDestroyView()

        activity?.window?.statusBarColor = ContextCompat.getColor(requireContext(), com.marastro.mykkumi.common_ui.R.color.white)
        activity?.window?.decorView?.systemUiVisibility =
            activity?.window?.decorView?.systemUiVisibility?.or(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR) ?: View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
    }
}