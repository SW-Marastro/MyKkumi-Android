package com.marastro.mykkumi.common_ui.post

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.ViewTreeObserver
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.marastro.mykkumi.common_ui.R
import com.marastro.mykkumi.common_ui.databinding.ItemPostImageViewViewpagerBinding
import com.marastro.mykkumi.domain.entity.HomePostImageVO


class PostImagesAdapter(
    private val context: Context,
    private var postImageList: MutableList<HomePostImageVO>
) : RecyclerView.Adapter<PostImagesAdapter.PostItemImageViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup, viewType: Int
    ): PostItemImageViewHolder {
        val binding = ItemPostImageViewViewpagerBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PostItemImageViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PostItemImageViewHolder, position: Int) {
        holder.bind(postImageList[position], position)
    }

    override fun getItemCount(): Int = postImageList.size

    inner class PostItemImageViewHolder(
        private val binding: ItemPostImageViewViewpagerBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        private var parentWidth = 0
        private var parentHeight = 0

        fun bind(item: HomePostImageVO, position: Int) {
            // 포스트 이미지 세팅
            // 페이지 넘길 때 이미지 로드
            Glide
                .with(context)
                .load(item.url)
                .placeholder(R.drawable.img_loading_post)
                .into(binding.imagePost)

            binding.relativePinsOfImages.removeAllViews()

            val parent = binding.imagePost

            // 이미지 세로 사이즈를 가로 사이즈와 동일하게 설정
            parent.viewTreeObserver.addOnGlobalLayoutListener(object :
                ViewTreeObserver.OnGlobalLayoutListener {
                override fun onGlobalLayout() {
                    if (parentWidth == 0 || parentHeight == 0) {
                        if (parent.width != 0 && parent.height != 0) {
                            if (parent.width > parent.height) {
                                parentWidth = parent.width
                                parentHeight = parent.height
                            } else {
                                parentWidth =
                                    (parent.width.toDouble() * (parent.width.toDouble() / parent.height.toDouble())).toInt()
                                parentHeight = parent.width
                            }

                            binding.relativePinsOfImages.layoutParams.width = parentWidth
                            binding.relativePinsOfImages.layoutParams.height = parentHeight
                            binding.relativePinsOfImages.requestLayout()

                            val width = parent.width
                            parent.layoutParams.height = width
                            parent.requestLayout()

                            binding.relativePostImage.layoutParams.height = binding.relativePostImage.width
                            binding.relativePostImage.requestLayout()

//                            notifyItemChanged(position)
                        }
                        else {
                            notifyItemChanged(position)
                        }
                    }

                    // 리스너 제거
                    parent.viewTreeObserver.removeOnGlobalLayoutListener(this)
                }
            })

            // 포스트에 핀 추가
            for(idx in 0..<item.pins.size) {
                val pin = item.pins[idx]

                val pinView = LayoutInflater.from(context).inflate(R.layout.item_pin_of_post_image, binding.relativePinsOfImages, false)
                binding.relativePinsOfImages.addView(pinView)

                pinView.post {
                    pinView.x = pin.positionX * (parentWidth - pinView.width)
                    pinView.y = pin.positionY * (parentHeight - pinView.height)
                }
            }
        }
    }
}