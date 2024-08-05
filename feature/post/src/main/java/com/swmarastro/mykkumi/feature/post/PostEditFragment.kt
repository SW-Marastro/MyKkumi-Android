package com.swmarastro.mykkumi.feature.post

import android.net.Uri
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.widget.HorizontalScrollView
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import coil.load
import com.swmarastro.mykkumi.common_ui.base.BaseFragment
import com.swmarastro.mykkumi.feature.post.databinding.FragmentPostEditBinding
import com.swmarastro.mykkumi.feature.post.image.ImagePickerArgument
import com.swmarastro.mykkumi.feature.post.touchEvent.PostEditImageTouchCallback

class PostEditFragment : BaseFragment<FragmentPostEditBinding>(R.layout.fragment_post_edit){
    private val viewModel by viewModels<PostEditViewModel>()

    private lateinit var selectPostImageListAdapter: SelectPostImageListAdapter

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

        // 추가된 이미지
        viewModel.postEditUiState.observe(viewLifecycleOwner, Observer {
            selectPostImageListAdapter.postImageList = it
            selectPostImageListAdapter.notifyDataSetChanged()

            // 스크롤을 맨 오른쪽으로 이동
            binding.scrollSelectPostImageList.isSmoothScrollingEnabled = true
            binding.scrollSelectPostImageList.fullScroll(HorizontalScrollView.FOCUS_RIGHT)

            if(it.size > 0)
                binding.imagePostEdit.load(it[it.size - 1].localUri) // 추가된 이미지를 화면에 보여주기

            // 이미지 10개 선택됐으면 추가 버튼 가리기
            if(selectPostImageListAdapter.postImageList.size == viewModel.MAX_IMAGE_COUNT) {
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

        var moveX = 50f
        var moveY = 50f

        // 핀 추가
        binding.btnAddPin.setOnClickListener {
            // test
            binding.testPin.visibility = View.VISIBLE
            binding.testPin.setOnTouchListener { v, event ->
                when(event.action) {
                    MotionEvent.ACTION_DOWN -> {
                        moveX = v.x - event.rawX
                        moveY = v.y - event.rawY
                    }

                    MotionEvent.ACTION_MOVE -> {
                        v.animate()
                            .x(event.rawX + moveX)
                            .y(event.rawY + moveY)
                            .setDuration(0)
                            .start()
                    }
                }

                true
            }
        }

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
    }

    // 선택된 이미지 리스트 Recyclerview
    private fun initSelectPostImagesRecyclerView() {
        selectPostImageListAdapter = SelectPostImageListAdapter(
            onClickPostImage = {
                onClickPostImage(it)
            }
        )

        // 드래그 이동 adapter
        postEditImageTouchHelper = ItemTouchHelper(PostEditImageTouchCallback(selectPostImageListAdapter))
        postEditImageTouchHelper.attachToRecyclerView(binding.recyclerviewSelectPostImage)

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