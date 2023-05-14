package com.example.budgetingapp.Adapters


import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.budgetingapp.DataClasses.CategoryModel
import com.example.budgetingapp.R
import com.example.budgetingapp.ViewACategory

class AllCategoriesAdapter(var context: Context, var mList: List<CategoryModel>) :
    RecyclerView.Adapter<AllCategoriesAdapter.ViewHolder>() {

    private lateinit var mListner : onItemClickListner

    //Setting up onClick listner interface
    interface onItemClickListner{
        fun onItemClick( position: Int)
    }

    fun setOnItemClickListner(listner: onItemClickListner){
        mListner = listner
    }

    inner class ViewHolder(itemView: View, listner: onItemClickListner) : RecyclerView.ViewHolder(itemView) {
        val tvTitle : TextView = itemView.findViewById(R.id.tvTitle)
        val btnViewDetails : Button = itemView.findViewById(R.id.btnViewDetails)
        val btnDlt : Button = itemView.findViewById(R.id.btnDlt)


        init{
            itemView.setOnClickListener {
                listner.onItemClick(adapterPosition)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.each_item_category, parent, false)
        return ViewHolder(view, mListner)
    }

    override fun getItemCount(): Int {
        return mList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.tvTitle.text = mList[position].name
        holder.btnViewDetails.setOnClickListener {
            val intent1 = Intent(context, ViewACategory::class.java).also {
                it.putExtra("name", mList[position].name)
                it.putExtra("id", mList[position].id)
                context.startActivity(it)
            }
        }
    }
}