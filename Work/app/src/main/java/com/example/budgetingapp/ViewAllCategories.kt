package com.example.budgetingapp


import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.budgetingapp.databinding.ActivityViewAllCategoriesBinding
import android.content.Intent
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.budgetingapp.Adapters.AllCategoriesAdapter
import com.example.budgetingapp.DataClasses.Article
import com.example.budgetingapp.DataClasses.CategoryModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.*
import com.google.firebase.ktx.Firebase

class ViewAllCategories : AppCompatActivity() {

    private lateinit var binding: ActivityViewAllCategoriesBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var databaseRef: DatabaseReference
    private lateinit var uid: String
    private lateinit var recyclerView: RecyclerView
    private var mList = ArrayList<CategoryModel>()
    private lateinit var adapter: AllCategoriesAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityViewAllCategoriesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //initialize late init variables
        auth = FirebaseAuth.getInstance()
        uid = auth.currentUser?.uid.toString()
        databaseRef = FirebaseDatabase.getInstance().reference.child("Categories").child(uid)

        recyclerView = binding.recyclerView
        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = LinearLayoutManager(this);

        databaseRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                mList.clear()
                for ( snapshot in snapshot.children){
                    val data = snapshot.getValue(CategoryModel::class.java)!!
                    if( data != null){
                        mList.add(data)
                    }
                }
                adapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@ViewAllCategories, error.message, Toast.LENGTH_SHORT).show()
            }
        })

        adapter = AllCategoriesAdapter(this,mList)
        recyclerView.adapter = adapter


        //Setting onclick on recyclerView each item
        adapter.setOnItemClickListner(object: AllCategoriesAdapter.onItemClickListner {
            override fun onItemClick(position: Int) {
                /*intent = Intent(applicationContext, UpdateArticle::class.java).also {
                    it.putExtra("title", mList[position].title)
                    it.putExtra("description", mList[position].des)
                    it.putExtra("id", mList[position].id)
                    startActivity(it)
                }*/
            }
        })
    }
}