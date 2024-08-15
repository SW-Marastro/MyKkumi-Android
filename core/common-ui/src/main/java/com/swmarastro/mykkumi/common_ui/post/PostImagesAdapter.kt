package com.swmarastro.mykkumi.common_ui.post

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.ViewTreeObserver
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.swmarastro.mykkumi.common_ui.R
import com.swmarastro.mykkumi.common_ui.databinding.ItemPostImageViewViewpagerBinding
import com.swmarastro.mykkumi.domain.entity.HomePostImageVO


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
            binding.imagePost.load(item.url)

            binding.relativePinsOfImages.removeAllViews()

            val parent = binding.imagePost

            // 이미지 세로 사이즈를 가로 사이즈와 동일하게 설정
            parent.viewTreeObserver.addOnGlobalLayoutListener {
                if(parentWidth == 0 || parentHeight == 0) {
                    if(parent.width != 0 && parent.height != 0) {
                        Log.d("test", "${item.pins.size}: image > width : ${parent.width}, height: ${parent.height}")
                        if(parent.width > parent.height) {
                            parentWidth = parent.width
                            parentHeight = parent.height
                        }
                        else {
                            parentWidth = (parent.width.toDouble() * (parent.width.toDouble() / parent.height.toDouble())).toInt()
                            parentHeight = parent.width
                        }

                        binding.relativePinsOfImages.layoutParams.width = parentWidth
                        binding.relativePinsOfImages.layoutParams.height = parentHeight
                        binding.relativePinsOfImages.requestLayout()

                        Log.d("test", "${item.pins.size}: relative > width: ${parentWidth}, height: ${parentHeight}")

                        val width = parent.width
                        parent.layoutParams.height = width
                        parent.requestLayout()

                        notifyItemChanged(position)
                    }
                }
            }

//            // pin이 이미지의 크기를 벗어나지 않도록 제한
//            val parent = binding.imagePost
//
//            parent.viewTreeObserver.addOnGlobalLayoutListener (
//                object : ViewTreeObserver.OnGlobalLayoutListener {
//                    override fun onGlobalLayout() {
//                        if (parent.width != 0 && parent.height != 0 && parentWidth == 0) {
//                            Log.d("test", "width: ${parent.width}, height: ${parent.height}")
//                            Log.d("test", "${binding.relativePostImage.width}")
//                            if(parent.width > parent.height) {
//                                parentWidth = binding.relativePostImage.width
//                                parentHeight = parent.height * (binding.relativePostImage.width / parent.width)
//                            }
//                            else {
//                                parentWidth = parent.width * (binding.relativePostImage.width / parent.height)
//                                parentHeight = binding.relativePostImage.width
//                            }
//
//                            binding.imagePost.layoutParams.width = parentWidth
//                            binding.imagePost.layoutParams.height = parentHeight
//                            binding.imagePost.requestLayout()
//
//                            Log.d("test", "------ width: ${parent.width}, height: ${parent.height}")
//                            Log.d("test", "------22 width: ${parentWidth}, height: ${parentHeight}")
//
//                            binding.relativePinsOfImages.layoutParams.width = parentWidth
//                            binding.relativePinsOfImages.layoutParams.height = parentHeight
//                            binding.relativePinsOfImages.requestLayout()
//                            notifyItemChanged(position)
//                        }
//
//                        parent.viewTreeObserver.removeOnGlobalLayoutListener(this)
//                    }
//                }
//            )

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