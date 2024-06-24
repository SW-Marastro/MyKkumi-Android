package com.swmarastro.mykkumi.feature.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
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
    ): View? {
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
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_toolbar_menu),
                contentDescription = null,
                modifier = Modifier
                    .size(24.dp)
            )
            SearchBar()
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
                .background(Color(context?.resources!!.getColor(com.swmarastro.mykkumi.common_ui.R.color.gray_DD)))
                .padding(horizontal = 10.dp, vertical = 5.dp)
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_search),
                contentDescription = null,
                modifier = Modifier
                    .size(16.dp)
            )
            TextField(
                value = text,
                onValueChange = { text = it },
                modifier = Modifier
                    .height(13.dp)
            )
        }
    }
}