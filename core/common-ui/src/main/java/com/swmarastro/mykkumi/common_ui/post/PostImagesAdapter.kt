package com.swmarastro.mykkumi.common_ui.post

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.swmarastro.mykkumi.common_ui.databinding.ItemPostImageViewpagerBinding


class PostImagesAdapter(
    private var postImageList: MutableList<String>
) : RecyclerView.Adapter<PostImagesAdapter.PostItemImageViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup, viewType: Int
    ): PostItemImageViewHolder {
        val binding = ItemPostImageViewpagerBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PostItemImageViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PostItemImageViewHolder, position: Int) {
        holder.bind(postImageList[position])
    }

    override fun getItemCount(): Int = postImageList.size

    inner class PostItemImageViewHolder(
        private val binding: ItemPostImageViewpagerBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: String) {
            // 포스트 이미지 세팅
            binding.imagePost.load(item)

            // 이미지 세로 사이즈를 가로 사이즈와 동일하게 설정
            binding.imagePost.viewTreeObserver.addOnGlobalLayoutListener {
                val width = binding.imagePost.width
                binding.imagePost.layoutParams.height = width
                binding.imagePost.requestLayout()
            }
        }
    }
}