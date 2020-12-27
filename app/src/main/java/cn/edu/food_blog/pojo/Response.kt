package cn.edu.food_blog.pojo

/**
 * 网络请求的返回
 */
data class Response(
        //msg和status代表食物列表请求是否返回成功
        //msg="ok",status=0返回成功
        val msg: String,
        val result: NetFoodList,
        val status: Int
)