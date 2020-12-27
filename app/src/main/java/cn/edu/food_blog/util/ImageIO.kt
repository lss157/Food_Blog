package cn.edu.food_blog.util

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build

object ImageIO {

    //根据uri读取图片
    fun getBitmapFromUri(context: Context, uri: Uri): Bitmap {
        //读取到图片后申请持久化访问权限
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            context.contentResolver.takePersistableUriPermission(uri, Intent.FLAG_GRANT_READ_URI_PERMISSION)
        }
        return context.contentResolver.openFileDescriptor(uri, "r")
            .use { BitmapFactory.decodeFileDescriptor(it?.fileDescriptor) }
    }

}