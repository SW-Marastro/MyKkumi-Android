package com.marastro.mykkumi.feature.post

import android.os.Bundle
import android.text.Editable
import android.text.TextUtils.concat
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.HorizontalScrollView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager2.widget.ViewPager2
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexWrap
import com.google.android.flexbox.FlexboxLayoutManager
import com.google.android.flexbox.JustifyContent
import com.marastro.mykkumi.analytics.AnalyticsHelper
import com.marastro.mykkumi.common_ui.base.BaseFragment
import com.marastro.mykkumi.feature.post.confirm.PostDeleteImageConfirmDialog
import com.marastro.mykkumi.feature.post.databinding.FragmentPostEditBinding
import com.marastro.mykkumi.feature.post.hobbyCategory.HobbySubCategoryAdapter
import com.marastro.mykkumi.feature.post.imagePicker.ImagePickerArgument
import com.marastro.mykkumi.feature.post.imageWithPin.EditImageWithPinAdapter
import com.marastro.mykkumi.feature.post.imageWithPin.InputProductInfoBottomSheet
import com.marastro.mykkumi.feature.post.touchEvent.PostEditImageTouchCallback
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
import com.marastro.mykkumi.common_ui.R as StringR

@AndroidEntryPoint
class PostEditFragment : BaseFragment<FragmentPostEditBinding>(R.layout.fragment_post_edit),
    InputProductInfoBottomSheet.InputProductInfoListener {

    @Inject
    lateinit var analyticsHelper: AnalyticsHelper

    private val viewModel by viewModels<PostEditViewModel>()

    private final val MAX_POST_CONTENT_LENGTH = 2000      // 본문 글자 수
    private final val MAX_POST_CONTENT_HASHTAG_COUNT = 20 // 본문 해시태그 수

    private lateinit var selectPostImageListAdapter: SelectPostImageListAdapter // 이미지들 썸네일 나열
    private lateinit var editImageWithPinAdapter: EditImageWithPinAdapter // 이미지 편집 view (핀 추가할 수 있는)
    private lateinit var hobbySubCategoryAdapter: HobbySubCategoryAdapter // 카테고리

    private var navController: NavController? = null

    // 포스트 이미지 recyclerview 아이템 이동 콜백 변수 : 드래그 시 이동하는 거
    private lateinit var postEditImageTouchHelper: ItemTouchHelper

    private var isRestoringState = false
    private var isStartPosting = false

    private val waitingNotice = "${String(Character.toChars(0x1F525))} 준비 중입니다 ${String(Character.toChars(0x1F525))}"

    override fun onResume() {
        super.onResume()

        // image picker에서 선택한 이미지
        navController?.currentBackStackEntry?.savedStateHandle?.getLiveData<ImagePickerArgument>("selectImages")
            ?.observe(viewLifecycleOwner) { images ->
                if(!images.selectImages.isNullOrEmpty()) {
                    isStartPosting = true
                    viewModel.selectPostImage(images.selectImages)

                    images.selectImages.clear() // 리스트에 추가했다면 지우기 - view resume 될 때마다 추가되는 현상 제거
                }
            }

        // 상태 복원 중임을 표시
        isRestoringState = true

        // ViewPager2의 페이지를 ViewModel의 상태에 맞게 설정
        viewModel.selectImagePosition.value?.let { position ->
            binding.viewpagerPostEditImages.setCurrentItem(position, false)
        }

        // 포스트 작성 시작 후 이미지를 하나도 선택 안 한 상태로 뒤로가기 눌렀을 때 -> 이전으로 돌아가기
        if(navController?.currentDestination?.id == R.id.postEditFragment && !isStartPosting) {
            navController?.popBackStack()
        }

        // 상태 복원 완료
        isRestoringState = false
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Firebase Analytics 화면 이름 로깅
        analyticsHelper.logScreenView(getString(com.marastro.mykkumi.analytics.R.string.edit_post_screen))

        navController = view.findNavController()

        if(!viewModel.postEditUiState.value.isNullOrEmpty()) binding.relativeEmptyImage.visibility = View.GONE
        else binding.relativeEmptyImage.visibility = View.VISIBLE

        binding.textBtnUploadPost.setBackgroundResource(com.marastro.mykkumi.common_ui.R.drawable.shape_btn_round12_neutral50)
        binding.textBtnUploadPost.setTextColor(ContextCompat.getColor(requireContext(), com.marastro.mykkumi.common_ui.R.color.neutral_700))
        binding.textCountContent.visibility = View.GONE

        // 이미지 추가
        binding.btnAddPostImage.setOnClickListener(View.OnClickListener {
            viewModel.openImagePicker(navController)
        })
        binding.textBtnAddNewImage.setOnClickListener(View.OnClickListener {
            viewModel.openImagePicker(navController)
        })

        // 핀 추가
        binding.btnAddPin.setOnClickListener(View.OnClickListener {
            // 핀 최대 개수
            if (viewModel.currentPinList.value!!.size >= viewModel.MAX_PIN_COUNT) {
                showToast("핀은 최대 ${viewModel.MAX_PIN_COUNT}개까지 추가할 수 있어요.")
            }
            else {
                requestUpdateProductInfo(null)
            }
        })

        // 핀 자동 생성
        binding.btnAutoAddPin.setOnClickListener(View.OnClickListener {
            showToast(waitingNotice)
        })

        // 본문 입력 글자 수 제한
        binding.edittextInputContent.addTextChangedListener(object: TextWatcher {
            override fun afterTextChanged(s: Editable?) {

            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (!s.isNullOrEmpty()){
                    // 본문 글자수
                    if(s.length > MAX_POST_CONTENT_LENGTH) {
                        val subLength = count - (s.length - MAX_POST_CONTENT_LENGTH) // 삭제하고 남겨야 할 길이
                        binding.edittextInputContent.setText(concat(s.subSequence(0, start + subLength), s.subSequence(start + count, s.length)))

                        binding.edittextInputContent.setSelection(start + subLength) // 커서를 입력하고 있던 곳에
                        showToast(getString(StringR.string.notice_post_content_max_length))
                    }

                    // 해시태그 개수
                    if(s.count { it == '#' } > MAX_POST_CONTENT_HASHTAG_COUNT) {
                        // 20개 넘어가는 건 자르기 = 방금 입력된 문자
                        val hashTagIndex = s.indexOf('#', start)
                        binding.edittextInputContent.setText(concat(s.subSequence(0, hashTagIndex), s.subSequence(start + count, s.length)))

                        binding.edittextInputContent.setSelection(hashTagIndex) // 커서를 입력하고 있던 곳에
                        showToast(getString(StringR.string.notice_post_hashtag_max_count))
                    }

                    // 본문 입력 중
                    if (s.length > 0) {
                        binding.textCountContent.visibility = View.VISIBLE
                        binding.textCountContent.text = "${s.length}/${MAX_POST_CONTENT_LENGTH}"
                    }
                }
                else {
                    binding.textCountContent.visibility = View.GONE
                }
            }
        })

        // 이전 버튼
        binding.btnBack.setOnClickListener {
            navController?.popBackStack()
        }

        // 포스트 등록 버튼
        binding.textBtnUploadPost.setOnClickListener(View.OnClickListener {
            val content = binding.edittextInputContent.text.toString()
            viewModel.doneEditPost(
                content = content,
                noticeEmptyImage = getString(StringR.string.notice_not_select_image),
                noticeEmptyCategory = getString(StringR.string.notice_select_hobby_category_of_post),
                showToast = {
                    showToast(it)
                },
                navController = navController
            )
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

        if(_binding != null) {
            val emptyImage = binding.relativeEmptyImage
            emptyImage.viewTreeObserver.addOnGlobalLayoutListener {
                emptyImage.layoutParams.height = emptyImage.width
                emptyImage.requestLayout()
            }
        }

        initSelectPostImagesRecyclerView()
        initEditImageWithPinViewPager()
        initHobbyCategoryRecycler()
        observePostResource()

        viewModel.getHobbyCategoryList()
    }

    // 선택된 이미지 리스트 Recyclerview
    private fun initSelectPostImagesRecyclerView() {
        selectPostImageListAdapter = SelectPostImageListAdapter(
            requireContext(),
            viewModel,
            confirmDeleteImage ={
                confirmDeleteImage(it)
            },
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
        editImageWithPinAdapter = EditImageWithPinAdapter(
            context = requireContext(),
            resources = resources,
            lockViewPagerMoving = {
                lockViewPagerMoving()
            },
            unlockViewPagerMoving = {
                unlockViewPagerMoving()
            },
            updateProductInfo = {
                requestUpdateProductInfo(it)
            },
            deletePinOfImage = { position ->
                viewModel.deletePinOfImage(
                    position = position,
                    message = getString(com.marastro.mykkumi.common_ui.R.string.toast_delete_pin),
                    showToast = { showToast(it) }
                )
            },
        )
        binding.viewpagerPostEditImages.adapter = editImageWithPinAdapter
        binding.viewpagerPostEditImages.orientation = ViewPager2.ORIENTATION_HORIZONTAL

        // ViewPager 넘겼을 때도 선택 이미지 변경되는 것
        binding.viewpagerPostEditImages.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                if (viewModel.isDeleteImageState.value != true) {
                    viewModel.changeSelectImagePosition(position)
                    selectPostImageListAdapter.notifyDataSetChanged()
                }
            }
        })
    }

    private fun initHobbyCategoryRecycler() {
        hobbySubCategoryAdapter = HobbySubCategoryAdapter(
            requireContext(),
            viewModel
        )
        FlexboxLayoutManager(requireContext()).apply {
            flexWrap = FlexWrap.WRAP // 다음 아이템의 크기가 남은 여유공간보다 큰 경우 자동으로 줄바꿈
            flexDirection = FlexDirection.ROW
            justifyContent = JustifyContent.FLEX_START // 좌측 정렬
        }.let {
            binding.recyclerviewHobbySubCategory.layoutManager = it
            binding.recyclerviewHobbySubCategory.adapter = hobbySubCategoryAdapter
        }
    }

    // 이미지 추가되면 recyclerview adapter data 변경, 스크롤 이동 + 이미지 개수 확인
    // edit viewPager data도 변경
    private fun observePostResource() {
        // 추가된 이미지
        viewModel.postEditUiState.observe(viewLifecycleOwner, Observer {
            selectPostImageListAdapter.postImageList = it
            editImageWithPinAdapter.imageWithPinList = it

            if (!viewModel.postEditUiState.value.isNullOrEmpty()) { //  && !viewModel.isDeleteImageState && !isRestoringState
                binding.relativeSelectPostImageList.visibility = View.VISIBLE
                binding.viewLineSelectImages.visibility = View.VISIBLE
                binding.relativeEmptyImage.visibility = View.GONE

                viewModel.changeSelectImagePosition(viewModel.postEditUiState.value!!.size - 1)

                // 이미지 있음 -> 카테고리까지 있는지 확인하고 포스트 작성 버튼 활성화
                if(viewModel.isPossibleUploadPost()) {
                    binding.textBtnUploadPost.setBackgroundResource(com.marastro.mykkumi.common_ui.R.drawable.shape_btn_round12_primary)
                    binding.textBtnUploadPost.setTextColor(ContextCompat.getColor(requireContext(), com.marastro.mykkumi.common_ui.R.color.white))
                }
            }

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

            if (it.size > 0) // 추가된 이미지를 화면에 보여주기
                binding.viewpagerPostEditImages.setCurrentItem(it.size - 1, false)

            // 이미지 10개 선택됐으면 추가 버튼 가리기
            if (selectPostImageListAdapter.postImageList.size == viewModel.MAX_IMAGE_COUNT) {
                binding.btnAddPostImage.visibility = View.GONE
            } else {
                binding.btnAddPostImage.visibility = View.VISIBLE
            }
        })

        // 핀 추가
        viewModel.currentPinList.observe(viewLifecycleOwner, Observer {
            viewModel.selectImagePosition.value?.let {
                editImageWithPinAdapter.notifyDataSetChanged()
            }
        })

        // 핀 추가 및 변화 강조
        viewModel.newEditPin.observe(viewLifecycleOwner, Observer { newPin ->
            if(newPin != -1) {
                editImageWithPinAdapter.updateNewEditPin(
                    selectImagePosition = viewModel.selectImagePosition.value!!,
                    newEditPin = viewModel.newEditPin.value!!
                )
                viewModel.doneHighlightNewPin()
            }
            // else editImageWithPinAdapter.notifyDataSetChanged()
        })

        // 이미지 삭제 처리
        viewModel.isDeleteImageState.observe(viewLifecycleOwner, Observer {
            binding.viewpagerPostEditImages.setCurrentItem(viewModel.selectImagePosition.value!!, false)
            selectPostImageListAdapter.notifyDataSetChanged()
        })

        // 카테고리 추가
        viewModel.hobbyCategoryUiState.observe(viewLifecycleOwner, Observer {
            hobbySubCategoryAdapter.hobbySubCategoryList = it
            hobbySubCategoryAdapter.notifyDataSetChanged()
        })

        // 카테고리 선택
        viewModel.selectHobbyCategory.observe(viewLifecycleOwner, Observer {
            // 카테고리 선택됨 -> 이미지까지 있는지 확인하고 포스트 작성 버튼 활성화
            if(viewModel.isPossibleUploadPost()) {
                binding.textBtnUploadPost.setBackgroundResource(com.marastro.mykkumi.common_ui.R.drawable.shape_btn_round12_primary)
                binding.textBtnUploadPost.setTextColor(ContextCompat.getColor(requireContext(), com.marastro.mykkumi.common_ui.R.color.white))
            }
        })
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

    // 이미지 삭제 -> notice
    private fun confirmDeleteImage(imageIndex: Int) {
        val dialog = PostDeleteImageConfirmDialog(this, analyticsHelper)
        dialog.confirmPostReportListener { postId ->
            confirmAgree(postId)
        }
        dialog.cancelPostReportListener { confirmCancel() }

        dialog.show(imageIndex)
    }

    // 이미지 삭제
    fun confirmAgree(position: Int) {
        viewModel.deleteImage(position)
        viewModel.doneDeleteImage()
        showToast(getString(StringR.string.toast_done_delete))

        // 이미지 전체 삭제된 경우
        if(viewModel.postEditUiState.value.isNullOrEmpty()) {
            binding.relativeSelectPostImageList.visibility = View.GONE
            binding.viewLineSelectImages.visibility = View.GONE

            binding.relativeEmptyImage.visibility = View.VISIBLE
        }

        // 이미지가 없으므로 포스트 등록 버튼 비활성화
        binding.textBtnUploadPost.setBackgroundResource(com.marastro.mykkumi.common_ui.R.drawable.shape_btn_round12_neutral50)
        binding.textBtnUploadPost.setTextColor(ContextCompat.getColor(requireContext(), com.marastro.mykkumi.common_ui.R.color.neutral_700))
    }

    // 이미지 삭제 취소
    fun confirmCancel() {
        viewModel.doneDeleteImage()
    }

    // 핀 추가
    override fun submitProductInput(productName: String, productUrl: String?) {
        binding.scrollEditPost.smoothScrollToTop() // 핀 추가 후 스크롤 최상단으로 이동

        viewModel.addPinOfImage(productName, productUrl)

        // 핀 내용 입력 완료되면 스크롤 풀어주기
        binding.viewpagerPostEditImages.isUserInputEnabled = true
        binding.scrollEditPost.setScrollingEnabled(true)

        // showToast(getString(com.marastro.mykkumi.common_ui.R.string.notice_edit_post_add_pin))
    }

    // 핀 내용 수정을 위한 입력 요청
    private fun requestUpdateProductInfo(position: Int?) {
        // 핀 내용 입력되는 동안 스크롤 막기
        binding.viewpagerPostEditImages.isUserInputEnabled = false
        binding.scrollEditPost.setScrollingEnabled(false)

        val bundle = viewModel.requestProductInfoForPin(position)

        val bottomSheet = InputProductInfoBottomSheet().apply {
            setListener(this@PostEditFragment)
            setAnalyticsHelper(analyticsHelper)
        }
        bottomSheet.arguments = bundle
        bottomSheet.show(this.parentFragmentManager, bottomSheet.tag)
    }

    // 핀 내용(제품 정보) 수정
    override fun updateProductInput(position: Int, productName: String, productUrl: String?) {
        binding.scrollEditPost.smoothScrollToTop() // 핀 수정 후 스크롤 최상단으로 이동

        viewModel.updateProductInfoForPin(position, productName, productUrl)

        // 핀 내용 입력 완료되면 스크롤 풀어주기
        binding.viewpagerPostEditImages.isUserInputEnabled = true
        binding.scrollEditPost.setScrollingEnabled(true)
    }

    // pin 이동 중일 때는 viewPager 전환 안 되게 막기
    // 수직 스크롤도 막기
    private fun lockViewPagerMoving() {
        binding.viewpagerPostEditImages.isUserInputEnabled = false
        binding.scrollEditPost.setScrollingEnabled(false)
    }

    // pin 이동 끝나면 viewPager 전환 가능하게 풀어주기
    // 수직 스크롤도 풀어주기
    private fun unlockViewPagerMoving() {
        binding.viewpagerPostEditImages.isUserInputEnabled = true
        binding.scrollEditPost.setScrollingEnabled(true)
    }

    private fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }
}