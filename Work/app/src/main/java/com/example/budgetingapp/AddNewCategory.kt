package com.example.budgetingapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.content.Intent
import android.widget.Toast
import com.example.budgetingapp.DataClasses.CategoryModel
import com.example.budgetingapp.databinding.ActivityAddNewCategoryBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class AddNewCategory : AppCompatActivity() {

    private lateinit var binding: ActivityAddNewCategoryBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var databaseRef: DatabaseReference
    private lateinit var uid: String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddNewCategoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //initialize variables
        auth = FirebaseAuth.getInstance()
        uid = auth.currentUser?.uid.toString()
        databaseRef = FirebaseDatabase.getInstance().reference.child("Categories").child(uid)

        binding.btnAdd.setOnClickListener {
            var catName = binding.categoryNameText.text.toString()
            if ( catName.isEmpty() ) {
                binding.categoryNameText.error = "Please enter category name"
            } else {
                //Id for new record
                var id = databaseRef.push().key!!
                //create a object
                val data = CategoryModel( id,catName,uid)
                databaseRef.child(id).setValue(data).addOnCompleteListener {
                    if (it.isSuccessful){
                        intent = Intent(applicationContext, ViewAllCategories::class.java)
                        startActivity(intent)
                        Toast.makeText(this, "Category created successfully", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(this, it.exception?.message, Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }
}