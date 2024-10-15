package com.marastro.mykkumi.feature.home.post

import android.content.Context
import android.text.SpannableStringBuilder
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.marastro.mykkumi.common_ui.post.PostImagesAdapter
import com.marastro.mykkumi.domain.entity.HomePostItemVO
import com.marastro.mykkumi.common_ui.R
import com.marastro.mykkumi.common_ui.server_driven.SpannableStringBuilderProvider
import com.marastro.mykkumi.feature.home.HomeViewModel
import com.marastro.mykkumi.feature.home.databinding.ItemPostRecyclerviewBinding

class PostListAdapter (
    private val context: Context,
    private val navController: NavController?,
    private val waitNotice: () -> Unit,
    private val blockNestedSwipeRefreshAndViewPager: (state: Int) -> Unit,
    private val reportPost: (writerUuid: String, postId: Int) -> Unit,
    private val viewModel: HomeViewModel
) : RecyclerView.Adapter<PostListAdapter.PostListViewHolder>(){
    private final val MAX_CONTENT_LENGTH = 50

    var postList = mutableListOf<HomePostItemVO>()

    override fun onCreateViewHolder(
        parent: ViewGroup, viewType: Int
    ): PostListViewHolder {
        val binding = ItemPostRecyclerviewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PostListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PostListViewHolder, position: Int) {
        if (position >= 0 && position < postList.size) {
            holder.bind(postList[position], position)
        }
    }

    override fun getItemCount(): Int = postList.size

    inner class PostListViewHolder(
        private val binding: ItemPostRecyclerviewBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: HomePostItemVO, position: Int) {
            // 사용자 프로필
            if(item.writer.profileImage == null) {
                binding.includePostWriter.imageWriterProfile.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.img_profile_default))
            }
            else {
                Glide
                    .with(context)
                    .load(item.writer.profileImage)
                    .placeholder(R.drawable.img_loading_post)
                    .circleCrop()
                    .into(binding.includePostWriter.imageWriterProfile)
            }

            binding.includePostWriter.textWriterNickname.text = item.writer.nickname // 닉네임
            binding.textContentWriterNickname.text = item.writer.nickname
            binding.includePostWriter.textPostCategory.text = item.category + " - " + item.subCategory // 포스트 카테고리

            // 포스트 이미지 viewpager
            var postItemImageAdapter: PostImagesAdapter = PostImagesAdapter(
                context,
                item.images.toMutableList()
            )
            binding.viewpagerPostImages.adapter = postItemImageAdapter
            binding.viewpagerPostImages.orientation = ViewPager2.ORIENTATION_HORIZONTAL

            val viewPager = binding.viewpagerPostImages
            viewPager.viewTreeObserver.addOnGlobalLayoutListener {
                viewPager.layoutParams.height = viewPager.width
                viewPager.requestLayout()
            }

            // 이미지 indicator
            if(item.images.size > 1)
                binding.indicatorPostImage.createIndicator(item.images.size, 0)
            else
                binding.indicatorPostImage.visibility = View.GONE

            // 이미지 현재 페이지 표시 + indicator 이동
            binding.viewpagerPostImages.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    if(item.images.size != 0) {
                        binding.indicatorPostImage.selectDot(position)
                    }
                }

                // Swipe 새로고침과 ViewPager 이벤트 중첩 방지
                override fun onPageScrollStateChanged(state: Int) {
                    super.onPageScrollStateChanged(state)

                    blockNestedSwipeRefreshAndViewPager(state)
                }
            })

            // 좋아요 수
            binding.textPostLikeCount.visibility = View.GONE

            // 댓글 수
            binding.textPostCommentCount.visibility = View.GONE

            // 스크랩 수
            binding.textPostScrapCount.visibility = View.GONE

            // 좋아요 버튼 - 아직 안 됨
            binding.btnPostLike.setOnClickListener {
//                binding.btnPostLike.setImageResource(R.drawable.ic_like_checked)
                waitNotice()
            }
            // 스크랩 버튼 - 아직 안 됨
            binding.btnPostScrap.setOnClickListener {
//                binding.btnPostScrap.setImageResource(R.drawable.ic_scrap_checked)
                waitNotice()
            }
            // 댓글 버튼 - 아직 안 됨
            binding.btnPostComment.setOnClickListener {
                waitNotice()
            }
            // 공유 버튼 - 아직 안 됨
            binding.btnPostShare.setOnClickListener {
                waitNotice()
            }

            // 포스트 신고하기
            binding.textBtnPostReport.setOnClickListener(View.OnClickListener {
                reportPost(
                    item.writer.uuid,
                    item.id
                )
            })

            // 로그인 된 유저한테만 보여줄 것 - 팔로우, 신고하기 버튼
            if(viewModel.isLogined.value) {
                binding.includePostWriter.btnFollow.visibility = View.VISIBLE // 팔로우
//                binding.textBtnPostReport.visibility = View.VISIBLE // 신고하기

                // 팔로우 버튼 - 아직 안 됨
                binding.includePostWriter.btnFollow.setOnClickListener(View.OnClickListener {
                    waitNotice()
                })
            }
            else {
                binding.includePostWriter.btnFollow.visibility = View.GONE // 팔로우
//                binding.textBtnPostReport.visibility = View.GONE // 신고하기
            }

            // 댓글 작성 버튼 - 아직 안 됨
            binding.textBtnAddComment.setOnClickListener(View.OnClickListener {
                waitNotice()
            })

            // 글 내용 --------------------------------------------------------------------------------------
            binding.textBtnMoreContent.visibility = View.GONE
            if(item.content.isNullOrEmpty()) {
                binding.textPostContent.visibility = View.GONE
            }
            else {
                binding.textPostContent.visibility = View.VISIBLE

                val allContent = item.content?.let { content ->
                    SpannableStringBuilderProvider
                        .getSpannableStringBuilder(
                            content,
                            navController
                        )
                }

                var hideContent = allContent
                // 본문 길이가 MAX_CONTENT_LENGTH를 넘는 경우
                if (hideContent != null && hideContent.length > MAX_CONTENT_LENGTH) {
                    val truncatedString =
                        hideContent.subSequence(0, MAX_CONTENT_LENGTH) as SpannableStringBuilder

                    // 단어 기준으로 자르기 위해 공백, 줄바꿈 위치 찾기
                    val lastSpaceIndex = truncatedString.lastIndexOf(' ')
                    val lastNewLineIndex = truncatedString.lastIndexOf('\n')

                    // 공백과 줄바꿈 중 더 뒤에 있는 것 or 둘다 없다면 MAX length
                    val finalCutIndex = when {
                        lastSpaceIndex != -1 && lastSpaceIndex > lastNewLineIndex -> lastSpaceIndex
                        lastNewLineIndex != -1 -> lastNewLineIndex
                        else -> MAX_CONTENT_LENGTH
                    }

                    hideContent =
                        SpannableStringBuilder(truncatedString.subSequence(0, finalCutIndex))

                    if (hideContent.length != allContent!!.length) {
                        hideContent.apply {
                            append("...")
                        }

                        binding.textBtnMoreContent.visibility = View.VISIBLE
                    }
                }
                binding.textPostContent.text = hideContent

                // 더보기 버튼
                binding.textBtnMoreContent.setOnClickListener(View.OnClickListener {
                    binding.textBtnMoreContent.visibility = View.GONE     // 더보기 버튼 가리기
                    binding.textBtnCloseContent.visibility = View.VISIBLE // 접기 버튼 보여주기
                    binding.textPostContent.text = allContent             // 글 전체 보여주기
                })

                // 접기 버튼
                binding.textBtnCloseContent.setOnClickListener(View.OnClickListener {
                    binding.textBtnMoreContent.visibility = View.VISIBLE // 더보기 버튼 보여주기
                    binding.textBtnCloseContent.visibility = View.GONE   // 접기 버튼 가리기
                    binding.textPostContent.text = hideContent           // 글 일부 숨기기
                })
            }

            // 마지막 아이템은 선 지우기
            if(postList.size - 1 == position) {
                binding.viewBottomLine.visibility = View.GONE
            }
            else {
                binding.viewBottomLine.visibility = View.VISIBLE
            }
        }
    }
}