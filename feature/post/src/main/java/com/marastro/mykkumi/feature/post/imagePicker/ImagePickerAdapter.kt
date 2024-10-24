package com.marastro.mykkumi.feature.post.imagePicker

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.marastro.mykkumi.feature.post.databinding.ItemCameraBtnBinding
import com.marastro.mykkumi.feature.post.databinding.ItemImagePickerBinding

class ImagePickerAdapter (
    private val context: Context,
    private val viewModel: ImagePickerViewModel,
    private val captureWithCamera: () -> Unit,
    private val isAllowSelectImages: () -> Boolean,
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var imagePickerList = mutableListOf<ImagePickerItem>()

    companion object {
        private const val TYPE_BUTTON_CAMERA = 0    // 카메라 버튼
        private const val TYPE_IMAGE = 1     // 이미지
    }

    override fun getItemViewType(position: Int): Int = when(imagePickerList[position]) {
        is CameraBtn -> TYPE_BUTTON_CAMERA
        is ImagePickerData -> TYPE_IMAGE
        else -> throw IllegalStateException("Not Found ViewHolder Type")
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ) = when(viewType) {
        TYPE_BUTTON_CAMERA -> {
            val binding = ItemCameraBtnBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            CameraButtonViewHolder(binding)
        }
        TYPE_IMAGE -> {
            val binding = ItemImagePickerBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            ImagePickerViewHolder(binding)
        }
        else -> {
            throw IllegalStateException("Not Found ViewHolder Type $viewType")
        }
    }

    override fun onBindViewHolder(
        holder: RecyclerView.ViewHolder,
        position: Int
    ) {
        when(holder) {
            is CameraButtonViewHolder -> {
                holder.bind(imagePickerList[position] as CameraBtn)
            }
            is ImagePickerViewHolder -> {
                holder.bind(imagePickerList[position] as ImagePickerData, position)
            }
        }
    }

    override fun getItemCount(): Int = imagePickerList.size

    // 카메라
    inner class CameraButtonViewHolder (
        private val binding: ItemCameraBtnBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: CameraBtn) {
            binding.btnCamera.setOnClickListener {
                captureWithCamera()
            }
        }
    }

    // 이미지
    inner class ImagePickerViewHolder (
        private val binding: ItemImagePickerBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: ImagePickerData, position: Int) {
            Glide
                .with(context)
                .load(item.localUri)
                .into(binding.imageForPicker)

            // 체크박스 선택
            binding.checkboxPickImage.setOnCheckedChangeListener { _, isChecked ->
                viewModel.imagePickerUiState.value?.let {
                    // 선택 하려는데 최대 개수인 경우
                    if(isChecked && !isAllowSelectImages()) {
                        binding.checkboxPickImage.isChecked = false
                        it[position - 1].isSelect = false
                        Toast.makeText(context, "이미지는 총 10개까지만 선택 가능합니다.", Toast.LENGTH_SHORT).show()
                    }
                    // 선택 하려는데 최대 개수인 경우를 제외하고 가능
                    else {
                        viewModel.toggleImageSelection(position - 1, isChecked)

                        if(isChecked) {
                            binding.textNumImagePicker.text = item.selectNum.toString()
                        }
                        else {
                            binding.textNumImagePicker.text = ""
                        }
                    }
                }
            }

            // 이미지 선택
            binding.imageForPicker.setOnClickListener(View.OnClickListener {
                viewModel.imagePickerUiState.value?.let {
                    val isChecked = !binding.checkboxPickImage.isChecked

                    // 선택 하려는데 최대 개수인 경우
                    if(isChecked && !isAllowSelectImages()) {
                        binding.checkboxPickImage.isChecked = false
                        it[position - 1].isSelect = false
                        Toast.makeText(context, "이미지는 총 10개까지만 선택 가능합니다.", Toast.LENGTH_SHORT).show()
                    }
                    // 선택 하려는데 최대 개수인 경우를 제외하고 가능
                    else {
                        binding.checkboxPickImage.isChecked = isChecked
                        viewModel.toggleImageSelection(position - 1, isChecked)

                        if(isChecked) {
                            binding.textNumImagePicker.text = item.selectNum.toString()
                        }
                        else {
                            binding.textNumImagePicker.text = ""
                        }
                    }
                }
            })
        }
    }
}