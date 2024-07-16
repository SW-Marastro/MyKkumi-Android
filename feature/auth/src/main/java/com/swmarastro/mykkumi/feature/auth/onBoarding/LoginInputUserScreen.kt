package com.swmarastro.mykkumi.feature.auth.onBoarding

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.swmarastro.mykkumi.feature.auth.R

private const val MAX_NICKNAME_LENGTH = 16
private const val MIN_NICKNAME_LENGTH = 3
private val NICKNAME_REGEX = Regex("^[a-zA-Z0-9._\\-ㄱ-ㅎ가-힣ㅏ-ㅣ]*$")

// 사용자 정보 입력 페이지 - 프로필 이미지, 닉네임
@Composable
fun LoginInputUserScreen(navController: NavController) {
    var nickname : String by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
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