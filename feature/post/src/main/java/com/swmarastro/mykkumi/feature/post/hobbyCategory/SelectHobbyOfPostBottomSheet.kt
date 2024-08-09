package com.swmarastro.mykkumi.feature.post.hobbyCategory

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.swmarastro.mykkumi.common_ui.base.BaseBottomSheetFragment
import com.swmarastro.mykkumi.feature.post.R
import com.swmarastro.mykkumi.feature.post.databinding.FragmentSelectHobbyOfPostBottomSheetBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SelectHobbyOfPostBottomSheet : BaseBottomSheetFragment<FragmentSelectHobbyOfPostBottomSheetBinding>(R.layout.fragment_select_hobby_of_post_bottom_sheet) {
    private val viewModel by viewModels<SelectHobbyOfPostViewModel>()

    private var selectHobbyOfPostListener: SelectHobbyOfPostListener? = null

    private lateinit var hobbyCategoryAdapter: HobbyCategoryAdapter
    private var hobbyCategorySpan: Int = 1

    interface SelectHobbyOfPostListener {
        fun doneSelectHobby(categoryId: Long)
    }

    fun setListener(selectHobbyOfPostListener: SelectHobbyOfPostListener) {
        this.selectHobbyOfPostListener = selectHobbyOfPostListener
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.hobbyCategoryUiState.observe(viewLifecycleOwner, Observer {
            hobbyCategoryAdapter.hobbyCategoryList = it
            hobbyCategoryAdapter.notifyDataSetChanged()
        })

        // 선택 완료
        binding.btnDoneSelectHobbyCategory.setOnClickListener {
            binding.btnDoneSelectHobbyCategory.isEnabled = false // 버튼 선택 막기 -> 연속 요청 방지
            if (viewModel.selectedHobby.value == -1L)
                Toast.makeText(context, "select_hobby_category_of_post", Toast.LENGTH_SHORT).show()

            else {
                viewModel.selectedHobby.value?.let {
                    selectHobbyOfPostListener?.doneSelectHobby(
                        it
                    )
                }
                dismiss()
            }
            binding.btnDoneSelectHobbyCategory.isEnabled = true // 버튼 선택 풀기
        }
    }

    override suspend fun initView() {
        bind {
            vm = viewModel
        }

        initRecyclerView()
        viewModel.getHobbyCategoryList()
    }

    private fun initRecyclerView() {
        val screenWidth = resources.displayMetrics.widthPixels
        val itemWidth = resources.getDimensionPixelSize(R.dimen.item_hobby_sub_category)

        hobbyCategorySpan = screenWidth / itemWidth

        hobbyCategoryAdapter = HobbyCategoryAdapter(
            hobbyCategorySpan,
            viewModel
        )
        binding.recyclerviewHobbyCategory.apply {
            layoutManager = LinearLayoutManager(
                context,
                LinearLayoutManager.VERTICAL,
                false
            )
            adapter = hobbyCategoryAdapter
            isNestedScrollingEnabled = false
        }
    }
}