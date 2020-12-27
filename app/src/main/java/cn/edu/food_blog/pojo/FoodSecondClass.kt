package cn.edu.food_blog.pojo
//第二级菜单
data class FoodSecondClass(
    val classid: Int,
    val name: String,
    val parentid: Int
) {
    override fun toString(): String {
        return name
    }
}