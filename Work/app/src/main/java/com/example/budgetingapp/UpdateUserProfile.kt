package com.example.budgetingapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.budgetingapp.databinding.ActivityUpdateUserProfileBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class UpdateUserProfile : AppCompatActivity() {
    private lateinit var binding: ActivityUpdateUserProfileBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var databaseRef: DatabaseReference
    private lateinit var uid: String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUpdateUserProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //initialize variables
        auth = FirebaseAuth.getInstance()
        uid = auth.currentUser?.uid.toString()
        databaseRef = FirebaseDatabase.getInstance().reference.child("users")

        var name = intent.getStringExtra("name").toString()
        var email = intent.getStringExtra("email").toString()
        var mobile = intent.getStringExtra("mobile").toString()

        //bind values to editTexts
        binding.etName.setText(name)
        binding.etEmail.setText(email)
        binding.etPhoneNumber.setText(mobile)

        binding.button2.setOnClickListener {
            name =  binding.etName.text.toString()
            email =  binding.etEmail.text.toString()
            mobile =  binding.etPhoneNumber.text.toString()

            val map = HashMap<String,Any>()

            //add data to hashMap
            map["name"] = name
            map["email"] = email
            map["mobile"] = mobile



            //update database from hashMap
            databaseRef.child(uid).updateChildren(map).addOnCompleteListener {
                if( it.isSuccessful){
                    intent = Intent(applicationContext, UserProfile::class.java)
                    startActivity(intent)
                    Toast.makeText(this, "Updated successfully", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, it.exception?.message, Toast.LENGTH_SHORT).show()
                }
            }

        }












    }
}