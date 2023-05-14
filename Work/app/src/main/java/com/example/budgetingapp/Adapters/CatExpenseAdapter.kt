package com.example.budgetingapp.Adapters


import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.budgetingapp.DataClasses.Article
import com.example.budgetingapp.DataClasses.CatExpenseModel
import com.example.budgetingapp.DataClasses.Expense
import com.example.budgetingapp.ExpenseTracker
import com.example.budgetingapp.R
import com.example.budgetingapp.UpdateCatExpense
import com.example.budgetingapp.UpdateExpense
import com.google.firebase.database.FirebaseDatabase

class CatExpenseAdapter(var context: Context, var mList: List<CatExpenseModel>) :
    RecyclerView.Adapter<CatExpenseAdapter.ViewHolder>() {

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
        val tvDes : TextView = itemView.findViewById(R.id.tvAmt)
        val btnEdit : ImageView = itemView.findViewById(R.id.btnEdit)
        val btnDlt : ImageView = itemView.findViewById(R.id.btnDlt)

        init{
            itemView.setOnClickListener {
                listner.onItemClick(adapterPosition)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.each_item_expenses, parent, false)
        return ViewHolder(view, mListner)
    }

    override fun getItemCount(): Int {
        return mList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.tvTitle.text = mList[position].name
        holder.tvDes.text = mList[position].amt

        holder.btnEdit.setOnClickListener {
            val intent1 = Intent(context, UpdateCatExpense::class.java).also {
                it.putExtra("name", mList[position].name)
                it.putExtra("amt", mList[position].amt)
                it.putExtra("exId", mList[position].exId)
                it.putExtra("catId", mList[position].catId)
                it.putExtra("catName", mList[position].catName)
            }
            context.startActivity(intent1)
        }

        holder.btnDlt.setOnClickListener {
            var uid = mList[position].uid
            var exId = mList[position].exId
            var catId = mList[position].catId
            var databaseRef = FirebaseDatabase.getInstance().reference.child("CategoryExpenses").child(uid!!).child(catId!!).child(exId!!)
            databaseRef.removeValue().addOnCompleteListener {
                if( it.isSuccessful){
                    Toast.makeText(context, "Item deleted", Toast.LENGTH_SHORT).show()
                }
            }
        }


    }
}