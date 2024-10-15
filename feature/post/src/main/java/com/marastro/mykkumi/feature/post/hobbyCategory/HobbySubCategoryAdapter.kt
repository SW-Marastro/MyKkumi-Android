package com.marastro.mykkumi.feature.post.hobbyCategory

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.marastro.mykkumi.domain.entity.HobbySubCategoryItemVO
import com.marastro.mykkumi.feature.post.PostEditViewModel
import com.marastro.mykkumi.feature.post.R
import com.marastro.mykkumi.feature.post.databinding.ItemHobbySubCategoryBinding

class HobbySubCategoryAdapter (
    private val context: Context,
    private val viewModel: PostEditViewModel
) : RecyclerView.Adapter<HobbySubCategoryAdapter.HobbySubCategoryViewHolder>(){
    var hobbySubCategoryList = mutableListOf<HobbySubCategoryItemVO>()

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): HobbySubCategoryAdapter.HobbySubCategoryViewHolder {
        val binding = ItemHobbySubCategoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return HobbySubCategoryViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: HobbySubCategoryAdapter.HobbySubCategoryViewHolder,
        position: Int
    ) {
        holder.bind(hobbySubCategoryList[position])
    }

    override fun getItemCount(): Int = hobbySubCategoryList.size

    inner class HobbySubCategoryViewHolder(
        private val binding: ItemHobbySubCategoryBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: HobbySubCategoryItemVO) {
            binding.textHobbySubCategory.text = item.subCategoryName

            if(item.subCategoryId == viewModel.selectHobbyCategory.value) {
                binding.linearSubHobbyCategory.setBackgroundResource(R.drawable.shape_sub_category_selected)
                binding.textHobbySubCategory.setTextColor(ContextCompat.getColor(context, com.marastro.mykkumi.common_ui.R.color.white))
            }
            else {
                binding.linearSubHobbyCategory.setBackgroundResource(R.drawable.shape_sub_category_unselected)
                binding.textHobbySubCategory.setTextColor(ContextCompat.getColor(context, com.marastro.mykkumi.common_ui.R.color.neutral_700))
            }

            binding.linearSubHobbyCategory.setOnClickListener(View.OnClickListener {
                viewModel.setHobbySelected(item.subCategoryId)
                notifyDataSetChanged()
            })
        }
    }
}