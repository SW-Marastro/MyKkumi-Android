package com.swmarastro.mykkumi.feature.post

import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.swmarastro.mykkumi.feature.post.databinding.ItemSelectPostImageBinding
import com.swmarastro.mykkumi.feature.post.touchEvent.ItemTouchHelperListener

class SelectPostImageListAdapter (
    private val onClickPostImage: (image: Uri) -> Unit
) : RecyclerView.Adapter<SelectPostImageListAdapter.SelectPostImageListViewHolder>(),
    ItemTouchHelperListener {

    var postImageList = mutableListOf<Uri>()
        set(value) { // 데이터 추가되면 마지막 데이터를 선택
            field = value
            selectImagePosition = value.size - 1
            notifyDataSetChanged()
        }
    var selectImagePosition: Int = 0

    // 아이템 이동 - 드래그로 이동 시 호출됨
    // from: 드래그가 시작되는 위치
    // to: 이동되는 위치
    override fun onItemMove(from: Int, to: Int) {
        // 드래그 되고있는 아이템을 변수로 지정 (dragItem)
        val dragItem: Uri = postImageList[from]
        postImageList.removeAt(from)      // 드래그 되고 있는 아이템 제거
        postImageList.add(to, dragItem)   // 드래그 끝나는 지점에 추가

        notifyItemMoved(from, to)

        // 드래그 된 것 중에 선택된 이미지가 있으면 그것도 체크
        if(selectImagePosition == to) {
            selectImagePosition = from
            notifyItemChanged(selectImagePosition)
        }
        else if(selectImagePosition == from) {
            selectImagePosition = to
            notifyItemChanged(selectImagePosition)
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): SelectPostImageListViewHolder {
        val binding = ItemSelectPostImageBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SelectPostImageListViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: SelectPostImageListViewHolder,
        position: Int
    ) {
        holder.bind(postImageList[position], position)
    }

    override fun getItemCount(): Int = postImageList.size

    inner class SelectPostImageListViewHolder(
        private val binding: ItemSelectPostImageBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Uri, position: Int) {
            binding.imagePostEditThumbnail.load(item)

            if (position == selectImagePosition) {
                binding.imagePostEditThumbnail.setBackgroundResource(R.drawable.shape_select_post_image)
            }
            else {
                binding.imagePostEditThumbnail.background = null
            }

            // 편집할 이미지 선택
            binding.imagePostEditThumbnail.setOnClickListener(View.OnClickListener {
                selectImagePosition = position
                notifyDataSetChanged()

                onClickPostImage(item)
            })

            // 이미지 삭제
            binding.btnDeleteEditImage.setOnClickListener {
                postImageList.removeAt(position)
                notifyItemRemoved(position)

                if(position >= postImageList.size) {
                    selectImagePosition = postImageList.size - 1
                    notifyItemChanged(selectImagePosition)
                }
            }
        }
    }
}