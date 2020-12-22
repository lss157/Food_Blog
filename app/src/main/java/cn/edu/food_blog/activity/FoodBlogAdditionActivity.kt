package cn.edu.food_blog.activity

import android.app.Activity
import android.content.Intent
import android.database.sqlite.SQLiteDatabase
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import cn.edu.food_blog.R
import cn.edu.food_blog.dao.FoodBlogDao
import cn.edu.food_blog.pojo.FoodPoster
import cn.edu.food_blog.util.ImageIO
import kotlinx.android.synthetic.main.activity_add_food_blog.*

class FoodBlogAdditionActivity : AppCompatActivity() {

    private var foodImageUri: String = ""
    private lateinit var dbHelper: FoodBlogDao
    private lateinit var db: SQLiteDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_food_blog)

        setSupportActionBar(toolBarAddBlog)
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowHomeEnabled(true)
        }

        imageFood.setOnClickListener {
            val intent = Intent(Intent.ACTION_OPEN_DOCUMENT)
            intent.addCategory(Intent.CATEGORY_OPENABLE)
            intent.setType("image/*")
            startActivityForResult(intent, 1)
        }

        btn_publish.setOnClickListener {
            dbHelper = FoodBlogDao(this, 1)
            db = dbHelper.writableDatabase
            val foodPoster = FoodPoster(null, edit_title.text.toString(), edit_text.text.toString(), foodImageUri)
            //intent 返回数据
            val intent = Intent()
            intent.putExtra("food_poster", foodPoster.toString())
            setResult(Activity.RESULT_OK, intent)
            finish()
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> finish()
        }
        return true
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            1 -> if (resultCode == Activity.RESULT_OK) {
                data?.data?.let {
                    val bitmap = ImageIO.getBitmapFromUri(this, it)
                    foodImageUri = it.toString()
                    imageFood.setImageBitmap(bitmap)
                }
            }
        }
    }

}