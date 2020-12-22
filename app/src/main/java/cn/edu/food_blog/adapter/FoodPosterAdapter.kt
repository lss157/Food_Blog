package cn.edu.food_blog.adapter

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.os.Build
import android.util.Log
import android.view.*
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import cn.edu.food_blog.R
import cn.edu.food_blog.dao.FoodBlogDao
import cn.edu.food_blog.pojo.FoodPoster
import cn.edu.food_blog.util.ImageIO
import kotlinx.android.synthetic.main.item_food_poster.view.*

class FoodPosterAdapter(val context: Context, var foodPosterList: List<FoodPoster>) :
    RecyclerView.Adapter<FoodPosterAdapter.ViewHolder>() {

    private var cmListener: ((Int) -> Unit)? = null
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
            foodTitle.text = foodPosterList[position].foodTitle
            foodText.text = foodPosterList[position].foodText
            val uriString = foodPosterList[position].foodImage
            if (!uriString.equals("")) foodPosterImage.setImageURI(Uri.parse(uriString))
            else foodPosterImage.setImageResource(R.drawable.food_default)
            //记录长按位置
            itemView.setOnLongClickListener {
                cmListener?.invoke(position)
                false
            }
        }
    }

}