package com.marastro.mykkumi.feature.post.imageWithPin

import android.content.Context
import android.content.res.Resources
import android.graphics.Rect
import android.util.Log
import android.view.GestureDetector
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.TouchDelegate
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.widget.PopupWindow
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.marastro.mykkumi.common_ui.R
import com.marastro.mykkumi.common_ui.databinding.ItemPostImageViewpagerBinding
import com.marastro.mykkumi.domain.entity.PostImageVO

// ViewPager
class EditImageWithPinAdapter(
    private val context: Context,
    private val resources: Resources,
    private val lockViewPagerMoving: () -> Unit,
    private val unlockViewPagerMoving: () -> Unit,
    private val updateProductInfo: (position: Int) -> Unit,
    private val deletePinOfImage: (position: Int) -> Unit,
) : RecyclerView.Adapter<EditImageWithPinAdapter.EditImageWithPinViewHolder>() {

    var imageWithPinList = mutableListOf<PostImageVO>()

    private var selectImagePosition: Int = -1
    private var newEditPin: Int = -1

    fun updateNewEditPin(selectImagePosition: Int, newEditPin: Int) {
        this.selectImagePosition = selectImagePosition
        this.newEditPin = newEditPin
        notifyItemChanged(selectImagePosition)
        //notifyDataSetChanged()
    }

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
        private var parentWidth = 0
        private var parentHeight = 0

        fun bind(item: PostImageVO, position: Int) {
            val parent = binding.imagePost
            val editImageView = binding.relativePinsOfImages
            val expandPx = (10 * resources.displayMetrics.density).toInt() // 10dp를 px로 변환

            Glide
                .with(context)
                .load(item.imageUri)
                .into(binding.imagePost)

            val currentPinList = item.pinList

            binding.relativePinsOfImages.removeAllViews()

            // pin이 이미지의 크기를 벗어나지 않도록 제한
            parent.viewTreeObserver.addOnGlobalLayoutListener (
                object : ViewTreeObserver.OnGlobalLayoutListener {
                    override fun onGlobalLayout() {
                        if (parentHeight != parent.height) {
                            parentWidth = parent.width
                            parentHeight = parent.height

                            if (parentHeight != 0) {
                                binding.relativePinsOfImages.layoutParams.height = parentHeight
                                binding.relativePinsOfImages.requestLayout()
                                notifyDataSetChanged()
                            }
                        }

                        parent.viewTreeObserver.removeOnGlobalLayoutListener(this)
                    }
                }
            )

            editImageView.layoutParams.height = parentHeight

            for(idx in 0..<currentPinList.size) {
                val pin = currentPinList[idx]

                val pinView = LayoutInflater.from(context).inflate(R.layout.item_pin_of_post_edit, editImageView, false)
                editImageView.addView(pinView)

                // 핀의 가운데를 기준으로
                var pinWidth = 0
                var pinHeight = 0

                // 핀 터치 영역 가져와서 넓혀주기
                editImageView.post {
                    val pinRect = Rect()
                    pinView.getHitRect(pinRect)
                    pinRect.inset(-expandPx, -expandPx)
                    editImageView.touchDelegate = TouchDelegate(pinRect, pinView)
                }

                // 핀 크기
                pinView.post {
                    pinWidth = pinView.width
                    pinHeight = pinView.height

                    pinView.x = pin.positionX * (parentWidth - pinWidth)
                    pinView.y = pin.positionY * (parentHeight - pinHeight)

                    // 새로 추가된 핀
                    if(newEditPin != -1 && position == selectImagePosition && idx == newEditPin) {
                        showTooltipOfPin(pinView, idx, pin.product.productName)
                        newEditPin = -1
                    }
                }

                var moveX = 0f
                var moveY = 0f

                // 제품 정보 수정 / 삭제 tooltip을 위한 touchEvent 체크
                val gestureDetector = GestureDetector(context, object : GestureDetector.SimpleOnGestureListener() {
                    override fun onSingleTapUp(e: MotionEvent): Boolean {
                        showTooltipOfPin(pinView, idx, pin.product.productName)
                        return super.onSingleTapUp(e)
                    }
                })

                pinView.setOnTouchListener { v, event ->
                    gestureDetector.onTouchEvent(event)

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

    // 제품 정보 수정, 삭제 tooltip
    private fun showTooltipOfPin(anchorView: View, pinIndex: Int, productName: String) {
        val tooltipView = LayoutInflater.from(anchorView.context).inflate(R.layout.tooltip_pin_of_image, null)

        // 제품명
        val textProductName = tooltipView.findViewById<TextView>(R.id.text_product_name)
        textProductName.text = productName

        val popupWindow = PopupWindow(
            tooltipView,
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT,
            true
        )

        tooltipView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED)
        val tooltipWidth = tooltipView.measuredWidth
        val tooltipHeight = tooltipView.measuredHeight

        // 핀 수정
        val buttonUpdateProductInfo = tooltipView.findViewById<TextView>(R.id.btn_update_product_info)
        buttonUpdateProductInfo.setOnClickListener(View.OnClickListener {
            updateProductInfo(pinIndex)
            popupWindow.dismiss()
        })

        // 핀 삭제
        val buttonDeletePin = tooltipView.findViewById<TextView>(R.id.btn_delete_pin)
        buttonDeletePin.setOnClickListener(View.OnClickListener {
            popupWindow.dismiss()
            deletePinOfImage(pinIndex)
        })

        val xOffset = (anchorView.width - tooltipWidth) / 2
        val yOffset = -anchorView.height - tooltipHeight

        popupWindow.showAsDropDown(anchorView, xOffset, yOffset)
    }
}