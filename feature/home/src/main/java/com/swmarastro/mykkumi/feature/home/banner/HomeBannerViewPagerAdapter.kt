package com.swmarastro.mykkumi.feature.home.banner

import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.swmarastro.mykkumi.domain.entity.BannerItemVO
import java.util.TimerTask
import coil.load
import com.swmarastro.mykkumi.feature.home.databinding.ItemBannerViewpagerBinding

class HomeBannerViewPagerAdapter(
    //private var bannerList: MutableList<Bitmap?>,
    private var bannerList: MutableList<BannerItemVO>,
    private val onClickBannerItem: (bannerId: Int) -> Unit
) : RecyclerView.Adapter<HomeBannerViewPagerAdapter.HomeBannerViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup, viewType: Int
    ): HomeBannerViewHolder {
        val binding = ItemBannerViewpagerBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return HomeBannerViewHolder(binding)
    }

    override fun onBindViewHolder(holder: HomeBannerViewHolder, position: Int) {
        val index = position % bannerList.size
        //val bitmap = bannerList[index]
        //holder.bind(bitmap)
        val item = bannerList[index]
        holder.bind(item)
    }

    override fun getItemCount(): Int {
        if(bannerList.size == 0) return 0
        else return Int.MAX_VALUE // 무한 페이지 뷰를 위한
    }

    /*fun setImages(bitmaps: MutableList<Bitmap?>) {
        bannerList = bitmaps
        notifyDataSetChanged()
    }*/

    inner class HomeBannerViewHolder(
        private val binding: ItemBannerViewpagerBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: BannerItemVO) {
            // binding.imageHomeBanner.setImageBitmap(bitmap)
            binding.imageHomeBanner.load(item.imageUrl)

            // 배너 클릭
            binding.imageHomeBanner.setOnClickListener(View.OnClickListener {
                onClickBannerItem(item.id)
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