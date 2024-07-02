package com.swmarastro.mykkumi.feature.home.post

import android.graphics.Color
import android.graphics.Typeface
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.text.style.StyleSpan
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import coil.load
import com.swmarastro.mykkumi.common_ui.post.PostImagesAdapter
import com.swmarastro.mykkumi.domain.entity.PostItemVO
import com.swmarastro.mykkumi.common_ui.R
import com.swmarastro.mykkumi.feature.home.databinding.ItemPostRecyclerviewBinding

class PostListAdapter (
    private var postList: MutableList<PostItemVO>
) : RecyclerView.Adapter<PostListAdapter.PostListViewHolder>(){

    private val postContentMaxLine = 2
    private val postContentShowMoreText = "...더보기"

    override fun onCreateViewHolder(
        parent: ViewGroup, viewType: Int
    ): PostListViewHolder {
        val binding = ItemPostRecyclerviewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PostListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PostListViewHolder, position: Int) {
        holder.bind(postList[position])
    }

    override fun getItemCount(): Int = postList.size

    inner class PostListViewHolder(
        private val binding: ItemPostRecyclerviewBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: PostItemVO) {
            binding.includePostWriter.imageWriterProfile.load(item.writer.profileImage) // 사용자 프로필
            binding.includePostWriter.textWriterNickname.text = item.writer.nickname // 닉네임
            binding.includePostWriter.textPostCategory.text = item.category + " - " + item.subCategory // 포스트 카테고리

            // 사용자 정보 우측 점3개 선택 -> 신고하기 버튼 보여주기
            binding.frameUserComplaint.visibility = View.GONE
            binding.includePostWriter.btnPostMoreMenu.setOnClickListener {
                if(binding.frameUserComplaint.visibility == View.GONE)
                    binding.frameUserComplaint.visibility = View.VISIBLE
                else
                    binding.frameUserComplaint.visibility = View.GONE
            }

            // 포스트 이미지 viewpager
            var postItemImageAdapter: PostImagesAdapter = PostImagesAdapter(
                item.image.toMutableList()
            )
            binding.viewpagerPostImages.adapter = postItemImageAdapter
            binding.viewpagerPostImages.orientation = ViewPager2.ORIENTATION_HORIZONTAL

            // 이미지 총 페이지 수 표시
            binding.textPostImagesTotalPage.text = "/" + item.image.size
            if(item.image.size != 0) binding.textPostImagesCurrentPage.text = "1"

            // 이미지 indicator
            binding.indicatorPostImage.createIndicator(item.image.size, 0)

            // 이미지 현재 페이지 표시 + indicator 이동
            binding.viewpagerPostImages.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    if(item.image.size != 0) {
                        binding.textPostImagesCurrentPage.text = (position + 1).toString()
                        binding.indicatorPostImage.selectDot(position)
                    }
                }
            })

            // 좋아요 버튼 - 일단 클릭 이벤트 세팅만 -> 데이터 변경되는 건 이후에 수정
            binding.btnPostLike.setOnClickListener {
                binding.btnPostLike.setImageResource(R.drawable.ic_like_checked)
            }
            // 스크랩 버튼 - 일단 클릭 이벤트 세팅만 -> 데이터 변경되는 건 이후에 수정
            binding.btnPostScrap.setOnClickListener {
                binding.btnPostScrap.setImageResource(R.drawable.ic_scrap_checked)
            }

            // 글 내용
            val spannableStringBuilder = SpannableStringBuilder().apply {
                // 닉네임
                append(item.writer.nickname)
                setSpan(
                    StyleSpan(Typeface.BOLD),
                    0,
                    item.writer.nickname.length,
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                )

                val clickableSpan = object : ClickableSpan() {
                    override fun onClick(widget: View) {
                        Log.d("---", "닉네임 클릭")
                    }

                    // 스타일 설정
                    override fun updateDrawState(ds: TextPaint) {
                        ds.isUnderlineText = false
                        ds.color = Color.parseColor("#000000")
                    }
                }
                setSpan(
                    clickableSpan,
                    0,
                    item.writer.nickname.length,
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                )

                // 글 내용
                append("  " + item.content)
            }

            binding.textPostNicknameContent.text = spannableStringBuilder
            binding.textPostNicknameContent.movementMethod = LinkMovementMethod.getInstance()
            /*binding.textPostNicknameContent.post {
                if(binding.textPostNicknameContent.lineCount > postContentMaxLine) {

                }
            }*/
        }
    }
}