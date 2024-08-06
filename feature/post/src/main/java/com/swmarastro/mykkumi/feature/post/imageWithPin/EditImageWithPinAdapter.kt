package com.swmarastro.mykkumi.feature.post.imageWithPin

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.swmarastro.mykkumi.common_ui.databinding.ItemPostImageViewpagerBinding
import com.swmarastro.mykkumi.common_ui.layoutManager.FrameLayoutManager
import com.swmarastro.mykkumi.domain.entity.PostEditPinVO
import com.swmarastro.mykkumi.feature.post.PostEditViewModel
import com.swmarastro.mykkumi.feature.post.PostImageData

// ViewPager
class EditImageWithPinAdapter(
    private val viewModel: PostEditViewModel,
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
        // 핀 RecyclerView
        private val editPinAdapter = EditPinAdapter()

        init {
            binding.recyclerviewPinsOfImages.apply {
                layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
                adapter = editPinAdapter
                isNestedScrollingEnabled = false
            }
        }

        fun bind(item: PostImageData, position: Int) {
            binding.imagePost.load(item.localUri)

            Log.d("test", "-------")

            val currentPinList = if(position == viewModel.selectImagePosition.value) { // 현재 선택된, 편집 중인 이미지
                viewModel.currentPinList.value ?: mutableListOf<PostEditPinVO>()
            }
            else { // 안 보이지만 ViewPager의 다른 페이지로 존재하는 다른 이미지들
                item.pinList ?: mutableListOf<PostEditPinVO>()
            }

            editPinAdapter.pinList = currentPinList

            editPinAdapter.notifyDataSetChanged()
        }
    }
}