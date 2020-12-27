package cn.edu.food_blog.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import cn.edu.food_blog.R
import cn.edu.food_blog.pojo.Process

class NetFoodProcessAdapter(val foodProcessList: List<Process>)
    : RecyclerView.Adapter<NetFoodProcessAdapter.ViewHolder>() {

    inner class ViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        val stepDesc: TextView = v.findViewById(R.id.stepDesc)
        val stepImage: ImageView = v.findViewById(R.id.stepImage)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_food_process, parent, false)
        return ViewHolder(v)
    }

    override fun getItemCount() = foodProcessList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.apply {
            //每一步的处理过程和图片
            //从0开始计数，所以步骤得加一
            stepDesc.text = "${position + 1}. ${foodProcessList[position].pcontent}"
            stepImage.setImageBitmap(foodProcessList[position].picImg)
        }
    }

}