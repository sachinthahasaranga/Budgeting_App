package com.example.budgetingapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.content.Intent
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.budgetingapp.Adapters.ExpensesAdapter
import com.example.budgetingapp.DataClasses.Expense
import com.example.budgetingapp.databinding.ActivityExpenseTrackerBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class ExpenseTracker : AppCompatActivity() {
    private lateinit var binding: ActivityExpenseTrackerBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var databaseRef: DatabaseReference
    private lateinit var uid: String
    private lateinit var recyclerView: RecyclerView
    private var mList = ArrayList<Expense>()
    private lateinit var adapter: ExpensesAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityExpenseTrackerBinding.inflate(layoutInflater)
        setContentView(binding.root)


        //initialize variables
        auth = FirebaseAuth.getInstance()
        uid = auth.currentUser?.uid.toString()
        //database
        databaseRef = FirebaseDatabase.getInstance().reference.child("expenses").child(uid)

        recyclerView = binding.recyclerView
        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = LinearLayoutManager(this);
//recive
        databaseRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                mList.clear()
                var totalAmount = 0.0
                for ( snapshot in snapshot.children){
                    val data = snapshot.getValue(Expense::class.java)!!
                    if( data != null){
                        mList.add(data)
                        var amount = data.amt!!.toDouble()
                        totalAmount += amount
                    }
                }
                adapter.notifyDataSetChanged()
                binding.tvTotSpendings.text = totalAmount.toString()
                //Toast.makeText(this@ExpenseTracker, totalAmount.toString(), Toast.LENGTH_SHORT).show(
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@ExpenseTracker, error.message, Toast.LENGTH_SHORT).show()
            }
        })

        adapter = ExpensesAdapter(this, mList)
        recyclerView.adapter = adapter

        //add a expense
        binding.btnSubmit.setOnClickListener {
            addExpense()
        }


        //Setting onclick on recyclerView each item
        adapter.setOnItemClickListner(object: ExpensesAdapter.onItemClickListner {
            override fun onItemClick(position: Int) {

            }
        })
    }


    private fun addExpense() {
        var amt = binding.etAmount.text.toString()
        var type = binding.etType.text.toString()

        if (amt.isEmpty() || type.isEmpty()) {

            if (amt.isEmpty()) {
                binding.etAmount.error = "Enter amount"
            }
            if (type.isEmpty()) {
                binding.etType.error = "Enter type"
            }
        } else {
            //Id for new record
            var id = databaseRef.push().key!!
            //create a object
            val data = Expense(id, amt, type, uid)
            databaseRef.child(id).setValue(data).addOnCompleteListener {
                if (it.isSuccessful) {
                    //make fields empty
                    binding.etAmount.setText("")
                    binding.etType.setText("")
                    Toast.makeText(this, "Expense added", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, it.exception?.message, Toast.LENGTH_SHORT).show()
                }
            }

        }
    }
}