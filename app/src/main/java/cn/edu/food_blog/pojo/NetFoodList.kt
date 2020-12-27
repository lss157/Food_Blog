package cn.edu.food_blog.pojo

/**
 * 请求远程食物列表的返回实体
 */
data class NetFoodList (
    val list: List<NetFood>,
    val num: Int,
    val total: Int
)