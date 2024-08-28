package com.swmarastro.mykkumi.android

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupWithNavController
import com.swmarastro.mykkumi.android.databinding.ActivityMainBinding
import com.swmarastro.mykkumi.common_ui.permission.ImagePermissionUtils
import dagger.hilt.android.AndroidEntryPoint

/*
@AndroidEntryPoint: Activity, Fragment, View, Service, BroadcastReceiver 같은 Android Component에 사용할 수 있는 어노테이션
이를 적용한 컴포넌트 내에서 @Inject가 달린 필드에 의존성 주입을 함
*/
@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private val viewModel by viewModels<MainViewModel>()

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

        // 아이콘에 색상 입히지 않고 아이콘 이미지 그대로 보여주기
        binding.bottomNav.itemIconTintList = null

        // 최상위 화면을 제외하고는 BottomNavigation Bar 없애기
        setBottomNavigation()

        // 포스트 작성 버튼
        binding.bottomNav.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.btn_add_post -> {
                    val intent = viewModel.navigateLogin()
                    if(intent == null) { // 로그인 됨
                        // 이미지 권한 요청하기
                        checkPermissionsAndProceed()
                    }
                    else { // 로그인 안 됨
                        startActivity(intent)
                    }

                    if(viewModel.currentMenu.value != null) binding.bottomNav.selectedItemId = viewModel.currentMenu.value!!
                    false
                }
                else -> {
                    viewModel.selectMenu(item.itemId)
                    NavigationUI.onNavDestinationSelected(item, navController)
                    true
                }
            }
        }

        
        // 회원가입 도중에 닉네임 입력 안 하고 종료한 유저인지 확인
        viewModel.checkIsSignDone(
            navController = navController,
            showToast = {
                showToast(it)
            }
        )
    }

    private fun setBottomNavigation() {
        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                com.swmarastro.mykkumi.feature.home.R.id.homeFragment,
                com.swmarastro.mykkumi.feature.around.R.id.aroundFragment,
                com.swmarastro.mykkumi.feature.shopping.R.id.shoppingFragment,
                com.swmarastro.mykkumi.feature.mypage.R.id.mypageFragment -> {
                    binding.bottomNav.visibility = View.VISIBLE
                }
                else -> {
                    binding.bottomNav.visibility = View.GONE
                }
            }
        }
    }

    override fun onBackPressed() {
        if(navController.currentDestination?.id == com.swmarastro.mykkumi.feature.home.R.id.homeFragment ||
            navController.currentDestination?.id == com.swmarastro.mykkumi.feature.around.R.id.aroundFragment ||
            navController.currentDestination?.id == com.swmarastro.mykkumi.feature.shopping.R.id.shoppingFragment ||
            navController.currentDestination?.id == com.swmarastro.mykkumi.feature.mypage.R.id.mypageFragment) {
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

    private fun checkPermissionsAndProceed() {
        if (ImagePermissionUtils.allPermissionsGranted(this)) {
            // 포스트 작성 페이지로 이동
            viewModel.navigatePostEdit(navController)
        } else if (ImagePermissionUtils.shouldShowRationale(this)) {
            val intent = Intent(
                Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                Uri.parse("package:${this.packageName}")
            ).apply {
                addCategory(Intent.CATEGORY_DEFAULT)
                flags = Intent.FLAG_ACTIVITY_NEW_TASK
            }

            ImagePermissionUtils.showSettingsSnackbar(
                this, binding.root,
                intent
            )
        } else {
            ImagePermissionUtils.requestPermissions(this)
        }
    }
    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }


    override fun onDestroy() {
        _binding = null
        super.onDestroy()
    }
}