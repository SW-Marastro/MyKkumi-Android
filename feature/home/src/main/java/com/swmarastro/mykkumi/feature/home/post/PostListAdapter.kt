package com.swmarastro.mykkumi.feature.home.post

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.swmarastro.mykkumi.domain.entity.PostItemVO
import com.swmarastro.mykkumi.feature.home.R
import com.swmarastro.mykkumi.feature.home.databinding.ItemHomePostRecyclerviewBinding

class PostListAdapter (
    private var postList: MutableList<PostItemVO>
) : RecyclerView.Adapter<PostListAdapter.PostListViewHolder>(){
    private var _binding: ItemHomePostRecyclerviewBinding? = null
    private val binding get() = _binding!!

    override fun onCreateViewHolder(
        parent: ViewGroup, viewType: Int
    ): PostListViewHolder {
        _binding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.item_home_post_recyclerview, parent, false)
        return PostListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PostListViewHolder, position: Int) {
        holder.bind(postList[position])
    }

    override fun getItemCount(): Int = postList.size

    inner class PostListViewHolder(
        private val binding: ItemHomePostRecyclerviewBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: PostItemVO) {
            binding.includePostWriter.imageWriterProfile.load(item.writer.profileImage) // 사용자 프로필
            binding.includePostWriter.textWriterNickname.text = item.writer.nickname // 닉네임
            binding.includePostWriter.textPostCategory.text = item.category + " - " + item.subCategory // 포스트 카테고리

            binding.frameUserComplaint.visibility = View.GONE
            binding.includePostWriter.btnPostMoreMenu.setOnClickListener {
                if(binding.frameUserComplaint.visibility == View.GONE)
                    binding.frameUserComplaint.visibility = View.VISIBLE
                else
                    binding.frameUserComplaint.visibility = View.GONE
            }

            // 좋아요 버튼 - 일단 클릭 이벤트 세팅만 -> 데이터 변경되는 건 이후에 수정
            binding.btnPostLike.setOnClickListener {
                binding.btnPostLike.setImageResource(R.drawable.ic_like_checked)
            }
            // 스크랩 버튼 - 일단 클릭 이벤트 세팅만 -> 데이터 변경되는 건 이후에 수정
            binding.btnPostScrap.setOnClickListener {
                binding.btnPostScrap.setImageResource(R.drawable.ic_scrap_checked)
            }
        }
    }
}