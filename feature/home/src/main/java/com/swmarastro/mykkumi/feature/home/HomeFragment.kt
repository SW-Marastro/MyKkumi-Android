package com.swmarastro.mykkumi.feature.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Text
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
                Row {
                    Text(text = "compose")
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

}