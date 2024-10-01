package com.marastro.mykkumi.feature.post.touchEvent

// 드래그로 편집 아이템 이동을 위한 부분
interface ItemTouchHelperListener {
    fun onItemMove(from: Int, to: Int) // 아이템이 움직일 때

    fun clearItemView() // 아이템 선택 해제될 때
}