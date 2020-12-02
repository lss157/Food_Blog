package cn.edu.food_blog

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import cn.edu.food_blog.fragment.FoodListFragment
import cn.edu.food_blog.fragment.ShowContextFragment
import cn.edu.food_blog.fragment.UpdateFragment
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private val foodListFragment = FoodListFragment()
    private val showContextFragment = ShowContextFragment()
    private val updateFragment = UpdateFragment()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportFragmentManager.beginTransaction()
            .replace(R.id.frame,foodListFragment)
            .commit()
        bottom.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.list -> supportFragmentManager.beginTransaction()
                    .replace(R.id.frame, foodListFragment)
                    .commit()
                R.id.showText -> supportFragmentManager.beginTransaction()
                    .replace(R.id.frame, showContextFragment)
                    .commit()
                R.id.update -> supportFragmentManager.beginTransaction()
                    .replace(R.id.frame,updateFragment)
                    .commit()
            }
            true
        }
    }
}