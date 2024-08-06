package com.swmarastro.mykkumi.feature.post.imageWithPin

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.swmarastro.mykkumi.common_ui.R
import com.swmarastro.mykkumi.common_ui.databinding.ItemPostImageViewpagerBinding
import com.swmarastro.mykkumi.domain.entity.PostEditPinVO
import com.swmarastro.mykkumi.feature.post.PostEditViewModel
import com.swmarastro.mykkumi.feature.post.PostImageData

// ViewPager
class EditImageWithPinAdapter(
    private val context: Context,
    private val viewModel: PostEditViewModel,
    private val lockViewPagerMoving: () -> Unit,
    private val unlockViewPagerMoving: () -> Unit,
) : RecyclerView.Adapter<EditImageWithPinAdapter.EditImageWithPinViewHolder>() {

    var imageWithPinList = mutableListOf<PostImageData>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EditImageWithPinViewHolder {
        val binding = ItemPostImageViewpagerBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return EditImageWithPinViewHolder(binding)
    }

    override fun onBindViewHolder(holder: EditImageWithPinViewHolder, position: Int) {
        holder.bind(imageWithPinList[position], position)
    }

    override fun getItemCount(): Int = imageWithPinList.size

    inner class EditImageWithPinViewHolder(
        private val binding: ItemPostImageViewpagerBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: PostImageData, position: Int) {
            Log.d("test", "parent created")

            binding.imagePost.load(item.localUri)

            val currentPinList = if(position == viewModel.selectImagePosition.value) { // 현재 선택된, 편집 중인 이미지
                viewModel.currentPinList.value ?: mutableListOf<PostEditPinVO>()
            }
            else { // 안 보이지만 ViewPager의 다른 페이지로 존재하는 다른 이미지들
                item.pinList ?: mutableListOf<PostEditPinVO>()
            }

            binding.relativePinsOfImages.removeAllViews()
            binding.relativePinsOfImages.layoutParams.height = (binding.imagePost as View).height

            for(pin in currentPinList) {
                val pinView = LayoutInflater.from(context).inflate(R.layout.item_pin_of_post_image, binding.relativePinsOfImages, false)
                binding.relativePinsOfImages.addView(pinView)

                // pin이 이미지의 크기를 벗어나지 않도록 제한
                val parent = binding.imagePost
                Log.d("test", "parent created")
                parent.viewTreeObserver.addOnGlobalLayoutListener(
                    object : ViewTreeObserver.OnGlobalLayoutListener {
                        override fun onGlobalLayout() {
                            Log.d("test", "item : ${item.localUri}imageview width : ${parent.width} height : ${parent.height}")
                        }
                    })
                val parentWidth = parent.width
                val parentHeight = parent.height

                // 핀의 크기를 고려
                val pinWidth = pinView.width
                val pinHeight = pinView.height

                pinView.post {
                    pinView.x = pin.positionX * (parentWidth - pinWidth)
                    pinView.y = pin.positionY * (parentHeight - pinHeight)
                }

                var moveX = 0f
                var moveY = 0f

                pinView.setOnTouchListener { v, event ->
                    when (event.action) {
                        MotionEvent.ACTION_DOWN -> { // Pin 선택하면 현재 위치 가져오기
                            moveX = v.x - event.rawX
                            moveY = v.y - event.rawY

                            lockViewPagerMoving() // Pin 움직이는 동안 ViewPager 화면 전환 막아두기
                        }

                        MotionEvent.ACTION_MOVE -> {
                            // 현재 위치
                            val currentX = event.rawX + moveX
                            val currentY = event.rawY + moveY

                            val clampedX = currentX.coerceIn(0f, (parentWidth - pinWidth).toFloat())
                            val clampedY = currentY.coerceIn(0f, (parentHeight - pinHeight).toFloat())

                            v.x = clampedX
                            v.y = clampedY

                            // 핀의 비율 위치 계산
                            pin.positionX = clampedX / (parentWidth - pinWidth)
                            pin.positionY = clampedY / (parentHeight - pinHeight)
                        }

                        MotionEvent.ACTION_UP -> {
                            unlockViewPagerMoving() // Pin 이동 종료 후 ViewPager 스크롤 다시 가능
                        }
                    }
                    true
                }
            }
        }
    }
}