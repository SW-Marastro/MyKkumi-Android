package com.swmarastro.mykkumi.feature.post.image

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.swmarastro.mykkumi.feature.post.databinding.ItemImagePickerBinding

class ImagePickerAdapter (
    private val viewModel: ImagePickerViewModel
) : RecyclerView.Adapter<ImagePickerAdapter.ImagePickerViewHolder>() {

    var imagePickerList = mutableListOf<ImagePickerData>()

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ImagePickerViewHolder {
        val binding = ItemImagePickerBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ImagePickerViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: ImagePickerViewHolder,
        position: Int
    ) {
        holder.bind(imagePickerList[position], position)
    }

    override fun getItemCount(): Int = imagePickerList.size

    inner class ImagePickerViewHolder (
        private val binding: ItemImagePickerBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: ImagePickerData, position: Int) {
            // 이미지를 1:1 비율로
            binding.imageForPicker.post {
                val width = binding.imageForPicker.width
                binding.imageForPicker.layoutParams.height = width
                binding.imageForPicker.requestLayout()
            }

            binding.imageForPicker.load(item.localUri)

            binding.checkboxPickImage.setOnCheckedChangeListener { _, isChecked ->
                viewModel.imagePickerUiState.value?.let {
                    it[position].isSelect = isChecked
                }
            }
        }
    }
}