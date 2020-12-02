package cn.edu.food_blog.fragment

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import cn.edu.food_blog.R
import kotlinx.android.synthetic.main.fragment_update.*


class UpdateFragment : Fragment() {

    val fromAlbum = 2
    val REQUESU_IMAGE_CAPTURE = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_update, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
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

    private fun dispatchTakePictureIntent(){
        val takepictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        if (activity!!.intent.resolveActivity(activity!!.packageManager) != null){
            startActivityForResult(takepictureIntent,REQUESU_IMAGE_CAPTURE)
        }
    }

    private fun getBitmapFromUri(uri: Uri) = activity!!.contentResolver
        .openFileDescriptor(uri,"r")?.use{
            BitmapFactory.decodeFileDescriptor(it.fileDescriptor)
        }

}