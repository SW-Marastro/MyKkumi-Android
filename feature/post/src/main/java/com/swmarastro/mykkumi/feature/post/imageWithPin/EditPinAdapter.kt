package com.swmarastro.mykkumi.feature.post.imageWithPin

import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.swmarastro.mykkumi.common_ui.databinding.ItemPinOfPostImageBinding
import com.swmarastro.mykkumi.domain.entity.PostEditPinVO

class EditPinAdapter : RecyclerView.Adapter<EditPinAdapter.PinViewHolder>() {

    var pinList = mutableListOf<PostEditPinVO>()

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): PinViewHolder {
        val binding = ItemPinOfPostImageBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PinViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: PinViewHolder,
        position: Int
    ) {
        holder.bind(pinList[position])
    }

    override fun getItemCount(): Int = pinList.size

    class PinViewHolder(
        private val binding: ItemPinOfPostImageBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: PostEditPinVO) {
            var moveX = 50f
            var moveY = 50f

            binding.pinOfPostImage.setOnTouchListener { v, event ->
                when(event.action) {
                    MotionEvent.ACTION_DOWN -> {
                        moveX = v.x - event.rawX
                        moveY = v.y - event.rawY
                    }

                    MotionEvent.ACTION_MOVE -> {
                        v.animate()
                            .x(event.rawX + moveX)
                            .y(event.rawY + moveY)
                            .setDuration(0)
                            .start()
                    }
                }

                true
            }
        }
    }
}