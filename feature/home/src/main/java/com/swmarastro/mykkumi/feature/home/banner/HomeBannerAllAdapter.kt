package com.swmarastro.mykkumi.feature.home.banner

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.swmarastro.mykkumi.domain.entity.HomeBannerItemVO
import com.swmarastro.mykkumi.feature.home.R
import com.swmarastro.mykkumi.feature.home.databinding.ItemBannerAllRecyclerviewBinding

class HomeBannerAllAdapter (
    private var bannerList: MutableList<HomeBannerItemVO>,
    private val onClickBannerItem: (bannerId: Int) -> Unit
) : RecyclerView.Adapter<HomeBannerAllAdapter.HomeBannerAllViewHolder>(){
    private var _binding: ItemBannerAllRecyclerviewBinding? = null
    private val binding get() = _binding!!

    override fun onCreateViewHolder(
        parent: ViewGroup, viewType: Int
    ): HomeBannerAllViewHolder {
        _binding = DataBindingUtil.inflate(LayoutInflater.from(parent.context),
            R.layout.item_banner_all_recyclerview, parent, false)
        return HomeBannerAllViewHolder(binding)
    }

    override fun onBindViewHolder(holder: HomeBannerAllViewHolder, position: Int) {
        holder.bind(bannerList[position])
    }

    override fun getItemCount(): Int = bannerList.size

    inner class HomeBannerAllViewHolder(
        private val binding: ItemBannerAllRecyclerviewBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: HomeBannerItemVO) {
            binding.imageHomeBanner.load(item.imageUrl)

            // 배너 클릭
            binding.imageHomeBanner.setOnClickListener(View.OnClickListener {
                onClickBannerItem(item.id)
            })
        }
    }
}