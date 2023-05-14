package com.example.budgetingapp


import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.budgetingapp.databinding.ActivityUpdateArticleBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class UpdateArticle : AppCompatActivity() {
    private lateinit var binding: ActivityUpdateArticleBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var databaseRef: DatabaseReference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUpdateArticleBinding.inflate(layoutInflater)
        setContentView(binding.root)


        //initialize variables accessing db
        databaseRef = FirebaseDatabase.getInstance().reference.child("articles")

        var title = intent.getStringExtra("title").toString()
        var id = intent.getStringExtra("id").toString()
        var description = intent.getStringExtra("description").toString()

        binding.etTitle.setText(title)
        binding.etDes.setText(description)


        //update article
        binding.btnUpdate.setOnClickListener {
            //title = binding.etTitle.text.toString()
            //description = binding.etDes.text.toString()

            var title = binding.etTitle.text.toString()
            var description = binding.etDes.text.toString()

            if(title.isEmpty() || description.isEmpty()){

                if(title.isEmpty()){
                    binding.etTitle.error = "Enter Topic"
                }
                if(description.isEmpty()){
                    binding.etDes.error = "Enter Description"
                }
            }else{

                val map = HashMap<String, Any>()

                //add data to hashMap
                map["title"] = title
                map["des"] = description

                //update database
                databaseRef.child(id).updateChildren(map).addOnCompleteListener {
                    if (it.isSuccessful) {
                        intent = Intent(applicationContext, AdminHome::class.java)
                        startActivity(intent)
                        Toast.makeText(this, "Updated successfully", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(this, it.exception?.message, Toast.LENGTH_SHORT).show()
                    }
                }
            }




        }

        //delete
        binding.btnDlt.setOnClickListener {
            databaseRef.child(id).removeValue().addOnCompleteListener {
                if (it.isSuccessful) {
                    Toast.makeText(this, "Item deleted", Toast.LENGTH_SHORT).show()
                    intent = Intent(applicationContext, AdminHome::class.java)
                    startActivity(intent)
                }
            }
        }
    }
}
