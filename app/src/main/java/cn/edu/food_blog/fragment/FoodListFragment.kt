package cn.edu.food_blog.fragment

import android.database.Cursor
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import cn.edu.food_blog.R



class FoodListFragment : Fragment() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_food_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }

}

class MyRecyclerViewAdapter(var cursor: Cursor): RecyclerView.Adapter<MyRecyclerViewAdapter.ViewHolder>() {
    //更新了list
    fun swapCursor(newCursor: Cursor){
        if (cursor == newCursor) return
        cursor.close()
        cursor = newCursor
        notifyDataSetChanged()
    }

    class ViewHolder(view: View): RecyclerView.ViewHolder(view) {
        val textView_title: TextView
        val textView_text: TextView
        val image: ImageView
        init {
            textView_title = view.findViewById(R.id.textView_title)
            textView_text = view.findViewById(R.id.textView_text)
            image = view.findViewById(R.id.image)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.food_layout,parent,false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        cursor.moveToPosition(position)
        holder.textView_title.text = cursor.getString(cursor.getColumnIndex("title"))
        holder.textView_text.text = cursor.getString(cursor.getColumnIndex("context"))
        holder.image.setImageResource(img)
    }

    override fun getItemCount(): Int {
        return cursor.count
    }
}