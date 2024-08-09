package com.swmarastro.mykkumi.feature.auth.onBoarding

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.provider.Settings
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.Scaffold
import androidx.compose.material.ScaffoldState
import androidx.compose.material.SnackbarResult
import androidx.compose.material.rememberScaffoldState
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.FileProvider
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import coil.compose.rememberImagePainter
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.swmarastro.mykkumi.common_ui.permission.ImagePermissionUtils
import com.swmarastro.mykkumi.feature.auth.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Date

// 닉네임 형식 & 길이
private const val MAX_NICKNAME_LENGTH = 16
private const val MIN_NICKNAME_LENGTH = 3
private val NICKNAME_REGEX = Regex("^[a-zA-Z0-9._\\-ㄱ-ㅎ가-힣ㅏ-ㅣ]*$")

// 이미지 Croppy
private const val RC_CROP_IMAGE = 101
private lateinit var localContext: Context
// 사용자 정보 입력 페이지 - 프로필 이미지, 닉네임
@ExperimentalPermissionsApi
@Composable
fun LoginInputUserScreen(
    activity: ComponentActivity,
    selectedHobbies: String?
//    selectedHobbies: LongArray?
) {
    localContext = LocalContext.current
    val loginViewModel: LoginInputUserViewModel = ViewModelProvider(
        LocalContext.current as ComponentActivity
    ).get(LoginInputUserViewModel::class.java)

    //Log.d("select category", selectedHobbies.toString())

    // 로그인 종료 상태 체크
    loginViewModel.finishLoginUiState.observe(activity, Observer {
        activity.finish()
    })
    // 갤러리, 카메라 접근 권한 허용 요청
    val multiplePermissionsState = rememberMultiplePermissionsState(
        permissions = ImagePermissionUtils.multiplePermissions
    )

    // 권한을 거부했을 때, 설정 페이지로 이동시켜서 권한 허용 요청을 하기 위해 필요함
    val scaffoldState: ScaffoldState = rememberScaffoldState()
    val coroutineScope: CoroutineScope = rememberCoroutineScope()
    val permissioSnackbarMessage = stringResource(id = R.string.notice_permission_revoke_go_setting)
    val permissioSnackbarAction = stringResource(id = R.string.action_permission_revoke_go_setting)

    // 이미지 Croppy
    val imageCroppyLauncher = rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            Log.d("test 333", result.data.toString())
        }
    }
    // 갤러리에서 이미지 선택
    val imagePickerLauncher = rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val data: Intent? = result.data
            val selectedImageUri: Uri? = data?.data

            // 갤러리에서 이미지를 선택했을 경우
            if (selectedImageUri != null) {
                loginViewModel.selectProfileImage(selectedImageUri)
            }
            // 카메라로 촬영했을 경우
            else if(loginViewModel.cameraImagePath.value != null) {

                loginViewModel.selectProfileImage(loginViewModel.cameraImagePath.value!!)
            }
        }
    }



    Scaffold(
        scaffoldState = scaffoldState
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
                .padding(innerPadding),
        ) {
            Spacer(
                modifier = Modifier.height(30.dp)
            )
            Image(
                painter = when(loginViewModel.profileImage.collectAsState().value) {
                    is String -> rememberAsyncImagePainter(
                        model = loginViewModel.profileImage.collectAsState().value
                    )
                    is Int -> painterResource(
                        id = loginViewModel.profileImage.collectAsState().value as Int
                    )
                    is Uri -> rememberAsyncImagePainter(
                        model = loginViewModel.profileImage.collectAsState().value
                    )
                    else -> painterResource(
                        id = com.swmarastro.mykkumi.common_ui.R.drawable.img_profile_default
                    )
                },
                contentDescription = "default profile image",
                contentScale = ContentScale.Crop, // CenterCrop
                modifier = Modifier
                    .size(160.dp)
                    .align(Alignment.CenterHorizontally)
                    .padding(3.dp)
                    .clip(CircleShape)
                    .clickable {
                        // 갤러리, 카메라 접근 권한 허용 요청
                        // 권한 허용됨
                        if (multiplePermissionsState.allPermissionsGranted) {
                            // 이미지 가져오기
                            ImagePermissionUtils.chooserImageIntent(
                                localContext,
                                imagePickerLauncher,
                            ) { uri -> // 카메라에서 선택했을 경우
                                loginViewModel.setCameraImagePath(uri)
                            }
                        }

                        // 권한을 요청한 적이 있지만 허용되지 않음
                        else if (multiplePermissionsState.shouldShowRationale) {
                            // 권한 허용을 위해 앱 디테일 설정 페이지로 이동시키기 위함
                            val intent = Intent(
                                Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                                Uri.parse("package:${localContext.packageName}")
                            ).apply {
                                addCategory(Intent.CATEGORY_DEFAULT)
                                flags = Intent.FLAG_ACTIVITY_NEW_TASK
                            }

                            // 앱 디테일 설정 페이지로 이동할지 물어보는 Snackbar
                            coroutineScope.launch {
                                val snackbarResult = scaffoldState.snackbarHostState.showSnackbar(
                                    message = permissioSnackbarMessage,
                                    actionLabel = permissioSnackbarAction
                                )
                                when (snackbarResult) {
                                    SnackbarResult.ActionPerformed -> localContext.startActivity(
                                        intent
                                    )

                                    SnackbarResult.Dismissed -> null
                                }
                            }
                        }

                        // 권한을 요청한 적이 없음
                        else multiplePermissionsState.launchMultiplePermissionRequest()
                    }
            )
    
            Spacer(
                modifier = Modifier.height(30.dp)
            )
    
            BasicTextField(
                value = loginViewModel.nickname.collectAsState().value,
                onValueChange = {
                    loginViewModel.onNicknameChange(it)
                },
                modifier = Modifier
                    .height(20.dp)
                    .fillMaxWidth()
                    .padding(
                        horizontal = 40.dp,
                    )
                    .drawWithContent { // underline
                        drawContent()
                        drawLine(
                            color = Color.Gray,
                            start = Offset(
                                x = 0f,
                                y = size.height + 1.dp.toPx(),
                            ),
                            end = Offset(
                                x = size.width,
                                y = size.height + 1.dp.toPx(),
                            ),
                            strokeWidth = 1.dp.toPx(),
                        )
                    },
            )
    
            Spacer(
                modifier = Modifier.height(10.dp)
            )
    
            // 최소글자수 미충족 경고
            if (loginViewModel.nickname.value.length < MIN_NICKNAME_LENGTH) {
                Text(
                    text = stringResource(id = R.string.notice_nickname_min_length),
                    color = Color.Red,
                    fontSize = 12.sp,
                    modifier = Modifier
                        .padding(
                            horizontal = 40.dp,
                        )
                )
            }

            // Spacer로 중간 공간을 채움
            Spacer(modifier = Modifier.weight(1f))

            Button(
                onClick = { loginViewModel.confirmNickname { showToast(it) } },
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Text(text = stringResource(id = R.string.clear_login_all_btn))
            }
        }
    }
}

private fun showToast(message: String) {
    Toast.makeText(localContext, message, Toast.LENGTH_SHORT).show()
}