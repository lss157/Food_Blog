package cn.edu.food_blog.pojo

import android.graphics.Bitmap
//每个小菜单的展示
data class NetFoodDesc(
    val name: String,
    val content: String,
    val tag: String,
    val peoplenum: String,
    var pic: Bitmap? = null
)