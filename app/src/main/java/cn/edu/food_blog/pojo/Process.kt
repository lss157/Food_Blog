package cn.edu.food_blog.pojo

import android.graphics.Bitmap

//制作工程
data class Process(
    val pcontent: String,
    val pic: String,
    var picImg: Bitmap? = null
)