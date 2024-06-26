package com.swmarastro.mykkumi.feature.home

import android.graphics.Bitmap
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.swmarastro.mykkumi.feature.home.databinding.ItemBannerViewpagerBinding

class HomeBannerViewPagerAdapter(
    private var bannerList: MutableList<Bitmap?>
) : RecyclerView.Adapter<HomeBannerViewPagerAdapter.HomeBannerViewHolder>() {
    private var _binding: ItemBannerViewpagerBinding? = null
    private val binding get() = _binding!!

    override fun onCreateViewHolder(
        parent: ViewGroup, viewType: Int
    ): HomeBannerViewHolder {
        _binding = DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.item_banner_viewpager, parent, false)
        return HomeBannerViewHolder(binding)
    }

    override fun onBindViewHolder(holder: HomeBannerViewHolder, position: Int) {
        val bitmap = bannerList[position]
        holder.bind(bitmap)
    }

    override fun getItemCount(): Int = bannerList.size

    fun setImages(bitmaps: MutableList<Bitmap?>) {
        bannerList = bitmaps
        notifyDataSetChanged()
    }

    inner class HomeBannerViewHolder(
        private val binding: ItemBannerViewpagerBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(bitmap: Bitmap?) {
            binding.imageHomeBanner.setImageBitmap(bitmap)
        }
    }
}