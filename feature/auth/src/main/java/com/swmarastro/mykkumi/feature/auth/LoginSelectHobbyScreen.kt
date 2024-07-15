package com.swmarastro.mykkumi.feature.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

data class TestHobby (
    val categoryName: String,
    val subCategories: MutableList<String>
)
private val testHobbies : MutableList<TestHobby> = mutableListOf(
    TestHobby("공예/DIY", mutableListOf("다이어리 꾸미기", "포토카드 꾸미기", "뜨개질")),
    TestHobby("스포츠", mutableListOf("야구", "테니스", "골프")),
    TestHobby("뷰티", mutableListOf("메이크업", "헤어"))
)

@Composable
fun LoginSelectHobbyScreen(navController: NavController) {
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
            contentPadding = PaddingValues(2.dp, 5.dp)
        ) {
            items(
                items = testHobbies,
                itemContent = { HobbyCategoryItem(hobby = it) }
            )
        }

        // Spacer로 중간 공간을 채움
        Spacer(modifier = Modifier.weight(1f))

        // 건너뛰기
        Text(
            text = stringResource(id = R.string.skip_login_select_hobby),
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(vertical = 10.dp)
                .clickable {
                    navController.navigate(route = LoginScreens.LoginInputUserScreen.name)
                }
        )

        // 다음
        Button(
            onClick = {
                navController.navigate(route = LoginScreens.LoginInputUserScreen.name)
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
fun HobbyCategoryItem(hobby: TestHobby) {
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
                itemContent = { HobbySubCategoryItem(it) }
            )
        }
    }
}

@Composable
fun HobbySubCategoryItem(subCategory: String) {
    var backgroundColor = remember { mutableStateOf(Color.LightGray) }

    Text(
        text = subCategory,
        modifier = Modifier
            .padding(horizontal = 5.dp)
            .background(backgroundColor.value)
            .clickable {
                backgroundColor.value =
                    if (backgroundColor.value == Color.LightGray) Color.Green else Color.LightGray
            }
    )
}