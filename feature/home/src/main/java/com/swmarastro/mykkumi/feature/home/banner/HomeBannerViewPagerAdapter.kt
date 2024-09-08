package com.swmarastro.mykkumi.feature.home.banner

import android.content.Context
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.swmarastro.mykkumi.domain.entity.BannerItemVO
import java.util.TimerTask
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.swmarastro.mykkumi.feature.home.databinding.ItemBannerViewpagerBinding
import com.swmarastro.mykkumi.feature.home.databinding.ItemLastBannerViewpagerBinding

class HomeBannerViewPagerAdapter(
    private val context: Context,
    private var bannerList: MutableList<BannerItemVO>,
    private val onClickBannerItem: (bannerId: Int) -> Unit,
    private val navigateBannerAll: () -> Unit,
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        private const val TYPE_BANNER = 0
        private const val TYPE_LAST_BANNER = 1
    }

    override fun getItemViewType(position: Int): Int {
        return if(position % bannerList.size == bannerList.size - 1) TYPE_LAST_BANNER
        else TYPE_BANNER
    }

    override fun onCreateViewHolder(
        parent: ViewGroup, viewType: Int
    ) = when (viewType) {
        TYPE_BANNER -> {
            val binding = ItemBannerViewpagerBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            HomeBannerViewHolder(binding)
        }
        TYPE_LAST_BANNER -> {
            val binding = ItemLastBannerViewpagerBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            HomeLastBannerViewHolder(binding)
        }
        else -> throw IllegalArgumentException("Invalid view type")
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val index = position % bannerList.size
        val item = bannerList[index]

        when(holder) {
            is HomeBannerViewHolder -> {
                holder.bind(item)
            }
            is HomeLastBannerViewHolder -> {
                holder.bind(item)
            }
            else -> throw IllegalArgumentException("Invalid view type")
        }
    }

    override fun getItemCount(): Int {
        if(bannerList.isEmpty()) return 0
        else return Int.MAX_VALUE // 무한 페이지 뷰를 위한
    }

    inner class HomeBannerViewHolder(
        private val binding: ItemBannerViewpagerBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: BannerItemVO) {
            Glide
                .with(context)
                .load(item.imageUrl)
                .transform(RoundedCorners(12))
                .into(binding.imageHomeBanner)

            // 배너 클릭
            binding.imageHomeBanner.setOnClickListener(View.OnClickListener {
                onClickBannerItem(item.id)
            })
        }
    }

    inner class HomeLastBannerViewHolder(
        private val binding: ItemLastBannerViewpagerBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: BannerItemVO) {
            binding.textBtnMoreBanner.setOnClickListener(View.OnClickListener {
                navigateBannerAll()
            })
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