package cn.edu.food_blog.net

import cn.edu.food_blog.pojo.Response
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface NetFoodApi {

    companion object {
        //请求的基地址
        const val BASE_URL = "https://api.jisuapi.com/"
        //我的appkey
        const val APPKEY = "a10ddb787ca81070"
    }

    //分页请求食物列表
    @GET("/recipe/byclass")
    fun getFoodList(@Query("classid") classId: String,
                    @Query("start") start: String,
                    @Query("num") num: String,
                    @Query("appkey") appkey:String = APPKEY) : Call<Response>

    //请求食物图片
    @GET("/recipe/upload/{path}")
    fun getFoodImage(@Path("path") uri: String) : Call<ResponseBody>
}