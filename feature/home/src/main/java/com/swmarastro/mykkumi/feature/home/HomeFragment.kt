package com.swmarastro.mykkumi.feature.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import coil.compose.AsyncImage
import com.swmarastro.mykkumi.common_ui.base.BaseFragment
import com.swmarastro.mykkumi.feature.home.databinding.FragmentHomeBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : BaseFragment<FragmentHomeBinding>(R.layout.fragment_home) {

    private val viewModel by viewModels<HomeViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DataBindingUtil.inflate<FragmentHomeBinding?>(
            inflater, R.layout.fragment_home, container, false
        ).apply {
            composeViewHome.setContent {
                Box(
                    modifier = Modifier
                        .fillMaxSize(),
                ) {
                    Column (
                        modifier = Modifier
                            .fillMaxSize(),
                    ) {
                        HomeToolbar() // 상단바
                        BannerCarousel() // 배너
                    }
                    // 작성 버튼 - Floating
                    FloatingActionButton(
                        onClick = { /*onClick()*/ },
                        modifier = Modifier
                            .align(Alignment.BottomEnd)
                            .padding(10.dp)
                    ) {
                        Text(text = "작성")
                    }
                    // AddPostBtn()
                }
            }
        }

        return _binding!!.root
    }

    override suspend fun initView() {
        bind {
            vm = viewModel
        }
    }

    // 상단바
    @Composable
    private fun HomeToolbar(
        modifier: Modifier = Modifier
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .padding(10.dp)
                .fillMaxWidth(),
        ) {
            Image( // 메뉴 햄버거바 icon
                painter = painterResource(id = R.drawable.ic_toolbar_menu),
                contentDescription = "메뉴 버튼",
                modifier = Modifier
                    .size(24.dp),
            )
            Spacer(modifier = Modifier.width(4.dp))
            SearchBar() // 검색창
            Spacer(modifier = Modifier.width(4.dp))
            Image( // 알림 icon
                painter = painterResource(id = R.drawable.ic_notice),
                contentDescription = "알림",
                modifier = Modifier
                    .size(24.dp),
            )
            Spacer(modifier = Modifier.width(4.dp))
            Image( // 장바구니 icon
                painter = painterResource(id = R.drawable.ic_shopping_cart),
                contentDescription = "장바구니",
                modifier = Modifier
                    .size(24.dp),
            )
        }
    }

    // 검색창
    @Composable
    private fun SearchBar(
        modifier: Modifier = Modifier
    ) {
        var text by remember { mutableStateOf("") }
        Row(
            modifier = Modifier
                .background(colorResource(id = com.swmarastro.mykkumi.common_ui.R.color.gray_DD))
                .padding(horizontal = 10.dp, vertical = 5.dp)
                .fillMaxWidth(fraction = 0.8f),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Image( // 검색 icon
                painter = painterResource(id = R.drawable.ic_search),
                contentDescription = "검색",
                modifier = Modifier
                    .size(16.dp),
            )
            Spacer(modifier = Modifier.width(4.dp))
            BasicTextField( // 검색어 입력창
                value = text,
                onValueChange = { text = it },
                singleLine = true,
                textStyle = TextStyle(
                    fontSize = 12.sp,
                    color = colorResource(id = com.swmarastro.mykkumi.common_ui.R.color.black),
                ),
            )
        }
    }

    // 배너
    @OptIn(ExperimentalFoundationApi::class)
    @Composable
    private fun BannerCarousel(
        modifier: Modifier = Modifier,
        bannerList: List<String> = listOf(
            "https://avatars.githubusercontent.com/u/76805879?v=4",
            "https://avatars.githubusercontent.com/u/168630394?s=400&u=a7c52691e6a47f8419477b917aa547a2ce3e8a6c&v=4"
        )
    ) {
        val pagerState = rememberPagerState(
            pageCount = { bannerList.size }
        )
        LaunchedEffect(key1 = Unit) {
            var initialPage = Int.MAX_VALUE / 2

            while (initialPage % bannerList.size != 0) {
                initialPage++
            }
            pagerState.scrollToPage(initialPage)
        }
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 10.dp, vertical = 5.dp)
        ) {
            HorizontalPager(
                state = pagerState,
            ) { index ->
                bannerList.getOrNull(index % (bannerList.size))?.let { image ->
                    AsyncImage(
                        model = image,
                        contentDescription = "배너 이미지",
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(100.dp),
                        contentScale = ContentScale.Crop
                    )
                }
            }
        }
    }



    // 포스트 작성 버튼
    @Composable
    private fun AddPostBtn(
        modifier: Modifier = Modifier,
        // onClick: () -> Unit
    ) {
        FloatingActionButton(
            onClick = { /*onClick()*/ },
        ) {
            Text(text = "작성")
        }
    }
}