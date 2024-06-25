package com.swmarastro.mykkumi.android

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.swmarastro.mykkumi.android.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint

/*
@AndroidEntryPoint: Activity, Fragment, View, Service, BroadcastReceiver 같은 Android Component에 사용할 수 있는 어노테이션
이를 적용한 컴포넌트 내에서 @Inject가 달린 필드에 의존성 주입을 함
*/
@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private var _binding: ActivityMainBinding? = null
    private val binding get() = _binding!!

    // NavController 선언
    private lateinit var navController: NavController

    private var waitTime = 0L

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.fragment_container_view) as NavHostFragment
        navController = navHostFragment.navController

        binding.bottomNav.setupWithNavController(navController)
    }

    override fun onBackPressed() {
        var fragmentList = supportFragmentManager.fragments

        if(fragmentList.size <= 1) {
            if(System.currentTimeMillis() - waitTime >= 1500 ) {
                waitTime = System.currentTimeMillis()
                Toast.makeText(this, R.string.back_pressed_toast, Toast.LENGTH_SHORT).show()
            } else {
                finish() // 액티비티 종료
            }
        }
        else {
            super.onBackPressed()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding?.unbind()
    }
}