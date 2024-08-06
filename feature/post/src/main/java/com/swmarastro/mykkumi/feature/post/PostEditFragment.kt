package com.swmarastro.mykkumi.feature.post

import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.widget.HorizontalScrollView
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager2.widget.ViewPager2
import com.swmarastro.mykkumi.common_ui.base.BaseFragment
import com.swmarastro.mykkumi.feature.post.databinding.FragmentPostEditBinding
import com.swmarastro.mykkumi.feature.post.image.ImagePickerArgument
import com.swmarastro.mykkumi.feature.post.imageWithPin.EditImageWithPinAdapter
import com.swmarastro.mykkumi.feature.post.touchEvent.PostEditImageTouchCallback


class PostEditFragment : BaseFragment<FragmentPostEditBinding>(R.layout.fragment_post_edit){
    private val viewModel by viewModels<PostEditViewModel>()

    private lateinit var selectPostImageListAdapter: SelectPostImageListAdapter // 이미지들 썸네일 나열
    private lateinit var editImageWithPinAdapter: EditImageWithPinAdapter // 이미지 편집 view (핀 추가할 수 있는)

    private var navController: NavController? = null

    // 포스트 이미지 recyclerview 아이템 이동 콜백 변수 : 드래그 시 이동하는 거
    private lateinit var postEditImageTouchHelper: ItemTouchHelper

    override fun onResume() {
        super.onResume()

        // image picker에서 선택한 이미지
        navController?.currentBackStackEntry?.savedStateHandle?.getLiveData<ImagePickerArgument>("selectImages")
            ?.observe(viewLifecycleOwner) { images ->
                if(!images.selectImages.isNullOrEmpty()) {
                    for (image in images.selectImages) {
                        viewModel.selectPostImage(image)
                    }

                    // 리스트에 추가했다면 지우기 - view resume 될 때마다 추가되는 현상 제거
                    images.selectImages.clear()
                }
            }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        navController = view.findNavController()

        // 이미지 추가
        binding.btnAddPostImage.setOnClickListener(View.OnClickListener {
            viewModel.openImagePicker(navController)
        })

        var moveX = 50f
        var moveY = 50f

        // 핀 추가
//        binding.btnAddPin.setOnClickListener {
//            // test
//            binding.testPin.visibility = View.VISIBLE
//            binding.testPin.setOnTouchListener { v, event ->
//                when(event.action) {
//                    MotionEvent.ACTION_DOWN -> {
//                        moveX = v.x - event.rawX
//                        moveY = v.y - event.rawY
//                    }
//
//                    MotionEvent.ACTION_MOVE -> {
//                        v.animate()
//                            .x(event.rawX + moveX)
//                            .y(event.rawY + moveY)
//                            .setDuration(0)
//                            .start()
//                    }
//                }
//
//                true
//            }
//        }

        // 이전 버튼
        binding.btnBack.setOnClickListener {
            navController?.popBackStack()
        }
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
        initEditImageWithPinViewPager()
        observePostImage()
    }

    // 선택된 이미지 리스트 Recyclerview
    private fun initSelectPostImagesRecyclerView() {
        selectPostImageListAdapter = SelectPostImageListAdapter(
            viewModel,
            onClickPostImage = {
                onClickPostImage()
            },
            onChangeImageSort = {
                onChangeImageSort()
            }
        )

        // 드래그 이동 adapter
        postEditImageTouchHelper = ItemTouchHelper(PostEditImageTouchCallback(selectPostImageListAdapter))
        postEditImageTouchHelper.attachToRecyclerView(binding.recyclerviewSelectPostImage)

        binding.recyclerviewSelectPostImage.apply {
            layoutManager = LinearLayoutManager(
                context,
                LinearLayoutManager.HORIZONTAL,
                false
            )
            adapter = selectPostImageListAdapter
        }
        binding.recyclerviewSelectPostImage.isNestedScrollingEnabled = false
    }

    // 선택된 이미지 편집 화면 viewpager init
    private fun initEditImageWithPinViewPager() {
        editImageWithPinAdapter = EditImageWithPinAdapter()
        binding.viewpagerPostEditImages.adapter = editImageWithPinAdapter
        binding.viewpagerPostEditImages.orientation = ViewPager2.ORIENTATION_HORIZONTAL

        // ViewPager 넘겼을 때도 선택 이미지 변경되는 것
        binding.viewpagerPostEditImages.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                viewModel.postEditUiState.value?.get(viewModel.selectImagePosition.value!!)!!.isSelect = false
                viewModel.postEditUiState.value?.get(position)!!.isSelect = true
                viewModel.changeSelectImagePosition(position)

                selectPostImageListAdapter.notifyDataSetChanged()
            }
        })
    }

    // 이미지 추가되면 recyclerview adapter data 변경, 스크롤 이동 + 이미지 개수 확인
    // edit viewPager data도 변경
    private fun observePostImage() {
        // 추가된 이미지
        viewModel.postEditUiState.observe(viewLifecycleOwner, Observer {
            selectPostImageListAdapter.postImageList = it
            editImageWithPinAdapter.imageWithPinList = it
            selectPostImageListAdapter.notifyDataSetChanged()
            editImageWithPinAdapter.notifyDataSetChanged()

            // 스크롤을 맨 오른쪽으로 이동
            _binding?.let { binding ->
                binding.scrollSelectPostImageList.isSmoothScrollingEnabled = true

                binding.scrollSelectPostImageList.post {
                    binding.scrollSelectPostImageList.fullScroll(HorizontalScrollView.FOCUS_RIGHT)
                }
            } ?: run {
                Log.e("PostEditFragment", "Binding is not initialized")
            }

            if(it.size > 0) // 추가된 이미지를 화면에 보여주기
                binding.viewpagerPostEditImages.post {
                    binding.viewpagerPostEditImages.currentItem = it.size - 1
                }
            //binding.imagePostEdit.load(it[it.size - 1].localUri) // 추가된 이미지를 화면에 보여주기

            // 이미지 10개 선택됐으면 추가 버튼 가리기
            if(selectPostImageListAdapter.postImageList.size == viewModel.MAX_IMAGE_COUNT) {
                binding.btnAddPostImage.visibility = View.GONE
            }
            else {
                binding.btnAddPostImage.visibility = View.VISIBLE
            }
        })

        // 선택된 이미지 (편집 중)
//        viewModel.selectImagePosition.observe(viewLifecycleOwner, Observer {
//            // ViewPager에서 보여주고 있는 이미지 변경
//            binding.viewpagerPostEditImages.setCurrentItem(viewModel.selectImagePosition.value!!, false)
//        })
    }

    // 이미지 순서 변경 시 ViewPager에 적용
    private fun onChangeImageSort() {
        editImageWithPinAdapter.notifyDataSetChanged()
        binding.viewpagerPostEditImages.setCurrentItem(viewModel.selectImagePosition.value!!, false)
    }

    // 선택된 이미지 (편집 중)
    private fun onClickPostImage() {
        // ViewPager에서 보여주고 있는 이미지 변경
        binding.viewpagerPostEditImages.setCurrentItem(viewModel.selectImagePosition.value!!, false)
    }
}