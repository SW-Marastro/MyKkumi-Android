package com.swmarastro.mykkumi.feature.post.imageWithPin

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.swmarastro.mykkumi.common_ui.databinding.ItemPostImageViewpagerBinding
import com.swmarastro.mykkumi.feature.post.PostImageData

// ViewPager
class EditImageWithPinAdapter(
) : RecyclerView.Adapter<EditImageWithPinAdapter.EditImageWithPinViewHolder>() {

    var imageWithPinList = mutableListOf<PostImageData>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EditImageWithPinViewHolder {
        val binding = ItemPostImageViewpagerBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return EditImageWithPinViewHolder(binding)
    }

    override fun onBindViewHolder(holder: EditImageWithPinViewHolder, position: Int) {
        holder.bind(imageWithPinList[position])
    }

    override fun getItemCount(): Int = imageWithPinList.size

    class  EditImageWithPinViewHolder(
        private val binding: ItemPostImageViewpagerBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: PostImageData) {
            binding.imagePost.load(item.localUri)
        }
    }
}