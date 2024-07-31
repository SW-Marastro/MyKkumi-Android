package com.swmarastro.mykkumi.feature.home.post

import android.graphics.Color
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.text.style.ForegroundColorSpan
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import coil.load
import com.swmarastro.mykkumi.common_ui.post.PostImagesAdapter
import com.swmarastro.mykkumi.domain.entity.HomePostItemVO
import com.swmarastro.mykkumi.common_ui.R
import com.swmarastro.mykkumi.feature.home.databinding.ItemPostRecyclerviewBinding

class PostListAdapter (
) : RecyclerView.Adapter<PostListAdapter.PostListViewHolder>(){

    var postList = mutableListOf<HomePostItemVO>()

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
        fun bind(item: HomePostItemVO) {
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
                item.images.toMutableList()
            )
            binding.viewpagerPostImages.adapter = postItemImageAdapter
            binding.viewpagerPostImages.orientation = ViewPager2.ORIENTATION_HORIZONTAL

            // 이미지 총 페이지 수 표시
            binding.textPostImagesTotalPage.text = "/" + item.images.size
            if(item.images.size != 0) binding.textPostImagesCurrentPage.text = "1"

            // 이미지 indicator
            binding.indicatorPostImage.createIndicator(item.images.size, 0)

            // 이미지 현재 페이지 표시 + indicator 이동
            binding.viewpagerPostImages.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    if(item.images.size != 0) {
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

            // 글 내용 --------------------------------------------------------------------------------------

            // val testContent = "#템빨 즉 아이템의 비중이 크거나 #다양한 취미를 즐기는 사람들이 모인 커뮤니티를 제공한다. #취미 생활에 관련된 팁을 주고 받거나, #새로운 제품과 스타일을 시도해볼 수 있다."

            // 닉네임 클릭 이벤트
            val clickableNicknameSpan = object : ClickableSpan() {
                override fun onClick(widget: View) {
                    Log.d("---", "닉네임 클릭")
                }

                // 스타일 설정
                override fun updateDrawState(ds: TextPaint) {
                    ds.isUnderlineText = false
                    ds.color = Color.parseColor("#000000")
                    ds.isFakeBoldText = true
                }
            }

            // 글 내용
            var originalSpannableStringBuilder = SpannableStringBuilder().apply {
                // 닉네임
                append(item.writer.nickname)
                // 닉네임 클릭 이벤트
                setSpan(
                    clickableNicknameSpan,
                    0,
                    item.writer.nickname.length,
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                )

                append("  ")

                // 글 내용 중 해시태그 색상 강조
                val words = item.content.split(" ")
                var startIndex = item.writer.nickname.length + 2 // 닉네임 + 공백 다음에 시작
                for (word in words) {
                    if (word.startsWith("#")) {
                        // 해시태그 시작과 끝
                        val start = startIndex
                        val end = startIndex + word.length

                        append(word)
                        setSpan(
                            ForegroundColorSpan(Color.BLUE),
                            start,
                            end,
                            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                        )
                    } else {
                        append(word)
                    }
                    append(" ")
                    startIndex += word.length + 1
                }
            }

            binding.textPostNicknameContent.text = originalSpannableStringBuilder
            binding.textPostNicknameContent.movementMethod = LinkMovementMethod.getInstance()

            // 글이 2줄을 넘어가면 '...더보기'로 숨기기
            binding.textPostNicknameContent.post {
                if(binding.textPostNicknameContent.lineCount > postContentMaxLine) {
                    // Log.d("--", binding.textPostNicknameContent.layout.getLineEnd(postContentMaxLine - 1).toString())

                    var hideSpannableStringBuilder = SpannableStringBuilder().apply {
                        // 닉네임
                        append(item.writer.nickname)
                        // 닉네임 클릭 이벤트
                        setSpan(
                            clickableNicknameSpan,
                            0,
                            item.writer.nickname.length,
                            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                        )

                        append("  ")

                        // 숨기기
                        val availableTextLength = binding.textPostNicknameContent.layout.getLineEnd(postContentMaxLine - 1) - item.writer.nickname.length - postContentShowMoreText.length - 2
                        val hideContent = item.content.substring(0, availableTextLength)

                        // 글 내용 중 해시태그 색상 강조
                        val words = hideContent.split(" ")
                        var startIndex = item.writer.nickname.length + 2 // 닉네임 + 공백 다음에 시작
                        for (word in words) {
                            if (word.startsWith("#")) {
                                // 해시태그 시작과 끝
                                val start = startIndex
                                val end = startIndex + word.length

                                append(word)
                                setSpan(
                                    ForegroundColorSpan(Color.BLUE),
                                    start,
                                    end,
                                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                                )

                            } else {
                                append(word)
                            }
                            append(" ")
                            startIndex += word.length + 1
                        }

                        // ...더보기
                        append(postContentShowMoreText)

                        // 더보기 클릭 이벤트
                        val clickableShowMoreSpan = object : ClickableSpan() {
                            override fun onClick(widget: View) {
                                binding.textPostNicknameContent.text = originalSpannableStringBuilder
                            }

                            // 스타일 설정
                            override fun updateDrawState(ds: TextPaint) {
                            }
                        }
                        setSpan(
                            clickableShowMoreSpan,
                            item.writer.nickname.length + hideContent.length + 2,
                            item.writer.nickname.length + hideContent.length + postContentShowMoreText.length + 2,
                            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                        )
                    }

                    binding.textPostNicknameContent.text = hideSpannableStringBuilder
                }
            }
        }
    }
}