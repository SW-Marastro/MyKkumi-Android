package com.swmarastro.mykkumi.feature.auth.onBoarding

import androidx.activity.ComponentActivity
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModelProvider
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

    LaunchedEffect(Unit) {
        viewModel.getHobbyCategoryList()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        Text(
            text = stringResource(id = R.string.title_login_select_hobby),
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(vertical = 10.dp)
        )

        // 취미 카테고리 : 대분류 > 소분류
        LazyColumn(
            contentPadding = PaddingValues(2.dp, 5.dp),
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

        // 건너뛰기
        Text(
            text = stringResource(id = R.string.skip_login_select_hobby),
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(vertical = 10.dp)
                .clickable {
                    viewModel.navigateToInputUserInfoScreen(navController)
                }
        )

        // 다음
        Button(
            onClick = {
                viewModel.navigateToInputUserInfoScreen(navController)
            },
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .fillMaxWidth()
                .padding(vertical = 2.dp)
        ) {
            Text(
                text = stringResource(id = R.string.next_login_user_info)
            )
        }
    }
}

@Composable
fun HobbyCategoryItem(
    hobby: HobbyCategoryItemVO,
    viewModel: LoginSelectHobbyViewModel
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = hobby.categoryName,
            fontWeight = FontWeight.Bold
        )
        LazyRow(
            contentPadding = PaddingValues(5.dp, 2.dp)
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
            Color.LightGray
        }
    )}

    Column(
        modifier = Modifier
            .padding(horizontal = 5.dp)
            .background(backgroundColor.value)
            .clickable {
                viewModel.setHobbySelected(subCategory)
                if(viewModel.isHobbySelected(subCategory)) {
                    backgroundColor.value = Color.Green
                }
                else {
                    backgroundColor.value = Color.LightGray
                }
            }
    ) {
        Image(
            painter = painterResource(
                id = com.swmarastro.mykkumi.common_ui.R.drawable.img_profile_default),
            contentDescription = "default image",
            modifier = Modifier
                .size(60.dp)
                .align(Alignment.CenterHorizontally)
                .padding(3.dp)
        )
        Text(
            text = subCategory.subCategoryName,
            modifier = Modifier
                .padding(horizontal = 5.dp)
                .align(Alignment.CenterHorizontally)
        )
    }
}