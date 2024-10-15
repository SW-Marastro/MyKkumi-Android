package com.marastro.mykkumi.feature.auth.onBoarding

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Scaffold
import androidx.compose.material.ScaffoldState
import androidx.compose.material.SnackbarResult
import androidx.compose.material.Surface
import androidx.compose.material.rememberScaffoldState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import coil.compose.rememberAsyncImagePainter
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.marastro.mykkumi.common_ui.permission.ImagePermissionUtils
import com.marastro.mykkumi.feature.auth.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

// 닉네임 형식 & 길이
private const val MAX_NICKNAME_LENGTH = 16
private const val MIN_NICKNAME_LENGTH = 3
private val NICKNAME_REGEX = Regex("^[a-zA-Z0-9._\\-ㄱ-ㅎ가-힣ㅏ-ㅣ]*$")

// 이미지 Croppy
private const val RC_CROP_IMAGE = 101
private lateinit var localContext: Context

// 사용자 정보 입력 페이지 - 프로필 이미지, 닉네임
@OptIn(ExperimentalMaterialApi::class, ExperimentalGlideComposeApi::class)
@ExperimentalPermissionsApi
@Composable
fun LoginInputUserScreen(
    activity: ComponentActivity,
    selectedHobbies: List<Long>?
) {
    localContext = LocalContext.current
    val keyboardController = LocalSoftwareKeyboardController.current

    val viewModel: LoginInputUserViewModel = ViewModelProvider(
        LocalContext.current as ComponentActivity
    ).get(LoginInputUserViewModel::class.java)
    viewModel.setHobbyCategory(selectedHobbies)

    var isKeyboardVisible by remember { mutableStateOf(false) }

    // 키보드 상태 감지
    val insets = WindowInsets.ime
    val density = LocalDensity.current
    val keyboardHeightDp = with(density) { insets.getBottom(density) / density.density }.dp

    // 키보드가 올라와 있으면 높이가 0보다 크기 때문에 이를 사용해 상태를 업데이트
    LaunchedEffect(keyboardHeightDp) {
        isKeyboardVisible = keyboardHeightDp > 0.dp
    }

    // 로그인 종료 상태 체크
    viewModel.finishLoginUiState.observe(activity, Observer {
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
                viewModel.selectProfileImage(selectedImageUri)
            }
            // 카메라로 촬영했을 경우
            else if(viewModel.cameraImagePath.value != null) {
                viewModel.selectProfileImage(viewModel.cameraImagePath.value!!)
            }
        }
    }

    Scaffold(
        scaffoldState = scaffoldState,
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .background(Color.White),
        ) {
            Text(
                text = stringResource(id = R.string.title_login_user_info),
                fontSize = 20.sp,
                fontFamily = FontFamily(Font(com.marastro.mykkumi.common_ui.R.font.pretendard_bold)),
                color = colorResource(id = com.marastro.mykkumi.common_ui.R.color.neutral_900),
                modifier = Modifier
                    .padding(top = 40.dp, start = 20.dp)
            )

            // 키보드 올라왔을 때는 프로필 이미지 설정하는 부분 숨기고 닉네임 입력에 포커싱
            if (!isKeyboardVisible) {
                Spacer(
                    modifier = Modifier.height(40.dp)
                )

                Text(
                    text = stringResource(id = R.string.label_login_user_info_profile),
                    fontSize = 15.sp,
                    fontFamily = FontFamily(Font(com.marastro.mykkumi.common_ui.R.font.pretendard_semibold)),
                    color = colorResource(id = com.marastro.mykkumi.common_ui.R.color.neutral_900),
                    modifier = Modifier
                        .padding(start = 20.dp)
                )
                Spacer(
                    modifier = Modifier.height(12.dp)
                )

                Image(
                    painter = when (viewModel.profileImage.collectAsState().value) {
                        is String -> rememberAsyncImagePainter(
                            model = viewModel.profileImage.collectAsState().value
                        )

                        is Int -> painterResource(
                            id = viewModel.profileImage.collectAsState().value as Int
                        )

                        is Uri -> rememberAsyncImagePainter(
                            model = viewModel.profileImage.collectAsState().value
                        )

                        else -> painterResource(
                            id = com.marastro.mykkumi.common_ui.R.drawable.img_profile_default
                        )
                    },
                    contentDescription = "default profile image",
                    contentScale = ContentScale.Crop, // CenterCrop
                    modifier = Modifier
                        .size(88.dp)
                        .align(Alignment.CenterHorizontally)
                        .clip(CircleShape)
                )

//                GlideImage(
//                    model = viewModel.profileImage.collectAsState().value as Uri,
//                    contentDescription = "default profile image",
//                    contentScale = ContentScale.Crop, // CenterCrop
//                    modifier = Modifier
//                        .size(88.dp)
//                        .align(Alignment.CenterHorizontally)
//                        .clip(CircleShape),
//                )

                Spacer(
                    modifier = Modifier.height(24.dp)
                )

                Surface(
                    onClick = {
                        // 갤러리, 카메라 접근 권한 허용 요청
                        // 권한 허용됨
                        if (multiplePermissionsState.allPermissionsGranted) {
                            // 이미지 가져오기
                            ImagePermissionUtils.chooserImageIntent(
                                localContext,
                                imagePickerLauncher,
                            ) { uri -> // 카메라에서 선택했을 경우
                                viewModel.setCameraImagePath(uri)
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
                    },
                    color = colorResource(id = com.marastro.mykkumi.common_ui.R.color.secondary_color),
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .clip(RoundedCornerShape(12.dp))
                ) {
                    Text(
                        fontSize = 13.sp,
                        fontFamily = FontFamily(Font(com.marastro.mykkumi.common_ui.R.font.pretendard_semibold)),
                        text = stringResource(id = R.string.btn_login_user_info_profile),
                        color = colorResource(id = com.marastro.mykkumi.common_ui.R.color.neutral_900),
                        modifier = Modifier
                            .padding(vertical = 12.dp, horizontal = 19.5.dp)
                    )
                }
            }

            Spacer(
                modifier = Modifier.height(40.dp)
            )

            Text(
                text = stringResource(id = R.string.label_login_user_info_nickname),
                fontSize = 15.sp,
                fontFamily = FontFamily(Font(com.marastro.mykkumi.common_ui.R.font.pretendard_semibold)),
                color = colorResource(id = com.marastro.mykkumi.common_ui.R.color.neutral_900),
                modifier = Modifier
                    .padding(start = 20.dp)
            )

            Spacer(
                modifier = Modifier.height(12.dp)
            )

            Box(
                modifier = Modifier
                    .padding(
                        horizontal = 20.dp,
                    )
                    .border(
                        width = 1.dp,
                        color = (
                                if (viewModel.nickname.value.isEmpty())
                                    colorResource(id = com.marastro.mykkumi.common_ui.R.color.neutral_200)
                                else
                                    colorResource(id = com.marastro.mykkumi.common_ui.R.color.neutral_800)
                                ),
                        shape = RoundedCornerShape(12.dp),
                    )
                    .padding(horizontal = 16.dp)
            ) {
                BasicTextField(
                    value = viewModel.nickname.collectAsState().value,
                    onValueChange = {
                        viewModel.onNicknameChange(
                            it,
                            showToast = { message ->
                                showToast(message)
                            }
                        )
                    },
                    textStyle = TextStyle(
                        color = colorResource(id = com.marastro.mykkumi.common_ui.R.color.neutral_800),
                        fontFamily = FontFamily(Font(com.marastro.mykkumi.common_ui.R.font.pretendard_semibold)),
                        fontSize = 16.sp
                    ),
                    keyboardOptions = KeyboardOptions(
                        imeAction = ImeAction.Done,
                        keyboardType = KeyboardType.Text
                    ),
                    keyboardActions = KeyboardActions(
                        onDone = { // enter 키 눌렸을 때
                            keyboardController?.hide() // 키보드 숨기기
                        }
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 14.dp)
                )
                if (viewModel.nickname.value.isEmpty()) {
                    // placeholder
                    Text(
                        text = stringResource(id = R.string.placeholder_user_nickname),
                        style = TextStyle(
                            color = colorResource(id = com.marastro.mykkumi.common_ui.R.color.neutral_400),
                            fontFamily = FontFamily(Font(com.marastro.mykkumi.common_ui.R.font.pretendard_semibold)),
                            fontSize = 16.sp
                        ),
                        modifier = Modifier
                            .align(Alignment.CenterStart)
                    )
                }
                else {
                    // 내용 지우기 버튼
                    Image(
                        painter = painterResource(id = R.drawable.ic_delete_edit_text),
                        contentDescription = "btn delete nickname",
                        modifier = Modifier
                            .size(24.dp)
                            .align(Alignment.CenterEnd)
                            .clickable {
                                viewModel.deleteNickname()
                            },
                        contentScale = ContentScale.Fit,
                    )
                }
            }

            Spacer(
                modifier = Modifier.height(12.dp),
            )

            Column(
                modifier = Modifier
                    .padding(start = 36.dp)
            ) {
                Row {
                    Image(
                        painter = painterResource(id = R.drawable.ic_dot),
                        contentDescription = "notice dot",
                        modifier = Modifier
                            .width(4.dp)
                            .align(Alignment.CenterVertically),
                        contentScale = ContentScale.Fit,
                    )
                    Text(
                        text = stringResource(id = R.string.notice_user_nickname_input_string1),
                        color = colorResource(id = com.marastro.mykkumi.common_ui.R.color.neutral_400),
                        fontFamily = FontFamily(Font(com.marastro.mykkumi.common_ui.R.font.pretendard_medium)),
                        fontSize = 12.sp,
                        modifier = Modifier
                            .padding(start = 6.dp)
                    )
                }

                Row {
                    Image(
                        painter = painterResource(id = R.drawable.ic_dot),
                        contentDescription = "notice dot",
                        modifier = Modifier
                            .width(4.dp)
                            .align(Alignment.CenterVertically),
                        contentScale = ContentScale.Fit,
                    )
                    Text(
                        text = stringResource(id = R.string.notice_user_nickname_input_string2),
                        color = colorResource(id = com.marastro.mykkumi.common_ui.R.color.neutral_400),
                        fontFamily = FontFamily(Font(com.marastro.mykkumi.common_ui.R.font.pretendard_medium)),
                        fontSize = 12.sp,
                        modifier = Modifier
                            .padding(start = 6.dp)
                    )
                }
            }

            // Spacer로 중간 공간을 채움
            Spacer(modifier = Modifier.weight(1f))

            Surface(
                onClick = { viewModel.confirmNickname { showToast(it) } },
                contentColor = (
                    if (viewModel.nickname.value.length < MIN_NICKNAME_LENGTH)
                        colorResource(id = com.marastro.mykkumi.common_ui.R.color.neutral_300)
                    else
                        colorResource(id = com.marastro.mykkumi.common_ui.R.color.white)
                ),
                color = (
                    if (viewModel.nickname.value.length < MIN_NICKNAME_LENGTH)
                        colorResource(id = com.marastro.mykkumi.common_ui.R.color.neutral_50)
                    else
                        colorResource(id = com.marastro.mykkumi.common_ui.R.color.primary_color)
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 10.dp)
                    .clip(RoundedCornerShape(12.dp))
            ) {
                Text(
                    text = stringResource(id = R.string.clear_login_all_btn),
                    color = (
                        if (viewModel.nickname.value.length < MIN_NICKNAME_LENGTH)
                            colorResource(id = com.marastro.mykkumi.common_ui.R.color.neutral_300)
                        else
                            colorResource(id = com.marastro.mykkumi.common_ui.R.color.white)
                    ),
                    fontSize = 15.sp,
                    fontFamily = FontFamily(Font(com.marastro.mykkumi.common_ui.R.font.pretendard_semibold)),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 15.5.dp)
                        .wrapContentWidth()
                )
            }
        }
    }
}

private fun showToast(message: String) {
    Toast.makeText(localContext, message, Toast.LENGTH_SHORT).show()
}