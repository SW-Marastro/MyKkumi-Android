package com.swmarastro.mykkumi.common_ui.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.lifecycleScope
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

abstract class BaseBottomSheetFragment<T: ViewDataBinding>(
    @LayoutRes private val layoutId: Int
) : BottomSheetDialogFragment() {
    protected var _binding: T? = null
    protected val binding get() = _binding ?: throw IllegalStateException("Binding is not initialized")

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = DataBindingUtil.inflate(inflater, layoutId, container, false)
        return _binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.lifecycleOwner = this@BaseBottomSheetFragment
        lifecycleScope.launchWhenCreated {
            initView()
        }
        super.onViewCreated(view, savedInstanceState)
    }

    abstract suspend fun initView()

    protected inline fun bind(block: T.() -> Unit) {
        binding.apply(block)
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}