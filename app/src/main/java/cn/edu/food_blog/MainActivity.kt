package cn.edu.food_blog

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        bottom.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.update -> {
                    val intent = Intent(this, UpdateActivity::class.java)
                    startActivity(intent)
                }
            }
            true
        }
    }
}