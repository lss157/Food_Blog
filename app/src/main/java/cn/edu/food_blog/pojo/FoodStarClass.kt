package cn.edu.food_blog.pojo

import com.google.gson.Gson

class FoodStarClass(var id: Int? = null,
                    var food_location:String) {
    override fun toString(): String {
        //转换成json字符串
        return Gson().toJson(this)
    }
}