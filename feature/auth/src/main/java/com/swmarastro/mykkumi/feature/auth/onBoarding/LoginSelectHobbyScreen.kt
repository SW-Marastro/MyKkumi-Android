package com.swmarastro.mykkumi.feature.auth.onBoarding

import androidx.activity.ComponentActivity
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.compose.LifecycleEventEffect
import androidx.navigation.NavController
import com.swmarastro.mykkumi.domain.entity.HobbyCategoryItemVO
import com.swmarastro.mykkumi.domain.entity.HobbySubCategoryItemVO
import com.swmarastro.mykkumi.feature.auth.R

// 관심 취미 선택
@Composable
fun LoginSelectHobbyScreen(
    navController: NavController,
) {
    val viewModel: LoginSelectHobbyViewModel = ViewModelProvider(
        LocalContext.current as ComponentActivity
    ).get(LoginSelectHobbyViewModel::class.java)

    LifecycleEventEffect(event = Lifecycle.Event.ON_CREATE) {
        viewModel.getHobbyCategoryList()
    }

    Box(modifier = Modifier
        .fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
                .padding(start = 20.dp)
        ) {
            Text(
                text = stringResource(id = R.string.title_login_select_hobby),
                fontSize = 20.sp,
                fontFamily = FontFamily(Font(com.swmarastro.mykkumi.common_ui.R.font.pretendard_bold)),
                color = colorResource(id = com.swmarastro.mykkumi.common_ui.R.color.neutral_900),
                modifier = Modifier
                    .padding(top = 40.dp, bottom = 24.dp)
            )

            // 취미 카테고리 : 대분류 > 소분류
            LazyColumn(
                modifier = Modifier
                    .weight(1f) // Spacer로 중간 공간을 채움
            ) {
                items(
                    items = viewModel.hobbyCategoryUiState.value,
                    itemContent = {
                        HobbyCategoryItem(
                            hobby = it,
                            viewModel = viewModel
                        )
                    }
                )
            }
        }

        Row(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .zIndex(1f)
                .border(
                    width = 1.dp,
                    color = colorResource(id = com.swmarastro.mykkumi.common_ui.R.color.neutral_100),
                    shape = RoundedCornerShape(topStart = 0.dp, topEnd = 0.dp)
                )
                .padding(top = 16.dp, bottom = 10.dp, start = 20.dp, end = 20.dp)
        ) {
            // 건너뛰기
            Surface(
                onClick = {
                    viewModel.navigateToInputUserInfoScreen(navController)
                },
                contentColor = colorResource(id = com.swmarastro.mykkumi.common_ui.R.color.neutral_700),
                color = colorResource(id = com.swmarastro.mykkumi.common_ui.R.color.neutral_50),
                modifier = Modifier
                    .padding(end = 11.dp)
                    .clip(RoundedCornerShape(12.dp))
            ) {
                Text(
                    text = stringResource(id = R.string.skip_login_select_hobby),
                    fontSize = 15.sp,
                    fontFamily = FontFamily(Font(com.swmarastro.mykkumi.common_ui.R.font.pretendard_semibold)),
                    modifier = Modifier
                        .padding(horizontal = 26.dp, vertical = 15.5.dp)
                )
            }

            // 다음
            Surface(
                onClick = {
                    viewModel.navigateToInputUserInfoScreen(navController)
                },
                contentColor = colorResource(id = com.swmarastro.mykkumi.common_ui.R.color.white),
                color = colorResource(id = com.swmarastro.mykkumi.common_ui.R.color.primary_color),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 2.dp)
                    .clip(RoundedCornerShape(12.dp)),
            ) {
                Text(
                    text = stringResource(id = R.string.next_login_user_info),
                    fontSize = 15.sp,
                    fontFamily = FontFamily(Font(com.swmarastro.mykkumi.common_ui.R.font.pretendard_semibold)),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 15.5.dp)
                        .wrapContentWidth()
                )
            }
        }
    }
}

@Composable
fun HobbyCategoryItem(
    hobby: HobbyCategoryItemVO,
    viewModel: LoginSelectHobbyViewModel
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 32.dp)
    ) {
        Text(
            text = hobby.categoryName,
            fontSize = 18.sp,
            fontFamily = FontFamily(Font(com.swmarastro.mykkumi.common_ui.R.font.pretendard_bold)),
            color = colorResource(id = com.swmarastro.mykkumi.common_ui.R.color.neutral_900),
            modifier = Modifier
                .padding(bottom = 8.dp)
        )
        LazyRow(
        ) {
            items(
                items = hobby.subCategories,
                itemContent = {
                    HobbySubCategoryItem(
                        subCategory = it,
                        viewModel = viewModel
                    )
                }
            )
        }
    }
}

@Composable
fun HobbySubCategoryItem(
    subCategory: HobbySubCategoryItemVO,
    viewModel: LoginSelectHobbyViewModel
) {
    var backgroundColor = remember { mutableStateOf(
        if (viewModel.isHobbySelected(subCategory)) {
            Color.Green
        } else {
            Color.Transparent
        }
    )}

    val primaryColor = colorResource(id = com.swmarastro.mykkumi.common_ui.R.color.primary_color)
    var borderModifier = remember { mutableStateOf(
        if (viewModel.isHobbySelected(subCategory)) {
        Modifier.border(
            BorderStroke(1.dp, primaryColor),
            shape = RoundedCornerShape(16.dp)
        )
        } else {
            Modifier // 조건이 만족되지 않으면 빈 Modifier
        }
    )}

    Column(
        modifier = Modifier
            .padding(end = 16.dp)
            .clickable {
                viewModel.setHobbySelected(subCategory)
                if(viewModel.isHobbySelected(subCategory)) {
                    borderModifier.value = Modifier.border(
                        BorderStroke(3.dp, primaryColor),
                        shape = RoundedCornerShape(16.dp)
                    )
                } else {
                    borderModifier.value  = Modifier
                }
            }
    ) {
        Image(
            painter = painterResource(
                id = R.drawable.img_hobby_category_default),
            contentDescription = "default image",
            modifier = Modifier
                .size(60.dp)
                .align(Alignment.CenterHorizontally)
                .clip(RoundedCornerShape(16.dp))
                .then(borderModifier.value),
            contentScale = ContentScale.Fit,
        )
        Text(
            text = subCategory.subCategoryName,
            fontSize = 13.sp,
            fontFamily = FontFamily(Font(com.swmarastro.mykkumi.common_ui.R.font.pretendard_medium)),
            color = colorResource(id = com.swmarastro.mykkumi.common_ui.R.color.neutral_900),
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(top = 6.dp)
        )
    }
}