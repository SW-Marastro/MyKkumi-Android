package com.swmarastro.mykkumi.feature.post.hobbyCategory

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.swmarastro.mykkumi.domain.entity.HobbySubCategoryItemVO
import com.swmarastro.mykkumi.feature.post.databinding.ItemHobbySubCategoryBinding

class HobbySubCategoryAdapter (
    private val viewModel: SelectHobbyOfPostViewModel,
    private val updateAllCategory: () -> Unit
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

            if(item.subCategoryId == viewModel.selectedHobby.value) {
                binding.linearSubHobbyCategory.setBackgroundColor(Color.GREEN)
            }
            else {
                binding.linearSubHobbyCategory.background = null
            }

            binding.linearSubHobbyCategory.setOnClickListener(View.OnClickListener {
                viewModel.setHobbySelected(item.subCategoryId)
                updateAllCategory()
            })
        }
    }
}