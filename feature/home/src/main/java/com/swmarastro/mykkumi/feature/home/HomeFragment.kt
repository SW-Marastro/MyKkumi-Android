package com.swmarastro.mykkumi.feature.home

import android.os.Bundle
import android.view.View
import android.widget.ScrollView
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager2.widget.ViewPager2
import com.swmarastro.mykkumi.common_ui.base.BaseFragment
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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        navController = view.findNavController()

        startAutoScroll()
        onClickBannerAll() // 배너 > + 버튼 선택 시 전체 리스트 페이지로 이동
    }

    override suspend fun initView() {
        bind {
            vm = viewModel
        }
        setHomeBanner() // 배너
        setPostList() // 포스트
    }

    // 배너 viewpager
    private fun initBannerViewPager(banners: BannerListVO) {
        bannerAdapter = HomeBannerViewPagerAdapter(
            banners.banners.toMutableList(),
            onClickBannerItem = {
                onClickBannerItem(it)
            }
        )
        binding.viewpagerBanner.adapter = bannerAdapter
        binding.viewpagerBanner.orientation = ViewPager2.ORIENTATION_HORIZONTAL
        binding.viewpagerBanner.setCurrentItem(1000, false) // 좌측으로도 배너 전환 가능하도록

        // 배너 페이지 표시
        binding.textBannerTotalPage.text = "/" + banners.banners.size
        if(!banners.banners.isEmpty()) binding.textBannerCurrentPage.text = "1"

        // 배너가 수동으로 변경되면, 자동 전환되는 타이머를 리셋 - 변경된 시점부터 3초 카운트
        binding.viewpagerBanner.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                timer.cancel()    // 기존 타이머 중지
                startAutoScroll() // 타이머 리셋

                // 현재 배너 페이지 표시
                if(banners.banners.size != 0)
                    binding.textBannerCurrentPage.text = (binding.viewpagerBanner.currentItem % banners.banners.size + 1).toString()
            }
        })
    }

    // 배너 내용 세팅
    private fun setHomeBanner() {
        viewModel.setHomeBanner()
        viewModel.bannerListUiState
            .onEach {
                initBannerViewPager(it)
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
        }, 3000, 3000) // 3초마다 전환 -> 너무 빠른가?
    }

    // 배너 클릭 -> 배너 상세 페이지 이동
    private fun onClickBannerItem(bannerId: Int) {
        viewModel.selectHomeBanner(bannerId)
        viewModel.navigateBannerDetail(navController)
    }

    // 배너 > + 버튼 클릭 -> 배너 전체 리스트 페이지로 이동
    private fun onClickBannerAll() {
//        binding.btnBannerMore.setOnClickListener {
//            viewModel.navigateBannerAll(navController)
//        }
    }

    // 포스트 리스트 recyclerview
    private fun initPostRecyclerView(posts: MutableList<HomePostItemVO>) {
        postListAdapter = PostListAdapter(
            requireContext(),
            navController
        )
        binding.recyclerviewPostList.layoutManager = LinearLayoutManager(
            context,
            LinearLayoutManager.VERTICAL,
            false
        )
        binding.recyclerviewPostList.adapter = postListAdapter
        postListAdapter.postList = posts

        // 스크롤 다 내려가면 다음 데이터 받아오기
        binding.scrollHomeBannerNPost.setOnScrollChangeListener { v, scrollX, scrollY, oldScrollX, oldScrollY ->
            if (v is ScrollView) {
                // 스크롤이 최하단까지 내려갔는지 확인
                val scroll = v.getChildAt(v.childCount - 1)
                val diff = scroll.bottom - (v.height + v.scrollY)
                if(viewModel.postCursor.value.isNullOrEmpty()) {
                    binding.includeListLoading.visibility = View.GONE
                }
                else if (diff == 0 && !viewModel.isPostListLoading.value) {
                    setNextPostList()
                }
            }
        }
    }

    // 포스트 내용 세팅
    private fun setPostList() {
        viewModel.setPostList(false)
        viewModel.postListUiState
            .onEach {
                initPostRecyclerView(it)
            }
            .launchIn(viewLifecycleOwner.lifecycleScope)
    }

    // 포스트 무한 스크롤 -> 스크롤 최하단 도달 시 다음 데이터 요청
    private fun setNextPostList() {
        viewModel.setPostList(true)
//        viewModel.postListUiState.collect {
//
//        }

//        lifecycleScope.launch {
//            viewModel.postListUiState.collect {
//                Log.d("test", "test1")
//                postListAdapter.postList = it
//                Log.d("test", "test2")
//
//                if (viewModel.postCursor.value.isNullOrEmpty()) {
//                    binding.includeListLoading.visibility = View.GONE
//                }
//            }
//        }

        viewModel.postListUiState
            .onEach {
                postListAdapter.postList = it

                if (viewModel.postCursor.value.isNullOrEmpty()) {
                    binding.includeListLoading.visibility = View.GONE
                }
            }
            .launchIn(viewLifecycleOwner.lifecycleScope)
    }

    override fun onDestroyView() {
        _binding = null
        timer.cancel()
        super.onDestroyView()
    }
}