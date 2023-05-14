package com.example.budgetingapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.content.Intent
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.budgetingapp.Adapters.CatExpenseAdapter
import com.example.budgetingapp.DataClasses.CatExpenseModel
import com.example.budgetingapp.DataClasses.CategoryModel
import com.example.budgetingapp.DataClasses.Expense
import com.example.budgetingapp.databinding.ActivityViewAcategoryBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class ViewACategory : AppCompatActivity() {
    private lateinit var binding: ActivityViewAcategoryBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var databaseRef: DatabaseReference
    private lateinit var databaseRefAddCat: DatabaseReference
    private lateinit var uid: String
    private lateinit var recyclerView: RecyclerView
    private var mList = ArrayList<CatExpenseModel>()
    private lateinit var adapter: CatExpenseAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityViewAcategoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val catName = intent.getStringExtra("name").toString()
        val catId = intent.getStringExtra("id").toString()

        binding.tvTitle.text = catName

        //initialize variables
        auth = FirebaseAuth.getInstance()
        uid = auth.currentUser?.uid.toString()
        databaseRef = FirebaseDatabase.getInstance().reference.child("CategoryExpenses").child(uid).child(catId)
        databaseRefAddCat = FirebaseDatabase.getInstance().reference.child("CategoryExpenses").child(uid).child(catId)

        recyclerView = binding.recycleView
        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = LinearLayoutManager(this);

        databaseRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                mList.clear()
                for ( snapshot in snapshot.children){
                    val data = snapshot.getValue(CatExpenseModel::class.java)!!
                    if( data != null){
                        mList.add(data)
                        var amount = data.amt!!.toDouble()
                    }
                }
                adapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@ViewACategory, error.message, Toast.LENGTH_SHORT).show()
            }
        })

        adapter = CatExpenseAdapter(this, mList)
        recyclerView.adapter = adapter

        adapter.setOnItemClickListner(object: CatExpenseAdapter.onItemClickListner {
            override fun onItemClick(position: Int) {
                /*intent = Intent(applicationContext, ViewAHotel::class.java).also {
                    it.putExtra("name", mList[position].name)
                    it.putExtra("id", mList[position].id)
                    it.putExtra("description", mList[position].description)
                    it.putExtra("location", mList[position].location)
                    startActivity(it)
                }*/
            }

        })


        binding.btnAdd.setOnClickListener {
            var exName = binding.etExName.text.toString()
            var exAmt = binding.etExAmt.text.toString()
            if ( exName.isEmpty() ) {
                binding.etExName.error = "Please enter expense name"
            } else if (exAmt.isEmpty()) {
                binding.etExAmt.error = "Please enter expense amount"
            } else {
                //Id for new record
                var id = databaseRef.push().key!!
                //create a object
                val data = CatExpenseModel( id,exName,exAmt,catId,uid,catName)
                databaseRefAddCat.child(id).setValue(data).addOnCompleteListener {
                    if (it.isSuccessful){
                        binding.etExAmt.setText("")
                        binding.etExName.setText("")
                        Toast.makeText(this, "Expense added", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(this, it.exception?.message, Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }
}