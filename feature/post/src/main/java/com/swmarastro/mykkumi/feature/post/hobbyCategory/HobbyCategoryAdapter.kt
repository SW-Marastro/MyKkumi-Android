package com.swmarastro.mykkumi.feature.post.hobbyCategory

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.swmarastro.mykkumi.domain.entity.HobbyCategoryItemVO
import com.swmarastro.mykkumi.feature.post.databinding.ItemHobbyCategoryBinding

class HobbyCategoryAdapter (
    private val hobbyCategorySpan: Int,
    private val viewModel: SelectHobbyOfPostViewModel,
) : RecyclerView.Adapter<HobbyCategoryAdapter.HobbyCategoryViewHolder>(){
    var hobbyCategoryList = mutableListOf<HobbyCategoryItemVO>()

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): HobbyCategoryAdapter.HobbyCategoryViewHolder {
        val binding = ItemHobbyCategoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return HobbyCategoryViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: HobbyCategoryAdapter.HobbyCategoryViewHolder,
        position: Int
    ) {
        holder.bind(hobbyCategoryList[position])
    }

    override fun getItemCount(): Int = hobbyCategoryList.size

    inner class HobbyCategoryViewHolder(
        private val binding: ItemHobbyCategoryBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        private lateinit var hobbySubCategoryAdapter: HobbySubCategoryAdapter

        fun bind(item: HobbyCategoryItemVO) {
            binding.textHobbyCategory.text = item.categoryName

            hobbySubCategoryAdapter = HobbySubCategoryAdapter(
                viewModel,
                updateAllCategory = {
                    notifyDataSetChanged()
                }
            )
            hobbySubCategoryAdapter.hobbySubCategoryList = item.subCategories.toMutableList()
            binding.recyclerviewHobbySubCategory.apply {
                layoutManager = GridLayoutManager(
                    context,
                    hobbyCategorySpan
                )
                adapter = hobbySubCategoryAdapter
            }
        }
    }
}