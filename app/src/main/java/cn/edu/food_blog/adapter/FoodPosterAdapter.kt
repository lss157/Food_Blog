package cn.edu.food_blog.adapter

import android.content.Intent
import android.net.Uri
import android.view.*
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import cn.edu.food_blog.R
import cn.edu.food_blog.activity.FoodBlogActivity
import cn.edu.food_blog.pojo.FoodPoster
import com.google.gson.Gson

class FoodPosterAdapter(val fragment: Fragment, var foodPosterList: List<FoodPoster>) :
    RecyclerView.Adapter<FoodPosterAdapter.ViewHolder>() {
    //传入一个闭包，外面编写处理逻辑传入adapter内部
    private var cmListener: ((Int) -> Unit)? = null //长按监听
    fun setOnCreateContextListener(listener: (Int) -> Unit) {
        cmListener = listener
    }

    inner class ViewHolder(val view: View) : RecyclerView.ViewHolder(view){
        val foodTitle: TextView = view.findViewById(R.id.foodTitle)
        val foodText: TextView = view.findViewById(R.id.foodText)
        val foodPosterImage: ImageView = view.findViewById(R.id.foodPosterImage)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_food_poster, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount() = foodPosterList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.apply {
            foodPosterList[position].foodTitle.run {
                foodTitle.text = if (length > 6) substring(0, 6) + "..." else this
            }
            foodPosterList[position].foodText.run {
                foodText.text = if (length > 25) substring(0, 25) + "..." else this
            }
            val uriString = foodPosterList[position].foodImage
            if (!uriString.equals("")) foodPosterImage.setImageURI(Uri.parse(uriString))
            else foodPosterImage.setImageResource(R.drawable.food_default)
            //列表全局长按，记录长按位置
            itemView.setOnLongClickListener {
                cmListener?.invoke(position)
                false
            }
            //列表全局点击事件，跳转到美食详情页
            itemView.setOnClickListener {
                val intent = Intent(fragment.activity, FoodBlogActivity::class.java).apply {
                    putExtra("id", "${foodPosterList[position].id}")
                    putExtra("position", position)
                    putExtra("foodPosterList", Gson().toJson(foodPosterList))
                }
                fragment.startActivityForResult(intent, 2)
            }
        }
    }

}