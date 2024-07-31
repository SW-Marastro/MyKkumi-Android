package com.swmarastro.mykkumi.feature.post

import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.swmarastro.mykkumi.feature.post.databinding.ItemSelectPostImageBinding

class SelectPostImageListAdapter (
    private val onClickPostImage: (image: Uri) -> Unit
) : RecyclerView.Adapter<SelectPostImageListAdapter.SelectPostImageListViewHolder>() {
    var postImageList = mutableListOf<Uri>()
        set(value) { // 데이터 추가되면 마지막 데이터를 선택
            field = value
            selectImagePosition = value.size - 1
            notifyDataSetChanged()
        }
    var selectImagePosition: Int = 0

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): SelectPostImageListViewHolder {
        val binding = ItemSelectPostImageBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SelectPostImageListViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: SelectPostImageListViewHolder,
        position: Int
    ) {
        holder.bind(postImageList[position], position)
    }

    override fun getItemCount(): Int = postImageList.size

    inner class SelectPostImageListViewHolder(
        private val binding: ItemSelectPostImageBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Uri, position: Int) {
            binding.imagePostEditThumbnail.load(item)

            // 편집할 이미지 선택
            binding.imagePostEditThumbnail.setOnClickListener(View.OnClickListener {
                onClickPostImage(item)
                selectImagePosition = position
                notifyDataSetChanged()
            })

            if (position == selectImagePosition) {
                binding.imagePostEditThumbnail.setBackgroundResource(R.drawable.shape_select_post_image)
            }
            else {
                binding.imagePostEditThumbnail.background = null
            }
        }
    }
}