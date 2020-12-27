package cn.edu.food_blog.activity

import android.app.Activity
import android.content.Intent
import android.database.sqlite.SQLiteDatabase
import android.graphics.BitmapFactory
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import cn.edu.food_blog.R
import cn.edu.food_blog.adapter.NetFoodProcessAdapter
import cn.edu.food_blog.net.NetFoodApi
import cn.edu.food_blog.pojo.FoodStarClass
import cn.edu.food_blog.pojo.Material
import cn.edu.food_blog.pojo.Process
import cn.edu.food_blog.util.NetApiCreator
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.android.synthetic.main.activity_food_detail.*
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

//食物详情页
class FoodDetailActivity : AppCompatActivity() {
    private lateinit var netFoodApi: NetFoodApi
    private lateinit var foodProcessList: ArrayList<Process>
    private lateinit var adapter: NetFoodProcessAdapter
    private lateinit var layoutManager: RecyclerView.LayoutManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_food_detail)

        netFoodApi = NetApiCreator.create(NetFoodApi::class.java)
        foodProcessList = ArrayList()
        adapter = NetFoodProcessAdapter(foodProcessList)
        layoutManager = LinearLayoutManager(this)
        foodProcessRecView.adapter = adapter
        foodProcessRecView.layoutManager = layoutManager

        setSupportActionBar(toolBarFoodDetail)
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
        }

        loadFoodDate()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> finish()
        }
        return true
    }

    //加载食物数据
    private fun loadFoodDate() {
        intent.run {
            foodDetailCollapseBar.title = getStringExtra("foodName")
            foodDetailImage.setImageBitmap(getParcelableExtra("foodImg"))
            foodDescription.text = getStringExtra("foodContent")
            val gson = Gson()
            //加载食材主辅料
            val mType = object : TypeToken<List<Material>>() {}.type
            var materialList = gson.fromJson<List<Material>>(getStringExtra("foodMaterial"), mType)
            loadFoodMaterial(materialList)
            //加载工序
            val pType = object : TypeToken<List<Process>>() {}.type
            foodProcessList.addAll(gson.fromJson<List<Process>>(intent.getStringExtra("foodProcess"), pType))
            adapter.notifyDataSetChanged()
            loadFoodProcess(foodProcessList)
        }
    }

    //加载食物材料
    private fun loadFoodMaterial(materialList: List<Material>) {
        //主料列表
        var mainMaterialList = ArrayList<String>()
        //辅料列表
        var axuMaterialList = ArrayList<String>()
        for (m in materialList) {
            if (m.type == 1) {
                //主料type为1
                mainMaterialList.add("${m.mname} ${m.amount}")
            } else {
                //辅料
                axuMaterialList.add("${m.mname} ${m.amount}")
            }
        }
        //每个元素添加分隔符，并转为字符串
        if (!mainMaterialList.isEmpty()) {
            mainMaterial.text = "主料：" + mainMaterialList.joinToString()
            mainMaterial.visibility = View.VISIBLE
        }
        if (!axuMaterialList.isEmpty()) {
            axuMeterial.text = "辅料：" + axuMaterialList.joinToString()
            axuMeterial.visibility = View.VISIBLE
        }
    }

    //加载食物工序
    private fun loadFoodProcess(foodProcessList: List<Process>) {
        Log.d("load process", "$foodProcessList")
        for (p in foodProcessList) {
            val picUri = p.pic.split("/").run {
                get(size - 2) + "/" + get(size - 1)
            }
            netFoodApi.getFoodImage(picUri).enqueue(object : Callback<ResponseBody> {
                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    t.printStackTrace()
                }
                override fun onResponse(
                    call: Call<ResponseBody>,
                    response: Response<ResponseBody>
                ) {
                    response.body()?.run {
                        val bm = BitmapFactory.decodeStream(byteStream())
                        p.picImg = bm
                        adapter.notifyDataSetChanged()
                    }
                }
            })
        }
    }

}