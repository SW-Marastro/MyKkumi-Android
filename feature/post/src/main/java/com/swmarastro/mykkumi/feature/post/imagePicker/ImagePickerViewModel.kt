package com.swmarastro.mykkumi.feature.post.imagePicker

import android.annotation.SuppressLint
import android.content.Context
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject

private const val INDEX_MEDIA_ID = MediaStore.MediaColumns._ID
private const val INDEX_MEDIA_URI = MediaStore.MediaColumns.DATA
private const val INDEX_ALBUM_NAME = MediaStore.Images.Media.BUCKET_DISPLAY_NAME
private const val INDEX_DATE_ADDED = MediaStore.MediaColumns.DATE_ADDED

@HiltViewModel
class ImagePickerViewModel @Inject constructor(
) : ViewModel() {
    private val _imagePickerUiState = MutableLiveData<MutableList<ImagePickerData>>(mutableListOf())
    val imagePickerUiState : LiveData<MutableList<ImagePickerData>> get() = _imagePickerUiState

    // 카메라로 촬영할 이미지를 저장할 path
    private var cameraImagePath : Uri? = null

    @SuppressLint("Range")
    fun fetchImageItemList(context: Context) {
        val uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        val projection = arrayOf(
            INDEX_MEDIA_ID,
            INDEX_MEDIA_URI,
            INDEX_ALBUM_NAME,
            INDEX_DATE_ADDED
        )
        val selection =
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) MediaStore.Images.Media.SIZE + " > 0"
            else null
        val sortOrder = "$INDEX_DATE_ADDED DESC"
        val cursor = context.contentResolver.query(uri, projection, selection, null, sortOrder)

        _imagePickerUiState.value = mutableListOf()

        cursor?.let {
            while(cursor.moveToNext()) {
                val mediaPath = cursor.getString(cursor.getColumnIndex(INDEX_MEDIA_URI))
                _imagePickerUiState.value!!.add(
                    ImagePickerData(
                        localUri = Uri.fromFile(File(mediaPath)),
                        presignedUri = null,
                        isSelect = false)
                )
            }
        }

        cursor?.close()
    }

    // 카메라로 촬영한 이미지가 저장될 경로
    fun setCameraImagePath(path: Uri) {
        cameraImagePath = path
    }

    // 카메라로 촬영한 이미지 선택
    fun doneSetCameraImage(navController: NavController?) {
        val selectImages = mutableListOf<Uri>(cameraImagePath!!)
        navController?.previousBackStackEntry?.savedStateHandle?.set("selectImages", ImagePickerArgument(selectImages))
        navController?.popBackStack()
    }

    fun doneSelectImages(navController: NavController?) {
        // 선택된 이미지
        val selectImages = mutableListOf<Uri>()
        imagePickerUiState.value.let {
            for (image in imagePickerUiState.value!!) {
                if (image.isSelect) selectImages.add(image.localUri)
            }
        }

        navController?.previousBackStackEntry?.savedStateHandle?.set("selectImages", ImagePickerArgument(selectImages))
        navController?.popBackStack()
    }
}