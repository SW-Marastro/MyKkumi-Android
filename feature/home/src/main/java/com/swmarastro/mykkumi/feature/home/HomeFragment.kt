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
                    image = listOf("https://private-user-images.githubusercontent.com/76805879/258024989-6804fbeb-6e54-4e3c-a1b5-be0260b80fc8.png?jwt=eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJnaXRodWIuY29tIiwiYXVkIjoicmF3LmdpdGh1YnVzZXJjb250ZW50LmNvbSIsImtleSI6ImtleTUiLCJleHAiOjE3MTk4MzgyMTMsIm5iZiI6MTcxOTgzNzkxMywicGF0aCI6Ii83NjgwNTg3OS8yNTgwMjQ5ODktNjgwNGZiZWItNmU1NC00ZTNjLWExYjUtYmUwMjYwYjgwZmM4LnBuZz9YLUFtei1BbGdvcml0aG09QVdTNC1ITUFDLVNIQTI1NiZYLUFtei1DcmVkZW50aWFsPUFLSUFWQ09EWUxTQTUzUFFLNFpBJTJGMjAyNDA3MDElMkZ1cy1lYXN0LTElMkZzMyUyRmF3czRfcmVxdWVzdCZYLUFtei1EYXRlPTIwMjQwNzAxVDEyNDUxM1omWC1BbXotRXhwaXJlcz0zMDAmWC1BbXotU2lnbmF0dXJlPWE3NjM1NDA3OTYyZDhiYTBmYjNkZWU2OTM2ZjM1MDdkM2Y5NzIyZTE0OGY0YTJkNTRiYjRhMDc5ZjMwMmFmNmYmWC1BbXotU2lnbmVkSGVhZGVycz1ob3N0JmFjdG9yX2lkPTAma2V5X2lkPTAmcmVwb19pZD0wIn0.nXp0joUnG93ksA2WwnMkWPAPSOEjPErSsTYvHrbwk14",
                        "https://private-user-images.githubusercontent.com/76805879/258025006-bf0ab08e-6b23-4fd1-a2d3-afbe7cd40d03.png?jwt=eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJnaXRodWIuY29tIiwiYXVkIjoicmF3LmdpdGh1YnVzZXJjb250ZW50LmNvbSIsImtleSI6ImtleTUiLCJleHAiOjE3MTk4MzgyMTMsIm5iZiI6MTcxOTgzNzkxMywicGF0aCI6Ii83NjgwNTg3OS8yNTgwMjUwMDYtYmYwYWIwOGUtNmIyMy00ZmQxLWEyZDMtYWZiZTdjZDQwZDAzLnBuZz9YLUFtei1BbGdvcml0aG09QVdTNC1ITUFDLVNIQTI1NiZYLUFtei1DcmVkZW50aWFsPUFLSUFWQ09EWUxTQTUzUFFLNFpBJTJGMjAyNDA3MDElMkZ1cy1lYXN0LTElMkZzMyUyRmF3czRfcmVxdWVzdCZYLUFtei1EYXRlPTIwMjQwNzAxVDEyNDUxM1omWC1BbXotRXhwaXJlcz0zMDAmWC1BbXotU2lnbmF0dXJlPWUwNzc2YjU5YWE0ZDAzMzY0MTcxN2VlMWU2OGVmOWI5ZTdhYjVhZDlkY2VlMzA0OWVlMzE4NGNiNDU4Y2Y4ODYmWC1BbXotU2lnbmVkSGVhZGVycz1ob3N0JmFjdG9yX2lkPTAma2V5X2lkPTAmcmVwb19pZD0wIn0.u0gFNhuXAjyvdwwJssHJ3e9jE0wGzxnlXXdQ5tsqSFo",
                        "https://private-user-images.githubusercontent.com/76805879/258025036-255e1215-e3b0-4337-a196-4a6625bbf5e2.png?jwt=eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJnaXRodWIuY29tIiwiYXVkIjoicmF3LmdpdGh1YnVzZXJjb250ZW50LmNvbSIsImtleSI6ImtleTUiLCJleHAiOjE3MTk4MzgyMTMsIm5iZiI6MTcxOTgzNzkxMywicGF0aCI6Ii83NjgwNTg3OS8yNTgwMjUwMzYtMjU1ZTEyMTUtZTNiMC00MzM3LWExOTYtNGE2NjI1YmJmNWUyLnBuZz9YLUFtei1BbGdvcml0aG09QVdTNC1ITUFDLVNIQTI1NiZYLUFtei1DcmVkZW50aWFsPUFLSUFWQ09EWUxTQTUzUFFLNFpBJTJGMjAyNDA3MDElMkZ1cy1lYXN0LTElMkZzMyUyRmF3czRfcmVxdWVzdCZYLUFtei1EYXRlPTIwMjQwNzAxVDEyNDUxM1omWC1BbXotRXhwaXJlcz0zMDAmWC1BbXotU2lnbmF0dXJlPTJiNGUwYjQ3NDhmYjQ3Y2UzMDczNmM3YTM3NjE3NGFkYjcwNTljZjRkM2RkNWE1YTI4NTczOTg3NzZjOGM5MDkmWC1BbXotU2lnbmVkSGVhZGVycz1ob3N0JmFjdG9yX2lkPTAma2V5X2lkPTAmcmVwb19pZD0wIn0.GbviSt0lJeSO3xEvEzKxGTT2CfZas1Zekdo6425PL_k",
                        "https://private-user-images.githubusercontent.com/76805879/294633456-48e0bd0a-bd72-4f57-9490-1903c9cdffad.png?jwt=eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJnaXRodWIuY29tIiwiYXVkIjoicmF3LmdpdGh1YnVzZXJjb250ZW50LmNvbSIsImtleSI6ImtleTUiLCJleHAiOjE3MTk4Mzg0NDMsIm5iZiI6MTcxOTgzODE0MywicGF0aCI6Ii83NjgwNTg3OS8yOTQ2MzM0NTYtNDhlMGJkMGEtYmQ3Mi00ZjU3LTk0OTAtMTkwM2M5Y2RmZmFkLnBuZz9YLUFtei1BbGdvcml0aG09QVdTNC1ITUFDLVNIQTI1NiZYLUFtei1DcmVkZW50aWFsPUFLSUFWQ09EWUxTQTUzUFFLNFpBJTJGMjAyNDA3MDElMkZ1cy1lYXN0LTElMkZzMyUyRmF3czRfcmVxdWVzdCZYLUFtei1EYXRlPTIwMjQwNzAxVDEyNDkwM1omWC1BbXotRXhwaXJlcz0zMDAmWC1BbXotU2lnbmF0dXJlPWJhZTM0YmZlY2RjZGRiNzVhZTYzMzAyYjgwM2NhMDRhY2I4MjQ4ZmU2ZGVlODhjZGEyY2QyMTNmMmZhMjlhN2EmWC1BbXotU2lnbmVkSGVhZGVycz1ob3N0JmFjdG9yX2lkPTAma2V5X2lkPTAmcmVwb19pZD0wIn0.EL9Ivd891w9ePYuiQajgnRpHQrAhWykWwHOn1YQK5Pc"),
                    category = "공예/DIY",
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