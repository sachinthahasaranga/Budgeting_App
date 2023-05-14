package com.example.budgetingapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.budgetingapp.DataClasses.User
import com.example.budgetingapp.databinding.ActivityLoginBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class Login : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var databaseRef: DatabaseReference
    private lateinit var uid: String
    private val emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //initialize variables
        auth = FirebaseAuth.getInstance()

        if( auth.currentUser != null ) {
            checkUserTypeAndRedirect()
        }

        //login
        binding.btnLogin.setOnClickListener {

            val email = binding.etEmail.text.toString()
            val password = binding.etPwd.text.toString()

            //checking if the input fields are empty
            if(email.isEmpty() || password.isEmpty()){

                if(email.isEmpty()){
                    binding.etEmail.error = "Enter your email address"
                }
                if(password.isEmpty()){
                    binding.etPwd.error = "Enter your password"
                }
            } else if (!email.matches(emailPattern.toRegex())){
                //validate email pattern
                binding.etEmail.error = "Enter a valid email address"
            } else if (password.length < 7){
                //validate passwords
                binding.etPwd.error = "Password must be at least 7 characters."
            } else{
                //Log in
                auth.signInWithEmailAndPassword(email, password).addOnCompleteListener{
                    if(it.isSuccessful){
                        //redirect users to their respective dashboards
                        checkUserTypeAndRedirect()
                    }else{
                        Toast.makeText(this, it.exception.toString(), Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }

        //navigate to register page
        binding.register.setOnClickListener{
            startActivity(Intent(this,Register::class.java))
        }

    }

    private fun checkUserTypeAndRedirect() {
        uid = auth.currentUser?.uid.toString()
        databaseRef = FirebaseDatabase.getInstance().reference.child("users").child(uid)
        databaseRef.addValueEventListener(object : ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot) {
                //retrieve values from the db and convert them to user data class
                var user = snapshot.getValue(User::class.java)!!

                if( user.type == "admin") {
                    intent = Intent(applicationContext, AdminHome::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    startActivity(intent)
                    finish()
                } else if( user.type == "user"){
                    intent = Intent(applicationContext, Home::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    startActivity(intent)
                    finish()
                } else {
                    Toast.makeText(this@Login, "Your account is not valid.", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@Login, "Failed to retrieve user", Toast.LENGTH_SHORT).show()
            }
        })
    }

}