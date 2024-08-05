package com.swmarastro.mykkumi.feature.post

import android.net.Uri
import android.util.Log
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

    var postImageList = mutableListOf<PostImageData>()
        set(value) { // 데이터 추가되면 마지막 데이터를 선택
            field = value
            val oldSelect = selectImagePosition
            selectImagePosition = field.size - 1
            if(oldSelect < field.size && oldSelect >= 0 && selectImagePosition >= 0) {
                field[oldSelect].isSelect = false
                field[selectImagePosition].isSelect = true
            }
            notifyItemChanged(oldSelect)
            notifyItemChanged(selectImagePosition)
        }
    var selectImagePosition: Int = 0

    // 아이템 이동 - 드래그로 이동 시 호출됨
    // from: 드래그가 시작되는 위치
    // to: 이동되는 위치
    override fun onItemMove(from: Int, to: Int) {
        // 드래그 되고있는 아이템을 변수로 지정 (dragItem)
        val dragItem: PostImageData = postImageList[from]
        postImageList.removeAt(from)      // 드래그 되고 있는 아이템 제거
        postImageList.add(to, dragItem)   // 드래그 끝나는 지점에 추가

        notifyItemMoved(from, to)

        if(selectImagePosition == from) {
            selectImagePosition = to
        }
        else if(to <= selectImagePosition && selectImagePosition < from) {
            selectImagePosition++
        }
        else if (from < selectImagePosition && selectImagePosition <= to){
            selectImagePosition--
        }
    }

    // 아이템 선택 해제될 때- 전체 새로고침해서 index 재정리
    override fun clearItemView() {
        notifyDataSetChanged()
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
        fun bind(item: PostImageData, position: Int) {
            binding.imagePostEditThumbnail.load(item.localUri)

            if (item.isSelect) {
                binding.imagePostEditThumbnail.setBackgroundResource(R.drawable.shape_select_post_image)
            }
            else {
                binding.imagePostEditThumbnail.background = null
            }

            // 편집할 이미지 선택
            binding.imagePostEditThumbnail.setOnClickListener(View.OnClickListener {
                postImageList[selectImagePosition].isSelect = false
                item.isSelect = true

                Log.d("test position", selectImagePosition.toString() + " -> " + position.toString())

                selectImagePosition = position
                notifyDataSetChanged()

                onClickPostImage(item.localUri)
            })

            // 이미지 삭제
            binding.btnDeleteEditImage.setOnClickListener {
                postImageList.removeAt(position)

                if(selectImagePosition >= postImageList.size) {
                    selectImagePosition = postImageList.size - 1
                    postImageList[selectImagePosition].isSelect = true
                }
                else if(selectImagePosition == position) {
                    postImageList[selectImagePosition].isSelect = true
                }

                onClickPostImage(postImageList[selectImagePosition].localUri)
                notifyDataSetChanged()
            }
        }
    }
}