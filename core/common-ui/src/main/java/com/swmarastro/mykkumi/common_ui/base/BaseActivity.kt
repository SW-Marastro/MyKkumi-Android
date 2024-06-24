package com.swmarastro.mykkumi.common_ui.base

import android.os.Bundle
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding

abstract class BaseActivity<T: ViewDataBinding>(
    @LayoutRes private val layoutId: Int
) : AppCompatActivity(){
    var _binding: T? = null
    val binding get() = _binding!!

    override fun onCreate(
        savedInstanceState: Bundle?
    ) {
        super.onCreate(savedInstanceState)
        _binding = DataBindingUtil.setContentView(this, layoutId)
        setContentView(binding.root)
    }

    abstract suspend fun initView()

    protected inline fun bind(block: T?.() -> Unit) {
        _binding.apply(block)
    }

    override fun onDestroy() {
        _binding?.unbind()
        super.onDestroy()
    }
}