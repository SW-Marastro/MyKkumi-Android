package com.marastro.mykkumi.feature.home.banner

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.marastro.mykkumi.domain.entity.BannerItemVO
import com.marastro.mykkumi.feature.home.databinding.ItemBannerAllRecyclerviewBinding

class HomeBannerAllAdapter (
    private val context: Context,
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
            Glide
                .with(context)
                .load(item.imageUrl)
                .into(binding.imageHomeBanner)

            // 배너 클릭
            binding.imageHomeBanner.setOnClickListener(View.OnClickListener {
                onClickBannerItem(item.id)
            })
        }
    }
}