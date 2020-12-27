package cn.edu.food_blog.pojo
//第一级菜单
data class FoodFirstClass (
    val classid: Int,
    val list: ArrayList<FoodSecondClass>,
    val name: String,
    val parentid: Int
)