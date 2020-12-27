package cn.edu.food_blog.util

import cn.edu.food_blog.net.NetFoodApi
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object NetApiCreator {
    //retrofit的封装工具
    private val retrofit =
        Retrofit.Builder()
            .baseUrl(NetFoodApi.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    //生成了实例
    fun <T> create(clazz: Class<T>) = retrofit.create(clazz)

}