package cn.edu.food_blog.activity

import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import cn.edu.food_blog.R
import cn.edu.food_blog.adapter.NetFoodDescAdapter
import cn.edu.food_blog.net.NetFoodApi
import cn.edu.food_blog.pojo.NetFood
import cn.edu.food_blog.pojo.NetFoodDesc
import cn.edu.food_blog.pojo.Response
import cn.edu.food_blog.util.NetApiCreator
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_food_list.*
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Retrofit

/**
 * 网络食物列表
 */
class FoodListActivity : AppCompatActivity() {

    //每页加载十一个条目
    private var start = 0
    private val num = 10
    private val netFoodDescList = ArrayList<NetFoodDesc>()
    private val netFoodList = ArrayList<NetFood>()
    private lateinit var adapter: NetFoodDescAdapter
    private lateinit var layoutManager: RecyclerView.LayoutManager
    private lateinit var netFoodApi: NetFoodApi
    //刷新列表的颜色
    private val refresherColor = Color.parseColor("#FFEB3B")
    private var classId: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_food_list)

        setSupportActionBar(toolBarRemoteFoodList)
        supportActionBar?.let {
            it.setDisplayHomeAsUpEnabled(true)
        }

        netFoodApi = NetApiCreator.create(NetFoodApi::class.java)
        adapter = NetFoodDescAdapter(this, netFoodDescList)
        layoutManager = LinearLayoutManager(this)
        foodRecView.adapter = adapter
        foodRecView.layoutManager = layoutManager

        //设置点击监听,点击跳转到食物详情页面
        adapter.setOnClickListener { pos ->
            val intent = Intent(this, FoodDetailActivity::class.java).apply {
                putExtra("foodImg", netFoodDescList[pos].pic)
                putExtra("foodName", netFoodList[pos].name)
                putExtra("foodContent", netFoodList[pos].content)
                Gson().apply {
                    putExtra("foodMaterial", toJson(netFoodList[pos].material))
                    putExtra("foodProcess", toJson(netFoodList[pos].process))
                }
            }
            startActivity(intent)
        }

        //刷新
        refresher.setColorSchemeColors(refresherColor)
        refresher.isRefreshing = true

        //关闭下拉刷新，滑到底部自动加载
        refresher.setOnRefreshListener {
            refresher.isRefreshing = false
        }
        //当已经加载了十一条数据后，需要刷新，获取新的食物菜单
        foodRecView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (newState == RecyclerView.SCROLL_STATE_IDLE && isBottom(recyclerView)) {
                    refresher.isRefreshing = true
                    fetchFoodList()
                }
            }
            fun isBottom(recyclerView: RecyclerView): Boolean {
                if (recyclerView == null)
                    return false
                if (recyclerView.computeVerticalScrollExtent() + recyclerView.computeVerticalScrollOffset() >= recyclerView.computeVerticalScrollRange())
                    return true
                return false
            }
        })

        //获取前面选择的分类
        intent.getStringExtra("classname")?.let {
            foodClass.text = it
        }
        intent.getStringExtra("classid")?.let {
            classId = it
            //请求分类下的网络食物
            fetchFoodList()
        }

    }

    //功能键，点击返回时
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> finish()
        }
        return true
    }

    //异步加载食物信息
    fun fetchFoodList() {
        classId?.let {
            netFoodApi.getFoodList(it, "$start", "$num").enqueue(object : Callback<Response> {
                override fun onFailure(call: Call<Response>, t: Throwable) {
                    t.printStackTrace()
                }
                override fun onResponse(
                    call: Call<Response>,
                    response: retrofit2.Response<Response>
                ) {
                    response.body()?.let {
                        if (it.status == 0 && it.msg.equals("ok")) {
                            netFoodList.addAll(it.result.list)
                            start += num
                            for (fi in it.result.list) {
                                val name: String
                                val content: String
                                val tag: String
                                //显示三级菜单
                                fi.name.apply { name = if (length > 7) substring(0, 7) + "..." else this }
                                fi.content.apply { content = if (length > 10) substring(0, 10) + "..." else this }
                                //设置最多显示了三个标签
                                fi.tag.split(",").apply { tag = if (size > 3) subList(0, 3).joinToString() else joinToString()}
                                val netFoodDesc = NetFoodDesc(name, content, tag, fi.peoplenum)
                                fetchFoodImage(netFoodDesc, fi.pic)
                                netFoodDescList.add(netFoodDesc)
                            }
                            adapter.notifyDataSetChanged()
                            //取消刷新
                            refresher.isRefreshing = false
                        }
                    }
                }
            })
        }
    }

    //异步请求食物图片
    fun fetchFoodImage(foodDesc: NetFoodDesc, pic: String) {
        val uri = pic.split("/").run {
            get(size-2) + "/" + get(size-1)
        }
        val netFoodApi = NetApiCreator.create(NetFoodApi::class.java)
        netFoodApi.getFoodImage(uri).enqueue(object : Callback<ResponseBody> {
            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                t.printStackTrace()
            }
            override fun onResponse(
                call: Call<ResponseBody>,
                response: retrofit2.Response<ResponseBody>
            ) {
                response.body()?.let {
                    val bm = BitmapFactory.decodeStream(it.byteStream())
                    foodDesc.pic = bm
                    adapter.notifyDataSetChanged()
                }
            }
        })
    }

}