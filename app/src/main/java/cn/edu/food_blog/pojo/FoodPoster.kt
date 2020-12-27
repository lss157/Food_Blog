package cn.edu.food_blog.pojo

import com.google.gson.Gson
//我的食物的博客
class FoodPoster(var id: Int? = null,
                 var foodTitle: String = "unknown",
                 var foodText: String = "unknown",
                 var foodImage: String) {
    override fun toString(): String {
        //转换成json字符串
        return Gson().toJson(this)
    }
}