package cn.edu.food_blog.pojo

import com.google.gson.Gson

class FoodPoster(val id: Int? = null,
                 val foodTitle: String = "unknown",
                 val foodText: String = "unknown",
                 val foodImage: String) {
    override fun toString(): String {
        //转换成json字符串
        return Gson().toJson(this)
    }
}