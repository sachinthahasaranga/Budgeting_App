package com.example.budgetingapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.budgetingapp.databinding.ActivityHomeBinding
import android.content.Intent
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.budgetingapp.Adapters.ArticlesAdapter
import com.example.budgetingapp.DataClasses.Article
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.*
import com.google.firebase.ktx.Firebase

class Home : AppCompatActivity() {

    private lateinit var binding: ActivityHomeBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var databaseRef: DatabaseReference
    private lateinit var uid: String
    private lateinit var recyclerView: RecyclerView
    private var mList = ArrayList<Article>()
    private lateinit var adapter: ArticlesAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //initialize late init variables
        auth = FirebaseAuth.getInstance()
        uid = auth.currentUser?.uid.toString()
        databaseRef = FirebaseDatabase.getInstance().reference.child("articles")

        recyclerView = binding.recyclerView
        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = LinearLayoutManager(this);

        //retrieve data from db
        databaseRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                mList.clear()
                for ( snapshot in snapshot.children){
                    val data = snapshot.getValue(Article::class.java)!!
                    if( data != null){
                        mList.add(data)
                    }
                }
                adapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@Home, error.message, Toast.LENGTH_SHORT).show()
            }
        })

        adapter = ArticlesAdapter(mList)
        recyclerView.adapter = adapter

        //send data to view an article
        //Setting onclick on recyclerView each item
        adapter.setOnItemClickListner(object: ArticlesAdapter.onItemClickListner {
            override fun onItemClick(position: Int) {
                intent = Intent(applicationContext, ViewAnArticle::class.java).also {
                    it.putExtra("title", mList[position].title)
                    it.putExtra("description", mList[position].des)
                    it.putExtra("id", mList[position].id)
                    startActivity(it)
                }
            }
        })

        binding.btnOptions.setOnClickListener{
            startActivity(Intent(this,OptionsMenu::class.java))
        }
        binding.btnExpenseTracker.setOnClickListener{
            startActivity(Intent(this,ExpenseTracker::class.java))
        }
        binding.btnBudgetCalc.setOnClickListener{
            startActivity(Intent(this,BudgetCalculator::class.java))
        }

    }
}