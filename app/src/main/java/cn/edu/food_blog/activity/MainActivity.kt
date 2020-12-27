package cn.edu.food_blog.activity

import android.content.res.Configuration
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import cn.edu.food_blog.R
import cn.edu.food_blog.fragment.FoodBlogsFragment
import cn.edu.food_blog.fragment.FoodNavFragment
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    //食物博客
    private val foodBlogsFragment = FoodBlogsFragment()
    //食物菜单
    private val foodNavFragment = FoodNavFragment()
    //食物收藏

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        //设置标题
        setSupportActionBar(toolBarMain)
        //加载食物博客
        supportFragmentManager.beginTransaction()
            .replace(R.id.frame,foodBlogsFragment)
            .commit()

        bottom.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.home -> {
                    supportFragmentManager.beginTransaction()
                            .replace(R.id.frame, foodBlogsFragment)
                            .commit()
                }
                R.id.nav -> {
                    supportFragmentManager.beginTransaction()
                            .replace(R.id.frame, foodNavFragment)
                            .commit()
                }
            }
            true
        }

    }

    //坏掉的小灯泡
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.action, menu)
        return true
    }
    //灯泡功能
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_theme -> {
                // Get new mode.
                val mode =
                    if ((resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK) ==
                        Configuration.UI_MODE_NIGHT_NO
                    ) {
                        AppCompatDelegate.MODE_NIGHT_YES
                    } else {
                        AppCompatDelegate.MODE_NIGHT_AUTO_BATTERY
                    }

                // Change UI Mode
                AppCompatDelegate.setDefaultNightMode(mode)
                true
            }
            else -> true
        }
    }

    //设置界面的标题
    fun setToolBarTitle(title: String) = toolBarMain.setTitle(title)

}