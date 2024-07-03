package com.swmarastro.mykkumi.feature.home

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager2.widget.ViewPager2
import com.swmarastro.mykkumi.common_ui.base.BaseFragment
import com.swmarastro.mykkumi.domain.entity.BannerListVO
import com.swmarastro.mykkumi.domain.entity.PostItemVO
import com.swmarastro.mykkumi.domain.entity.PostListVO
import com.swmarastro.mykkumi.domain.entity.PostWriterVO
import com.swmarastro.mykkumi.feature.home.banner.HomeBannerViewPagerAdapter
import com.swmarastro.mykkumi.feature.home.databinding.FragmentHomeBinding
import com.swmarastro.mykkumi.common_ui.post.PostImagesAdapter
import com.swmarastro.mykkumi.feature.home.post.PostListAdapter
import dagger.hilt.android.AndroidEntryPoint
import java.util.Timer
import java.util.TimerTask

@AndroidEntryPoint
class HomeFragment : BaseFragment<FragmentHomeBinding>(R.layout.fragment_home) {

    private val viewModel by viewModels<HomeViewModel>({ requireActivity() })
    private lateinit var bannerAdapter: HomeBannerViewPagerAdapter
    private lateinit var postListAdapter: PostListAdapter
    private lateinit var postItemImageAdapter: PostImagesAdapter
    private lateinit var timer: Timer

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.lifecycleOwner = this
        lifecycleScope.launchWhenCreated {
            initView()
        }
        super.onViewCreated(view, savedInstanceState)

        // 배너 테스트용
        /*val bannerList: HomeBannerListVO = HomeBannerListVO(
            mutableListOf(
                HomeBannerVO(1, "https://avatars.githubusercontent.com/u/76805879?v=4"),
                HomeBannerVO(2, "https://avatars.githubusercontent.com/u/168630394?s=200&v=4")
            )
        )
        initBannerViewPager(bannerList)*/

        startAutoScroll()
        onClickBannerAll() // 배너 > + 버튼 선택 시 전체 리스트 페이지로 이동

        setPostList() // 포스트 리스트
    }

    override suspend fun initView() {
        bind {
            vm = viewModel
        }
        setHomeBanner() // 배너

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

        /*binding.lifecycleOwner = this
        lifecycleScope.launchWhenStarted {
            viewModel.bannerImageUiState.collect { bitmaps ->
                binding.viewpagerBanner.adapter?.let {
                    if (it is HomeBannerViewPagerAdapter) {
                        it.setImages(bitmaps)
                    }
                }

            }
        }
        viewModel.loadImages(banners.banners)*/

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
    private suspend fun setHomeBanner() {
        viewModel.setHomeBanner()
        viewModel.bannerListUiState.collect { response ->
            initBannerViewPager(response)
        }
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
        view?.findNavController()?.navigate(R.id.action_navigate_fragment_to_home_banner_detail)
    }

    // 배너 > + 버튼 클릭 -> 배너 전체 리스트 페이지로 이동
    private fun onClickBannerAll() {
        binding.btnBannerMore.setOnClickListener {
            view?.findNavController()?.navigate(R.id.action_navigate_fragment_to_home_banner_all)
        }
    }

    // 포스트 리스트 recyclerview
    private fun initPostRecyclerView(posts: PostListVO) {
        postListAdapter = PostListAdapter(
            posts.posts.toMutableList()
        )
        binding.recyclerviewPostList.layoutManager = LinearLayoutManager(
            context,
            LinearLayoutManager.VERTICAL,
            false
        )
        binding.recyclerviewPostList.adapter = postListAdapter
    }

    // 포스트 내용 세팅
    private fun setPostList() {
        val test: PostListVO = PostListVO(
            listOf(
                PostItemVO(
                    id = 1,
                    category = "공예/DIY",
                    image = listOf("https://avatars.githubusercontent.com/u/76805879?v=4",
                        "https://user-images.githubusercontent.com/76805879/236372564-dd445ac3-3f7e-4032-b872-f1073ef5775e.jpg",
                        "https://avatars.githubusercontent.com/u/168630394?s=400&u=a7c52691e6a47f8419477b917aa547a2ce3e8a6c&v=4"),
                    subCategory = "다이어리 꾸미기",
                    writer = PostWriterVO(
                        nickname = "마이꾸미",
                        profileImage = "https://avatars.githubusercontent.com/u/76805879?v=4",
                    ),
                    content = "마라스트로"
                ),
                PostItemVO(
                    id = 1,
                    image = listOf("https://user-images.githubusercontent.com/76805879/236372564-dd445ac3-3f7e-4032-b872-f1073ef5775e.jpg",
                        "https://avatars.githubusercontent.com/u/76805879?v=4",
                        "https://avatars.githubusercontent.com/u/168630394?s=400&u=a7c52691e6a47f8419477b917aa547a2ce3e8a6c&v=4"),
                    category = "동물",
                    subCategory = "고양이",
                    writer = PostWriterVO(
                        nickname = "츄르",
                        profileImage = "https://avatars.githubusercontent.com/u/168630394?s=48&v=4",
                    ),
                    content = "마라스트로"
                ),
                PostItemVO(
                    id = 1,
                    image = listOf("https://avatars.githubusercontent.com/u/168630394?s=400&u=a7c52691e6a47f8419477b917aa547a2ce3e8a6c&v=4",
                        "https://avatars.githubusercontent.com/u/76805879?v=4"),
                    category = "공예/DIY",
                    subCategory = "다이어리 꾸미기",
                    writer = PostWriterVO(
                        nickname = "마이꾸미",
                        profileImage = "https://avatars.githubusercontent.com/u/76805879?v=4",
                    ),
                    content = "마라스트로"
                )
            ),
        )
        initPostRecyclerView(test)
    }

    override fun onDestroyView() {
        _binding = null
        timer.cancel()
        super.onDestroyView()
    }
}