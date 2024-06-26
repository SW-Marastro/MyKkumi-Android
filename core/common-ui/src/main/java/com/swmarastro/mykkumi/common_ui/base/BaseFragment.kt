package com.swmarastro.mykkumi.common_ui.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope

abstract class BaseFragment<T: ViewDataBinding>(
    @LayoutRes private val layoutId: Int
) : Fragment() {

    lateinit var binding: T

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, layoutId, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.lifecycleOwner = this@BaseFragment
        lifecycleScope.launchWhenCreated {
            initView()
        }
        super.onViewCreated(view, savedInstanceState)
    }

    abstract suspend fun initView()

    override fun onDestroyView() {
        binding.unbind()
        super.onDestroyView()
    }

    protected inline fun bind(block: T.() -> Unit) {
        binding.apply(block)
    }
}