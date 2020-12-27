package cn.edu.food_blog.pojo

data class Material(
    //所需要的食材
    val amount: String,//材料数量
    val mname: String,//材料名称
    val type: Int//主料为1，辅料为0
)