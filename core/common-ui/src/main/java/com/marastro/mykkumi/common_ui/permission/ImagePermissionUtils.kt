package com.marastro.mykkumi.common_ui.permission

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.view.View
import androidx.activity.result.ActivityResultLauncher
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.getString
import androidx.core.content.FileProvider
import com.google.android.material.snackbar.Snackbar
import com.marastro.mykkumi.common_ui.R
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Date

object ImagePermissionUtils {

    const val MULTIPLE_PERMISSION = 10235;

    // 갤러리, 카메라 접근권한
    val multiplePermissions = mutableListOf(
        android.Manifest.permission.CAMERA
    ).apply {
        // sdk version 28 이하 - 사진 촬영 후 저장 권한 허용 요청
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.P) {
            add(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
        }
        // sdk version 32 이하 - 파일 접근 권한 허용 요청
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.S) {
            add(android.Manifest.permission.READ_EXTERNAL_STORAGE)
        }
        // sdk version 33 이상 - 이미지 접근 권한 허용 요청
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            add(android.Manifest.permission.READ_MEDIA_IMAGES)
        }
        // sdk version 34 이상 - READ_MEDIA_VISUAL_USER_SELECTED
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
            add(android.Manifest.permission.READ_MEDIA_VISUAL_USER_SELECTED)
        }
    }

    fun allPermissionsGranted(context: Context): Boolean {
        return multiplePermissions.all {
            ContextCompat.checkSelfPermission(context, it) == PackageManager.PERMISSION_GRANTED
        }
    }

    fun shouldShowRationale(activity: Activity): Boolean {
        return multiplePermissions.any {
            ActivityCompat.shouldShowRequestPermissionRationale(activity, it)
        }
    }

    fun requestPermissions(activity: Activity) {
        ActivityCompat.requestPermissions(activity, multiplePermissions.toTypedArray(), MULTIPLE_PERMISSION)
    }

    fun showSettingsSnackbar(activity: Activity, view: View, intent: Intent) {
        val message = getString(activity, R.string.notice_permission_revoke_go_setting)
        val actionLabel = getString(activity, R.string.action_permission_revoke_go_setting)
        Snackbar.make(view, message, Snackbar.LENGTH_LONG)
            .setAction(actionLabel) {
                activity.startActivity(intent)
            }.show()
    }

    fun chooserImageIntent(
        localContext: Context,
        launcher: ActivityResultLauncher<Intent>,
        onImageUriCreated: (Uri) -> Unit
    ) {
        val chooserTitle = getString(localContext, R.string.choose_way_for_image)

        // 갤러리에서 불러오기
        val galleryIntent = Intent(Intent.ACTION_PICK)
        galleryIntent.type = "image/*"

        // 카메라로 사진 찍기 Intent
        val captureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { intent ->
            val photoFile: File? = try {
                createImageFile(localContext)
            } catch (ex: IOException) {
                null
            }

            photoFile?.also {
                val photoURI: Uri = FileProvider.getUriForFile(
                    localContext, "${localContext.packageName}.provider", it
                )
                intent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                onImageUriCreated(photoURI)
            }
        }

        // 다중 Intent 선택 창
        val chooserIntent = Intent.createChooser(galleryIntent, chooserTitle).apply {
            putExtra(Intent.EXTRA_INITIAL_INTENTS, arrayOf(captureIntent))
        }

        launcher.launch(chooserIntent)
    }

    fun captureWithCamera(
        localContext: Context,
        launcher: ActivityResultLauncher<Intent>,
        onImageUriCreated: (Uri) -> Unit
    ) {
        // 카메라로 사진 찍기 Intent
        val captureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { intent ->
            val photoFile: File? = try {
                createImageFile(localContext)
            } catch (ex: IOException) {
                null
            }

            photoFile?.also {
                val photoURI: Uri = FileProvider.getUriForFile(
                    localContext, "${localContext.packageName}.provider", it
                )
                intent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                onImageUriCreated(photoURI)
            }
        }

        launcher.launch(captureIntent)
    }

    @Throws(IOException::class)
    private fun createImageFile(localContext: Context): File {
        val timeStamp: String = SimpleDateFormat("yyyy-MM-dd_HH_mm_ss").format(Date())
        val storageDir: File? = localContext.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        val imageFile = File.createTempFile (
            "JPEG_${timeStamp}_",
            ".jpg",
            storageDir
        )

        return imageFile
    }
}