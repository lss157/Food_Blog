package cn.edu.food_blog

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import androidx.core.content.FileProvider
import kotlinx.android.synthetic.main.activity_update.*
import java.io.File

class UpdateActivity : AppCompatActivity() {

    val fromAlbum = 2

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_update)
        //拍照上传图片
        takePhotoBtn.setOnClickListener {
            dispatchTakePictureIntent()
        }
        //从相册中选择照片
        fromAlbumBtn.setOnClickListener {
            val intent = Intent(Intent.ACTION_OPEN_DOCUMENT)
            intent.addCategory(Intent.CATEGORY_OPENABLE)
            //指定显示图片
            intent.type = "image/*"
            startActivityForResult(intent,fromAlbum)
        }
    }
    val REQUESU_IMAGE_CAPTURE = 1
    private fun dispatchTakePictureIntent(){
        val takepictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        if (intent.resolveActivity(packageManager) != null){
            startActivityForResult(takepictureIntent,REQUESU_IMAGE_CAPTURE)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUESU_IMAGE_CAPTURE && resultCode == Activity.RESULT_OK){
            val imageBitmap = data?.extras?.get("data") as Bitmap
            imageView.setImageBitmap(imageBitmap)
        }
        else if(requestCode == fromAlbum && resultCode == Activity.RESULT_OK && data!=null){
            data.data?.let {uri ->
                val bitmap = getBitmapFromUri(uri)
                imageView.setImageBitmap(bitmap)
            }
        }
    }
    private fun getBitmapFromUri(uri:Uri) = contentResolver
        .openFileDescriptor(uri,"r")?.use{
        BitmapFactory.decodeFileDescriptor(it.fileDescriptor)
    }
}