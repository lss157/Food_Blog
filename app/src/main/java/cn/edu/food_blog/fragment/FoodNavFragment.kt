package cn.edu.food_blog.fragment

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import cn.edu.food_blog.R
import cn.edu.food_blog.activity.FoodListActivity
import cn.edu.food_blog.activity.MainActivity
import cn.edu.food_blog.pojo.FoodFirstClass
import cn.edu.food_blog.pojo.FoodSecondClass
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.android.synthetic.main.fragment_food_nav.*
import java.io.BufferedReader
import java.io.InputStreamReader

class FoodNavFragment : Fragment() {
    //第一级菜单
    private lateinit var firstClassList: ArrayList<FoodFirstClass>
    //第二级菜单
    private val secondClassList = ArrayList<FoodSecondClass>()
    private lateinit var adapter: ArrayAdapter<FoodSecondClass>
    //第一级菜单的视图
    private lateinit var btnList: ArrayList<TextView>
    private var btnPos = 0

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        return inflater.inflate(R.layout.fragment_food_nav, container, false)
    }

    //重新进入界面后已选择位置的保存
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val ac = activity as MainActivity
        ac.setToolBarTitle("导航")
        //加载数据
        //一级菜单和二级菜单
        firstClassList = getRaws(R.raw.food_classes)
        //先开始显示第一级菜单的第一个条目的第二级菜单
        secondClassList.addAll(firstClassList[0].list)
        adapter = ArrayAdapter(activity!!, android.R.layout.simple_list_item_1, secondClassList)
        rightBar.adapter = adapter
        //第一个菜单被选中就设置背景颜色为白色
        btnCaixi.setBackgroundColor(Color.WHITE)
        //不同按钮点击加载不同的选项菜单
        btnList = ArrayList<TextView>().apply {
            add(btnCaixi)
            add(btnXiaochi)
            add(btnCaipin)
            add(btnKouwei)
            add(btnGongyi)
            add(btnChangjing)
        }
        //一直监听一级菜单的哪个条目被按下
        for (pos in 0 until btnList.size) {
            btnList[pos].setOnClickListener {
                //要把前一次的清除
                secondClassList.clear()
                secondClassList.addAll(firstClassList[pos].list)
                adapter.notifyDataSetChanged()
                //把没有选中的一级菜单的条目设置为灰色
                for (idx in 0 until btnList.size) {
                    if (idx != pos) btnList[idx].setBackgroundColor(Color.parseColor("#EFEFEF"))
                    else btnList[idx].setBackgroundColor(Color.WHITE)
                }
            }
        }
        //注册类别点击事件
        rightBar.setOnItemClickListener { parent, view, position, id ->
            val intent = Intent(activity, FoodListActivity::class.java)
            intent.putExtra("classid", secondClassList[position].classid.toString())
            intent.putExtra("classname", secondClassList[position].name)
            startActivity(intent)
        }
    }

    //读取二级文件
    fun getRaws(resId: Int): ArrayList<FoodFirstClass> {
        val br = BufferedReader(InputStreamReader(resources.openRawResource(resId)))
        val sb = StringBuffer()
        br.forEachLine { sb.append(it) }
        val rawString = sb.toString()
        val type = object : TypeToken<ArrayList<FoodFirstClass>>(){}.type
        return Gson().fromJson(rawString, type)
    }

}