package com.marastro.mykkumi.feature.mypage.editProfile

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.findNavController
import com.bumptech.glide.Glide
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexWrap
import com.google.android.flexbox.FlexboxLayoutManager
import com.google.android.flexbox.JustifyContent
import com.marastro.mykkumi.analytics.AnalyticsHelper
import com.marastro.mykkumi.common_ui.base.BaseFragment
import com.marastro.mykkumi.feature.mypage.R
import com.marastro.mykkumi.feature.mypage.databinding.FragmentEditProfileBinding
import com.marastro.mykkumi.feature.mypage.editProfile.hobbyCategory.HobbySubCategoryAdapter
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class EditProfileFragment : BaseFragment<FragmentEditProfileBinding>(
    R.layout.fragment_edit_profile
) {
    @Inject
    lateinit var analyticsHelper: AnalyticsHelper

    private val viewModel by viewModels<EditProfileViewModel>()

    private var navController: NavController? = null

    private lateinit var hobbySubCategoryAdapter: HobbySubCategoryAdapter // 카테고리

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Firebase Analytics 화면 이름 로깅
        analyticsHelper.logScreenView(getString(com.marastro.mykkumi.analytics.R.string.edit_profile_screen))

        navController = view.findNavController()

        viewModel.getLoginUser(
            showToast = {
                showToast(it)
            }
        )

        // 뒤로가기
        binding.btnBack.setOnClickListener {
            navController?.popBackStack()
        }
    }

    override suspend fun initView() {
        bind {
            vm = viewModel
        }
        observeData()

        viewModel.getHobbyCategoryList()
        initHobbyCategoryRecycler()
    }

    private fun observeData() {
        // 사용자 정보 observe
        viewModel.editProfileUiState.observe(viewLifecycleOwner, Observer {
            if(it != null) {
                binding.edittextInputNickname.setText(it.nickname)
                binding.textEmail.text = it.email
                binding.edittextInputIntroduction.setText(it.introduction)

                Glide
                    .with(requireContext())
                    .load(it.profileImage)
                    .placeholder(com.marastro.mykkumi.common_ui.R.drawable.img_profile_default)
                    .circleCrop()
                    .into(binding.imgEditProfileImage)
            }
        })

        // 카테고리 세팅
        // 카테고리 추가
        viewModel.hobbyCategoryUiState.observe(viewLifecycleOwner, Observer {
            hobbySubCategoryAdapter.hobbySubCategoryList = it
            hobbySubCategoryAdapter.notifyDataSetChanged()
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

    private fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }
}