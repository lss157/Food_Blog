package cn.edu.food_blog.fragment.vm

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import cn.edu.food_blog.adapter.FoodPosterAdapter
import cn.edu.food_blog.dao.FoodBlogDao
import cn.edu.food_blog.pojo.FoodPoster

class FoodBlogsViewModel(val fragment: Fragment) : ViewModel() {
    //获得对象的Field属性
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
        foodPosterAdapter = FoodPosterAdapter(fragment, foodPosterList)
        db = FoodBlogDao(fragment.activity!!, 1).writableDatabase
        pos = 0
        layoutManager = LinearLayoutManager(fragment.activity)
    }

}

//view model传参的工厂方法
class FoodBlogsViewModelFactory(val fragment: Fragment)
    : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return FoodBlogsViewModel(fragment) as T
    }

}