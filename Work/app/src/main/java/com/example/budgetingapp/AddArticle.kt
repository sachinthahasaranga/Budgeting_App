package com.example.budgetingapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.budgetingapp.DataClasses.Article
import com.example.budgetingapp.databinding.ActivityAddArticleBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class AddArticle : AppCompatActivity() {
    private lateinit var binding: ActivityAddArticleBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var databaseRef: DatabaseReference
    private lateinit var uid: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityAddArticleBinding.inflate(layoutInflater)
        setContentView(binding.root)


        //initialize variables
        auth = FirebaseAuth.getInstance()
        uid = auth.currentUser?.uid.toString()
        databaseRef = FirebaseDatabase.getInstance().reference.child("articles")

        //validation
        binding.btnPublish.setOnClickListener {
            var title = binding.etTitle.text.toString()
            var des = binding.etDes.text.toString()

            if(title.isEmpty() || des.isEmpty()){

                if(title.isEmpty()){
                    binding.etTitle.error = "Enter Topic"
                }
                if(des.isEmpty()){
                    binding.etDes.error = "Enter Description"
                }
            } else {
                //Id for new record
                var id = databaseRef.push().key!!
                //create a object
                val data = Article( id,title, des, uid)
                databaseRef.child(id).setValue(data).addOnCompleteListener {
                    if (it.isSuccessful){
                        intent = Intent(applicationContext, AdminHome::class.java)
                        startActivity(intent)
                        Toast.makeText(this, "Article added", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(this, it.exception?.message, Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }
}