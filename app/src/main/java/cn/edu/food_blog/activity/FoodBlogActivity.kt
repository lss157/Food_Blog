package cn.edu.food_blog.activity

import android.app.Activity
import android.content.ContentValues
import android.content.Intent
import android.database.sqlite.SQLiteDatabase
import android.net.Uri
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import cn.edu.food_blog.R
import cn.edu.food_blog.dao.FoodBlogDao
import cn.edu.food_blog.pojo.FoodPoster
import cn.edu.food_blog.util.ImageIO
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.android.synthetic.main.activity_food_blog.*

//查看与编辑食物记录
class FoodBlogActivity : AppCompatActivity() {

    //查看模式 —— true 只读、false 可写
    private var mode = true
    private lateinit var db: SQLiteDatabase
    private lateinit var id: String //博客id
    private lateinit var foodBlog: FoodPoster
    private lateinit var foodImageUri: String
    private lateinit var foodPosterList: ArrayList<FoodPoster>
    private var pos = -1 //博客所在博客列表的位置

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_food_blog)

        //导入设置好的博客的展示样式
        setSupportActionBar(infoFoodToolbar)
        supportActionBar?.apply {
            setHomeAsUpIndicator(R.drawable.ic_left)
            setDisplayHomeAsUpEnabled(true)
        }

        //获取所在位置
        pos = intent.getIntExtra("position", -1)

        //解析博客列表
        val fplJson = intent.getStringExtra("foodPosterList")!!
        val fplType = object : TypeToken<List<FoodPoster>>() {}.type
        foodPosterList = Gson().fromJson(fplJson, fplType)

        //加载博客数据
        id = intent.getStringExtra("id")!!
        db = FoodBlogDao(this, 1).writableDatabase
        db.query(FoodBlogDao.FOOD_BLOG, null, "id = ?", arrayOf(id), null, null, null).run {
            if (moveToFirst()) {
                foodBlog = FoodPoster(id.toInt(),
                        getString(getColumnIndex("food_title")),
                        getString(getColumnIndex("food_text")),
                        getString(getColumnIndex("food_image")))
                foodImageUri = foodBlog.foodImage //初始化图片地址的可变参数
                foodBlog.foodTitle.run {
                    infoFoodCollapseBar.title = this
                }
                infoFoodText.setText(foodBlog.foodText)
                if (!foodBlog.foodImage.equals("")) infoFoodImage.setImageURI(Uri.parse(foodBlog.foodImage))
                else infoFoodImage.setBackgroundResource(R.drawable.add_icon)
            }
        }

        //查看与编辑的模式切换
        infoFoodMode.setOnClickListener { it as FloatingActionButton
            mode = !mode
            when (mode) {
                true -> {
                    it.setImageResource(R.drawable.ic_ro)
                    //readonly不可更改内容，并保存
//                    infoFoodTitle.isEnabled = false
                    infoFoodText.isEnabled = false
                    saveBlog()
                }
                else -> {
                    it.setImageResource(R.drawable.ic_rw)
//                    infoFoodTitle.isEnabled = true
                    infoFoodText.isEnabled = true
                }
            }
        }

        //图片更改
        infoFoodImage.setOnClickListener {
            if (!mode) {
                val intent = Intent(Intent.ACTION_OPEN_DOCUMENT)
                intent.addCategory(Intent.CATEGORY_OPENABLE)
                intent.setType("image/*")
                startActivityForResult(intent, 1)
            }
        }

    }
    //退出返回数据
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> returnData()
        }
        return true
    }
    //按退出键返回数据
    override fun onBackPressed() = returnData()

    //获取图片选择的响应
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            1 -> if (resultCode == Activity.RESULT_OK) {
                data?.data?.let {
                    val bitmap = ImageIO.getBitmapFromUri(this, it)
                    foodImageUri = it.toString()
                    infoFoodImage.setImageBitmap(bitmap)
                }
            }
        }
    }

    //保存实时修改的内容
    private fun saveBlog() {
//        foodBlog.foodTitle = infoFoodTitle.text.toString()
        foodBlog.foodText = infoFoodText.text.toString()
        foodBlog.foodImage = foodImageUri
        foodPosterList[pos] = foodBlog
        db.update(FoodBlogDao.FOOD_BLOG, ContentValues().apply {
            put("food_title", foodBlog.foodTitle)
            put("food_text", foodBlog.foodText)
            put("food_image", foodBlog.foodImage)
        }, "id = ?", arrayOf(id))
        Toast.makeText(this, "博客保存成功", Toast.LENGTH_SHORT).show()
    }
    //把更改的数据返回给开始的列表
    private fun returnData() {
        val intent = Intent()
        intent.putExtra("foodPosterList", Gson().toJson(foodPosterList))
        setResult(Activity.RESULT_OK, intent)
        finish()
    }

}