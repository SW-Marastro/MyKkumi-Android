package com.swmarastro.mykkumi.feature.home

import android.graphics.Bitmap
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.swmarastro.mykkumi.feature.home.databinding.ItemBannerViewpagerBinding
import java.util.TimerTask

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

// 배너 자동 전환
class AutoScrollTask(
    private val viewPager: ViewPager2,
    private val adapter: HomeBannerViewPagerAdapter
) : TimerTask() {

    private val handler = Handler(Looper.getMainLooper())

    override fun run() {
        handler.post {
            val currentItem = viewPager.currentItem
            val nextItem = if (currentItem == adapter.itemCount - 1) 0 else currentItem + 1
            viewPager.setCurrentItem(nextItem, true)
        }
    }
}