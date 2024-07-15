package com.swmarastro.mykkumi.feature.home.banner

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.swmarastro.mykkumi.domain.entity.BannerItemVO
import com.swmarastro.mykkumi.feature.home.databinding.ItemBannerAllRecyclerviewBinding

class HomeBannerAllAdapter (
    private var bannerList: MutableList<BannerItemVO>,
    private val onClickBannerItem: (bannerId: Int) -> Unit
) : RecyclerView.Adapter<HomeBannerAllAdapter.HomeBannerAllViewHolder>(){
    override fun onCreateViewHolder(
        parent: ViewGroup, viewType: Int
    ): HomeBannerAllViewHolder {
        val binding = ItemBannerAllRecyclerviewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return HomeBannerAllViewHolder(binding)
    }

    override fun onBindViewHolder(holder: HomeBannerAllViewHolder, position: Int) {
        holder.bind(bannerList[position])
    }

    override fun getItemCount(): Int = bannerList.size

    inner class HomeBannerAllViewHolder(
        private val binding: ItemBannerAllRecyclerviewBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: BannerItemVO) {
            binding.imageHomeBanner.load(item.imageUrl)

            // 배너 클릭
            binding.imageHomeBanner.setOnClickListener(View.OnClickListener {
                onClickBannerItem(item.id)
            })
        }
    }
}