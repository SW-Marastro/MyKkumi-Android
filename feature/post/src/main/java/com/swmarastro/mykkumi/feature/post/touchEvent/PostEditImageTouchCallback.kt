package com.swmarastro.mykkumi.feature.post.touchEvent

import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView

class PostEditImageTouchCallback(private val listener: ItemTouchHelperListener): ItemTouchHelper.Callback() {

    /** 드래그 방향과 드래그 이동을 정의하는 함수 */
    // 드래그의 방향을 정의하고, 움직임을 리턴함.
    override fun getMovementFlags(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder): Int {
        // 드래그 방향
        val dragFlags = ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
        // 스와이프 방향
        val swipeFlags = ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT

        // 드래그 이동을 만드는 함수 = 아이템의 움직임을 리턴
        // 드래그, 스와이프 방향 파라미터로 넣은 방향만 움직일 수 있음.
        // 현재 좌우 드래그, 좌우 스와이프만 가능
        return makeMovementFlags(dragFlags, swipeFlags)
    }

    /** 아이템이 움직일 때 호출되는 함수 */
    override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean {
        viewHolder.itemView.elevation = 10.0F
        // ItemTouchHelperListener 인터페이스로 선언한 onItemMove() 함수를 호출
        listener.onItemMove(viewHolder.adapterPosition, target.adapterPosition)
        return false
    }

    /** 아이템이 스와이프 될때 호출되는 함수 */
    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        //TODO("Not yet implemented")
    }

    /** 스와이프는 꺼두기 */
    override fun isItemViewSwipeEnabled(): Boolean {
        return false
    }

    /** 아이템 선택 시(=꾹 눌렀을 때) 호출되는 함수 */
    override fun onSelectedChanged(viewHolder: RecyclerView.ViewHolder?, actionState: Int) {
        super.onSelectedChanged(viewHolder, actionState)
        viewHolder?.itemView?.elevation = 10.0F
    }

    /** 아이템 선택 해제 시 호출되는 함수 */
    override fun clearView(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder) {
        super.clearView(recyclerView, viewHolder)
        viewHolder.itemView.elevation = 0.0F
        listener.clearItemView()
    }

}