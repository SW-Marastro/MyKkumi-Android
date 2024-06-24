package com.swmarastro.mykkumi.feature.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
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
        binding = DataBindingUtil.inflate<FragmentHomeBinding?>(
            inflater, R.layout.fragment_home, container, false
        ).apply {
            composeViewHome.setContent {
                Column {
                    HomeToolbar()
                }
            }
        }

        return binding.root
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
            Image(
                painter = painterResource(id = R.drawable.ic_toolbar_menu),
                contentDescription = null,
                modifier = Modifier
                    .size(24.dp),
            )
            Spacer(modifier = Modifier.width(4.dp))
            SearchBar()
            Spacer(modifier = Modifier.width(4.dp))
            Image(
                painter = painterResource(id = R.drawable.ic_notice),
                contentDescription = null,
                modifier = Modifier
                    .size(24.dp),
            )
            Spacer(modifier = Modifier.width(4.dp))
            Image(
                painter = painterResource(id = R.drawable.ic_shopping_cart),
                contentDescription = null,
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
            Image(
                painter = painterResource(id = R.drawable.ic_search),
                contentDescription = null,
                modifier = Modifier
                    .size(16.dp),
            )
            Spacer(modifier = Modifier.width(4.dp))
            BasicTextField(
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
}