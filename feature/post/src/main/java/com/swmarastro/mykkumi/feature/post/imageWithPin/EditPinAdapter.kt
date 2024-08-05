package com.swmarastro.mykkumi.feature.post.imageWithPin

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.swmarastro.mykkumi.domain.entity.PostEditPinVO
import com.swmarastro.mykkumi.feature.post.databinding.ItemPinOfPostImageBinding

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
        binding: ItemPinOfPostImageBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: PostEditPinVO) {

        }
    }
}