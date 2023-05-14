package com.example.budgetingapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.budgetingapp.DataClasses.User
import com.example.budgetingapp.databinding.ActivityUserProfileBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.*
import com.google.firebase.ktx.Firebase

class UserProfile : AppCompatActivity() {
    private lateinit var binding: ActivityUserProfileBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var databaseRef: DatabaseReference
    private lateinit var uid: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)


        //initialize variables
        auth = FirebaseAuth.getInstance()
        uid = auth.currentUser?.uid.toString()
        databaseRef = FirebaseDatabase.getInstance().reference.child("users")
//data read
        databaseRef.child(auth.currentUser!!.uid).addValueEventListener(object :
            ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot) {
                //retrieve values from the db and convert them to user data class
                var user = snapshot.getValue(User::class.java)!!
//bind for fornend
                binding.tvName.text = user.name
                binding.tvEmail.text = user.email
                binding.tvNumber.text = user.mobile
                binding.tvNic.text = user.nic

            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@UserProfile, "Faild to fetch user", Toast.LENGTH_SHORT).show()

            }
        })
// after push edit
        binding.btnEdit.setOnClickListener {

            var name = binding.tvName.text.toString()
            var email = binding.tvEmail.text.toString()
            var mobile = binding.tvNumber.text.toString()
            var nic = binding.tvNic.text.toString()

            intent = Intent(applicationContext, UpdateUserProfile::class.java).also {
                it.putExtra("name", name)
                it.putExtra("email",email)
                it.putExtra("mobile",mobile)
                it.putExtra("nic",nic)
                startActivity(it)
            }
        }

        binding.button5.setOnClickListener {
            Firebase.auth.signOut()

            //redirect user to login page
            intent = Intent(applicationContext, Login::class.java)
            startActivity(intent)

            //toast message
            Toast.makeText(this, "Successfully logged out", Toast.LENGTH_SHORT).show()
        }


    }
}