package com.example.budgetingapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.budgetingapp.databinding.ActivityAdminHomeBinding
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

class AdminHome : AppCompatActivity() {

    private lateinit var binding: ActivityAdminHomeBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var databaseRef: DatabaseReference
    private lateinit var uid: String
    private lateinit var recyclerView: RecyclerView
    private var mList = ArrayList<Article>()
    private lateinit var adapter: ArticlesAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAdminHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //initialize late init variables
        auth = FirebaseAuth.getInstance()
        uid = auth.currentUser?.uid.toString()
        databaseRef = FirebaseDatabase.getInstance().reference.child("articles")

        recyclerView = binding.recyclerView
        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = LinearLayoutManager(this);

        //retrieve data from the db
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
                Toast.makeText(this@AdminHome, error.message, Toast.LENGTH_SHORT).show()
            }
        })

        adapter = ArticlesAdapter(mList)
        recyclerView.adapter = adapter

        //pass data to update article
        //Setting onclick on recyclerView each item
        adapter.setOnItemClickListner(object: ArticlesAdapter.onItemClickListner {
            override fun onItemClick(position: Int) {
                intent = Intent(applicationContext, UpdateArticle::class.java).also {
                    it.putExtra("title", mList[position].title)
                    it.putExtra("description", mList[position].des)
                    it.putExtra("id", mList[position].id)
                    startActivity(it)
                }
            }
        })

        //navigate to add article page
        binding.btnNewArticle.setOnClickListener {
            startActivity(Intent(this,AddArticle::class.java))
        }

        //logout
        binding.btnLogout.setOnClickListener {
            Firebase.auth.signOut()
            //redirect user to login page
            intent = Intent(applicationContext, Login::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            finish()
        }

    }
}