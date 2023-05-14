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
import com.example.budgetingapp.DataClasses.Expense
import com.example.budgetingapp.ExpenseTracker
import com.example.budgetingapp.R
import com.example.budgetingapp.UpdateExpense
import com.google.firebase.database.FirebaseDatabase

class ExpensesAdapter(var context: Context, var mList: List<Expense>) :
    RecyclerView.Adapter<ExpensesAdapter.ViewHolder>() {

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
        holder.tvTitle.text = mList[position].amt
        holder.tvDes.text = mList[position].type
//updt
        holder.btnEdit.setOnClickListener {
            val intent1 = Intent(context, UpdateExpense::class.java).also {
                it.putExtra("type", mList[position].type)
                it.putExtra("amt", mList[position].amt)
                it.putExtra("id", mList[position].id)
            }
            context.startActivity(intent1)
        }
//del
        holder.btnDlt.setOnClickListener {
            var uid = mList[position].uid
            var id = mList[position].id
            var databaseRef = FirebaseDatabase.getInstance().reference.child("expenses").child(uid!!).child(id!!)
            databaseRef.removeValue().addOnCompleteListener {
                if( it.isSuccessful){
                    Toast.makeText(context, "Item deleted", Toast.LENGTH_SHORT).show()
                }
            }
        }


    }
}