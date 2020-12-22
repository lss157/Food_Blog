package cn.edu.food_blog.dao

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.widget.Toast

class FoodBlogDao(val context: Context, val version: Int)
    : SQLiteOpenHelper(context, DB_NAME, null, version) {

    companion object {
        const val DB_NAME = "food"
        const val FOOD_BLOG = "food_blog"
        const val INIT_SQL = "CREATE TABLE " + FOOD_BLOG + " (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "food_title VARCHAR(20) NOT NULL," +
                "food_text VARCHAR(1000)," +
                "food_image VARCHAR(200)" +
                ")"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL(INIT_SQL)
        Toast.makeText(context, "数据库初始化成功!", Toast.LENGTH_SHORT).show()
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {

    }

}