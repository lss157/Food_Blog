package cn.edu.food_blog.fragment.viewmodel

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import cn.edu.food_blog.activity.MainActivity
import cn.edu.food_blog.adapter.FoodPosterAdapter
import cn.edu.food_blog.dao.FoodBlogDao
import cn.edu.food_blog.fragment.FoodBlogsFragment
import cn.edu.food_blog.pojo.FoodPoster

class FoodBlogsViewModel(val context: Context) : ViewModel() {

    var foodPosterList: ArrayList<FoodPoster>
        get() = field

    var foodPosterAdapter: FoodPosterAdapter
        get() = field

    var db: SQLiteDatabase
        get() = field

    var pos: Int
        get() = field

    var layoutManager: RecyclerView.LayoutManager
        get() = field

    init {
        foodPosterList = ArrayList()
        foodPosterAdapter = FoodPosterAdapter(context, foodPosterList)
        db = FoodBlogDao(context, 1).writableDatabase
        pos = 0
        layoutManager = LinearLayoutManager(context)
    }

}

//view model传参的工厂方法
class FoodBlogsViewModelFactory(val context: Context)
    : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return FoodBlogsViewModel(context) as T
    }

}