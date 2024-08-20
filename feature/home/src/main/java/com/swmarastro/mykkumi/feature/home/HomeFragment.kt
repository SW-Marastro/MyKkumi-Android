package com.swmarastro.mykkumi.feature.home

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ScrollView
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager2.widget.ViewPager2
import com.swmarastro.mykkumi.common_ui.base.BaseFragment
import com.swmarastro.mykkumi.common_ui.report.PostReportConfirmDialog
import com.swmarastro.mykkumi.domain.entity.BannerItemVO
import com.swmarastro.mykkumi.domain.entity.BannerListVO
import com.swmarastro.mykkumi.domain.entity.HomePostItemVO
import com.swmarastro.mykkumi.feature.home.banner.HomeBannerViewPagerAdapter
import com.swmarastro.mykkumi.feature.home.databinding.FragmentHomeBinding
import com.swmarastro.mykkumi.feature.home.post.PostListAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import java.util.Timer
import java.util.TimerTask

@AndroidEntryPoint
class HomeFragment : BaseFragment<FragmentHomeBinding>(R.layout.fragment_home) {
    private val viewModel by viewModels<HomeViewModel>()

    private lateinit var bannerAdapter: HomeBannerViewPagerAdapter
    private lateinit var postListAdapter: PostListAdapter

    private lateinit var timer: Timer

    private var navController: NavController? = null

    private val waitingNotice = "${String(Character.toChars(0x1F525))} 준비 중입니다 ${String(Character.toChars(0x1F525))}"

    override fun onResume() {
        super.onResume()

        setHomeBanner() // 배너
        setPostList() // 포스트
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        navController = view.findNavController()
        binding.includeListLoading.visibility = View.VISIBLE
        binding.emptyPostList.visibility = View.GONE

        binding.scrollHomeBannerNPost.isSmoothScrollingEnabled = true
        binding.scrollHomeBannerNPost.post {
            postListAdapter.postList.clear()
            binding.scrollHomeBannerNPost.fullScroll(ScrollView.FOCUS_UP)
        }

        startAutoScroll()

        binding.btnSearch.setOnClickListener {
            waitNotice()
        }
        binding.btnNotice.setOnClickListener {
            waitNotice()
        }

        // 포스트 리스트 추가
        viewModel.postCursor.observe(viewLifecycleOwner, Observer {
            if(postListAdapter.postList.size != viewModel.postListUiState.value.size) {
                val postList = viewModel.postListUiState.value ?: mutableListOf()
                postListAdapter.postList.clear()
                postListAdapter.postList.addAll(postList)
                val rangeEnd = postList.size
                if (rangeEnd != 0) {
                    var count = rangeEnd % (viewModel.postLimit.value ?: 1)
                    if (count == 0) count = viewModel.postLimit.value ?: rangeEnd
                    postListAdapter.notifyItemRangeInserted(rangeEnd - count, rangeEnd)
                }
            }
            else if(viewModel.bannerListUiState.value.isNullOrEmpty()) {
                binding.includeListLoading.visibility = View.GONE
                binding.emptyPostList.visibility = View.VISIBLE
            }
        })
    }

    override suspend fun initView() {
        bind {
            vm = viewModel
        }

        setHomeBanner() // 배너
        setPostList() // 포스트
    }

    // 배너 viewpager
    private fun initBannerViewPager(banners: MutableList<BannerItemVO>) {
        bannerAdapter = HomeBannerViewPagerAdapter(
            banners,
            onClickBannerItem = {
                onClickBannerItem(it)
            },
            navigateBannerAll = {
                navigateBannerAll()
            }
        )
        binding.viewpagerBanner.adapter = bannerAdapter
        binding.viewpagerBanner.orientation = ViewPager2.ORIENTATION_HORIZONTAL
        if(banners.size != 0) binding.viewpagerBanner.setCurrentItem(1000 / banners.size * banners.size, false) // 좌측으로도 배너 전환 가능하도록

        // 배너 페이지 표시
        binding.textBannerTotalPage.text = "/" + banners.size
        if(!banners.isEmpty()) binding.textBannerCurrentPage.text = "1"

        // 배너가 수동으로 변경되면, 자동 전환되는 타이머를 리셋 - 변경된 시점부터 3초 카운트
        binding.viewpagerBanner.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                timer.cancel()    // 기존 타이머 중지
                startAutoScroll() // 타이머 리셋

                // 현재 배너 페이지 표시
                if(banners.size != 0)
                    binding.textBannerCurrentPage.text = (binding.viewpagerBanner.currentItem % banners.size + 1).toString()
            }
        })
    }

    // 배너 내용 세팅
    private fun setHomeBanner() {
        viewModel.setHomeBanner()
        viewModel.bannerListUiState
            .onEach {
                initBannerViewPager(it)
                binding.skeletonBanner.visibility = View.GONE
            }
            .launchIn(lifecycleScope)
    }

    // 배너 자동 전환
    private fun startAutoScroll() {
        timer = Timer()
        timer.schedule(object : TimerTask() {
            override fun run() {
                binding.viewpagerBanner.post {
                    binding.viewpagerBanner.currentItem = (binding.viewpagerBanner.currentItem + 1) % Int.MAX_VALUE
                }
            }
        }, 5000, 3000) // 5초마다 전환
    }

    // 배너 클릭 -> 배너 상세 페이지 이동
    private fun onClickBannerItem(bannerId: Int) {
        viewModel.selectHomeBanner(bannerId)
        viewModel.navigateBannerDetail(navController)
    }

    // 포스트 리스트 recyclerview
    private fun initPostRecyclerView() {
        postListAdapter = PostListAdapter(
            requireContext(),
            navController,
            waitNotice = {
                waitNotice()
            },
            reportPost = {
                postReportConfirm(it)
            },
            viewModel
        )
        binding.recyclerviewPostList.layoutManager = LinearLayoutManager(
            context,
            LinearLayoutManager.VERTICAL,
            false
        )
        binding.recyclerviewPostList.adapter = postListAdapter

        // 스크롤 다 내려가면 다음 데이터 받아오기
        binding.scrollHomeBannerNPost.setOnScrollChangeListener { v, scrollX, scrollY, oldScrollX, oldScrollY ->
            if (v is ScrollView) {
                // 스크롤이 최하단까지 내려갔는지 확인
                val scroll = v.getChildAt(v.childCount - 1)
                val diff = scroll.bottom - (v.height + v.scrollY)

                // includeListLoading이 보이기 시작하는 시점 확인
                val loadingViewTop = binding.includeListLoading.top

                val scrollYPosition = v.scrollY + v.height
                if(viewModel.postCursor.value.isNullOrEmpty()) {
                    binding.includeListLoading.visibility = View.GONE
                }
                else if (scrollYPosition >= loadingViewTop + 1 && !viewModel.isPostListLoading.value) {
                    viewModel.setPostList(true)
                }
//                else if (diff == 0 && !viewModel.isPostListLoading.value) {
//                    setNextPostList()
//                }
            }
        }
    }

    // 포스트 내용 세팅
    private fun setPostList() {
        viewModel.setPostList(false)
        initPostRecyclerView()
    }

    // 배너 전체 리스트 페이지로 이동
    private fun navigateBannerAll() {
        viewModel.navigateBannerAll(navController)
    }

    private fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

    private fun waitNotice() {
        showToast(waitingNotice)
    }

    // 포스트 신고 확인 Dialog
    private fun postReportConfirm(postId: Int) {
        val dialog = PostReportConfirmDialog(this)
        dialog.setOnClickListener { postId ->
            Toast.makeText(context, getString(com.swmarastro.mykkumi.common_ui.R.string.post_report_confirm_clear_toast), Toast.LENGTH_SHORT).show()
            Log.d("test", "신고 포스트: ${postId}")
        }

        dialog.show(postId)
    }

    override fun onDestroyView() {
        _binding = null
        timer.cancel()
        super.onDestroyView()
    }
}