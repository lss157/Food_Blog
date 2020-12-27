package cn.edu.food_blog.fragment

import android.app.Activity
import android.app.AlertDialog
import android.content.ContentValues
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import cn.edu.food_blog.R
import cn.edu.food_blog.activity.FoodBlogAdditionActivity
import cn.edu.food_blog.activity.MainActivity
import cn.edu.food_blog.adapter.FoodPosterAdapter
import cn.edu.food_blog.dao.FoodBlogDao
import cn.edu.food_blog.fragment.vm.FoodBlogsViewModel
import cn.edu.food_blog.fragment.vm.FoodBlogsViewModelFactory
import cn.edu.food_blog.pojo.FoodPoster
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.android.synthetic.main.fragment_food_blogs.*


class FoodBlogsFragment : Fragment() {


    private val vm by lazy {
        ViewModelProvider(activity!!, FoodBlogsViewModelFactory(this))
            .get(FoodBlogsViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_food_blogs, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        var ac = activity as MainActivity
        ac.setToolBarTitle("食记")
        //获取数据库连接
        vm.db = FoodBlogDao(activity!!, 1).writableDatabase

        //查询所有在数据库的blog数据
        if (vm.foodPosterList.isEmpty()) {
            val cursor = vm.db.query(FoodBlogDao.FOOD_BLOG, null, null, null, null, null, null)
            while (cursor.moveToNext()) {
                val id = cursor.getInt(cursor.getColumnIndex("id"))
                val foodTitle = cursor.getString(cursor.getColumnIndex("food_title"))
                val foodText = cursor.getString(cursor.getColumnIndex("food_text"))
                val foodImage = cursor.getString(cursor.getColumnIndex("food_image"))
                val foodPoster = FoodPoster(id, foodTitle, foodText, foodImage)
                vm.foodPosterList.add(foodPoster)
            }
        }
        //注入recyclerview
        vm.foodPosterAdapter.setOnCreateContextListener { position -> vm.pos = position }
        vm.layoutManager = LinearLayoutManager(activity)
        foodPosterRecyclerView.adapter = vm.foodPosterAdapter
        foodPosterRecyclerView.layoutManager = vm.layoutManager
        //注册context menu
        registerForContextMenu(foodPosterRecyclerView)
        //添加博客按钮被点击
        addBlogBtn.setOnClickListener {
            val intent = Intent(activity, FoodBlogAdditionActivity::class.java)
            startActivityForResult(intent, 1)
        }
    }

    //插入博客的返回
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when (requestCode) {
            //解析并添加到博客列表
            1 -> if (resultCode == Activity.RESULT_OK) {
                data?.getStringExtra("food_poster")?.let {
                    val posterType = object : TypeToken<FoodPoster>(){}.type
                    val foodPoster = Gson().fromJson<FoodPoster>(it, posterType)
                    //插入数据并获取自增键id
                    var foodId = vm.db.insert(FoodBlogDao.FOOD_BLOG, null, ContentValues().apply {
                        put("food_title", foodPoster.foodTitle)
                        put("food_text", foodPoster.foodText)
                        put("food_image", foodPoster.foodImage)
                    })
                    foodPoster.id = foodId.toInt()
                    vm.foodPosterList.add(foodPoster)
                    vm.foodPosterAdapter.notifyDataSetChanged()
                    Toast.makeText(activity, "博客插入成功", Toast.LENGTH_SHORT).show()
                }
            }
            //解析并修改博客列表
            2 -> {
                if (resultCode == Activity.RESULT_OK) {
                    //数据更新
                    data?.getStringExtra("foodPosterList")?.let {
                        val fplType = object : TypeToken<List<FoodPoster>>() {}.type
                        vm.foodPosterList.run {
                            clear()
                            addAll(Gson().fromJson(it, fplType))
                        }
                        vm.foodPosterAdapter.notifyDataSetChanged()
                    }
                }
            }
        }
    }

    //长按列表项
    override fun onCreateContextMenu(
        menu: ContextMenu,
        v: View,
        menuInfo: ContextMenu.ContextMenuInfo?
    ) {
        super.onCreateContextMenu(menu, v, menuInfo)
        activity!!.menuInflater.inflate(R.menu.blog_item_menu, menu)
    }

    //博客操作选项被选中
    override fun onContextItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.optionDelete -> {
                //插入自定义警告框
                val v = LayoutInflater.from(activity)
                        .inflate(R.layout.alert_delete_blog, null, false)
                val alertDialog = AlertDialog.Builder(activity)
                        .setView(v)
                        .create()
                val btnPositive: Button = v.findViewById(R.id.btnPositive)
                val btnNegative: Button = v.findViewById(R.id.btnNegative)
                btnPositive.setOnClickListener {
                    //列表中移除食物海报数据
                    val id = vm.foodPosterList.removeAt(vm.pos).id
                    //数据库中移除食物海报数据
                    vm.db.delete(FoodBlogDao.FOOD_BLOG,
                        "id = ?",
                        arrayOf(id.toString()))
                    //通知删除
                    vm.foodPosterAdapter.notifyDataSetChanged()
                    Toast.makeText(activity, "博客删除成功", Toast.LENGTH_SHORT).show()
                    alertDialog.dismiss()
                }
                btnNegative.setOnClickListener {
                    //关闭界面
                    alertDialog.dismiss()
                }
                alertDialog.show()
            }

        }
        return true
    }

}