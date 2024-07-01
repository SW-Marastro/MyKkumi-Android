package com.swmarastro.mykkumi.common_ui.post

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.swmarastro.mykkumi.common_ui.R
import com.swmarastro.mykkumi.common_ui.databinding.ItemPostImageViewpagerBinding


class PostImagesAdapter(
    private var postImageList: MutableList<String>
) : RecyclerView.Adapter<PostImagesAdapter.PostItemImageViewHolder>() {
    private var _binding: ItemPostImageViewpagerBinding? = null
    private val binding get() = _binding!!

    override fun onCreateViewHolder(
        parent: ViewGroup, viewType: Int
    ): PostItemImageViewHolder {
        _binding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.item_post_image_viewpager, parent, false)
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

            binding.imagePost.viewTreeObserver.addOnGlobalLayoutListener {
                val width = binding.imagePost.width
                binding.imagePost.layoutParams.height = width
                binding.imagePost.requestLayout()
            }
        }
    }
}