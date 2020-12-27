package cn.edu.food_blog.pojo

data class NetFood(
        //菜谱的详情
        var store:Boolean,
        val classid: Int,
        val content: String,
        val cookingtime: String,
        val id: Int,
        val material: List<Material>,
        val name: String,
        val peoplenum: String,
        val pic: String,
        val preparetime: String,
        val process: List<Process>,
        val tag: String
)