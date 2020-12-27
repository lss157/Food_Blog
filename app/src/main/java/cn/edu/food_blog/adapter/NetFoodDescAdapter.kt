package cn.edu.food_blog.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import cn.edu.food_blog.R
import cn.edu.food_blog.pojo.NetFoodDesc

/**
 * 网络请求到的食物数据适配器
 */
class NetFoodDescAdapter(val context: Context, val netFoodDescList: List<NetFoodDesc>)
    : RecyclerView.Adapter<NetFoodDescAdapter.ViewHolder>() {

    private var cListener: ((Int) -> Unit)? = null
    fun setOnClickListener(listener: (Int) -> Unit) {
        cListener = listener
    }
    //前面展示adapter
    inner class ViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        val netFoodImage: ImageView = v.findViewById(R.id.netFoodImage)
        val netFoodTitle: TextView = v.findViewById(R.id.netFoodTitle)
        val netFoodContent: TextView = v.findViewById(R.id.netFoodContent)
        val netFoodTag: TextView = v.findViewById(R.id.netFoodTag)
        val netFoodPeopleNum: TextView = v.findViewById(R.id.netFoodPeopleNum)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_remote_food_desc, parent, false)
        return ViewHolder(v)
    }

    override fun getItemCount() = netFoodDescList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.apply {
            netFoodDescList[position].apply {
                netFoodImage.setImageBitmap(pic)
                netFoodTitle.text = name
                netFoodContent.text = content
                netFoodTag.text = tag
                netFoodPeopleNum.text = peoplenum
            }
            //跳转到食物详情界面
            itemView.setOnClickListener {
                cListener?.invoke(position)
            }
        }
    }

}