package com.swmarastro.mykkumi.feature.post

import androidx.fragment.app.viewModels
import com.swmarastro.mykkumi.common_ui.base.BaseFragment
import com.swmarastro.mykkumi.feature.post.databinding.FragmentPostEditBinding


class PostEditFragment : BaseFragment<FragmentPostEditBinding>(R.layout.fragment_post_edit){

    private val viewModel by viewModels<PostEditViewModel>()

    override suspend fun initView() {
        bind {
            vm = viewModel
        }
    }

}