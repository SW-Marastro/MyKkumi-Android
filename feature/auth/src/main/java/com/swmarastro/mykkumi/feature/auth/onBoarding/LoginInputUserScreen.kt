package com.swmarastro.mykkumi.feature.auth.onBoarding

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.provider.Settings
import androidx.activity.compose.rememberLauncherForActivityResult
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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.app.ActivityCompat.startActivityForResult
import androidx.navigation.NavController
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.swmarastro.mykkumi.feature.auth.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

private const val MAX_NICKNAME_LENGTH = 16
private const val MIN_NICKNAME_LENGTH = 3
private val NICKNAME_REGEX = Regex("^[a-zA-Z0-9._\\-ㄱ-ㅎ가-힣ㅏ-ㅣ]*$")

// 사용자 정보 입력 페이지 - 프로필 이미지, 닉네임
@ExperimentalPermissionsApi
@Composable
fun LoginInputUserScreen(navController: NavController) {
    val localContext = LocalContext.current
    var nickname : String by remember { mutableStateOf("") }

    // 갤러리, 카메라 접근 권한 허용 요청
    val multiplePermissionsState = rememberMultiplePermissionsState(
        permissions = mutableListOf(
            android.Manifest.permission.CAMERA,
        ).apply {
            // sdk version 28 이하 - 사진 촬영 후 저장 권한 허용 요청
            if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.P) {
                add(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
            }
            // sdk version 32 이하 - 파일 접근 권한 허용 요청
            if(Build.VERSION.SDK_INT <= Build.VERSION_CODES.S) {
                add(android.Manifest.permission.READ_EXTERNAL_STORAGE)
            }
            // sdk version 33 이상 - 이미지 접근 권한 허용 요청
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                add(android.Manifest.permission.READ_MEDIA_IMAGES)
            }
        }
    )

    // 권한을 거부했을 때, 설정 페이지로 이동시켜서 권한 허용 요청을 하기 위해 필요함
    val scaffoldState: ScaffoldState = rememberScaffoldState()
    val coroutineScope: CoroutineScope = rememberCoroutineScope()
    val permissioSnackbarMessage = stringResource(id = R.string.notice_permission_revoke_go_setting)
    val permissioSnackbarAction = stringResource(id = R.string.action_permission_revoke_go_setting)

    // 갤러리에서 이미지 선택
    val galleryLauncher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        uri?.let {

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
                painter = painterResource(
                id = com.swmarastro.mykkumi.common_ui.R.drawable.img_profile_default),
                contentDescription = "default profile image",
                modifier = Modifier
                    .size(160.dp)
                    .align(Alignment.CenterHorizontally)
                    .padding(3.dp)
                    .clip(CircleShape)
                    .clickable {
                        // 갤러리, 카메라 접근 권한 허용 요청
                        // 권한 허용됨
                        if (multiplePermissionsState.allPermissionsGranted) {
                            // 갤러리 열기
                            val type = "image/*"
                            galleryLauncher.launch(type)
//                            val intent = Intent(Intent.ACTION_PICK)
//                            intent.type = "image/*"
//                            intent.action = Intent.ACTION_GET_CONTENT
//                            startActivityForResult(intent)
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
                value = nickname,
                onValueChange = {
                    // 입력 문자 제한 - 한글, 영문자, 숫자, _, -, .
                    if(it.matches(NICKNAME_REGEX)) {
                        // 닉네임 최대 길이 제한
                        if (it.length <= MAX_NICKNAME_LENGTH) nickname = it
                        else nickname = it.substring(0, MAX_NICKNAME_LENGTH)
                    }
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
            if (nickname.length < MIN_NICKNAME_LENGTH) {
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
        }
    }
}

//private fun setImgUri(imgUri: Uri) {
//    imgUri.let {
//        val bitmap: Bitmap
//        if (Build.VERSION.SDK_INT < 28) {
//            bitmap = MediaStore.Images.Media.getBitmap(
//                this.contentResolver,
//                imgUri
//            )
//
//        } else {
//            val source =
//                ImageDecoder.createSource(this.contentResolver, imgUri)
//            bitmap = ImageDecoder.decodeBitmap(source)
//        }
//    }
//}