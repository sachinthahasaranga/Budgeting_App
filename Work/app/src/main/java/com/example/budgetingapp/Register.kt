package com.example.budgetingapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.budgetingapp.DataClasses.User
import com.example.budgetingapp.databinding.ActivityRegisterBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class Register : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var database: FirebaseDatabase
    //email pattern
    private val emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //Initializing auth and database variables
        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance()
//validations
        binding.btnRegister.setOnClickListener {
            val name = binding.etName.text.toString()
            val email = binding.etEmail.text.toString()
            val pwd = binding.etPwd.text.toString()
            val confPwd = binding.etConfPwd.text.toString()
            val nic = binding.etNIC.text.toString()
            val mobile = binding.etMobile.text.toString()

            if(name.isEmpty() || email.isEmpty() || pwd.isEmpty() || confPwd.isEmpty() || nic.isEmpty() || mobile.isEmpty()){

                if(name.isEmpty()){
                    binding.etName.error = "Please enter your name"
                }
                if(email.isEmpty()){
                    binding.etEmail.error = "Please enter your email"
                }
                if(pwd.isEmpty()){
                    binding.etPwd.error = "Please enter your password"
                }
                if(confPwd.isEmpty()){
                    binding.etConfPwd.error = "Please enter your password again"
                }
            }

            else if (!email.matches(emailPattern.toRegex())) {
                binding.etEmail.error = "Please enter a valid email address"

            }
            else if (pwd.length < 7) {
                binding.etPwd.error = "Password must be at least 7 characters."

            } else if (confPwd != pwd) {
                binding.etConfPwd.error = "Passwords do not match."

            }else {
                auth.createUserWithEmailAndPassword(email, pwd).addOnCompleteListener {

                    if (it.isSuccessful) {
                        //store user details in the database
                        val databaseRef =
                            database.reference.child("users").child(auth.currentUser!!.uid)
                        val user: User = User(name, email,auth.currentUser!!.uid,"user",nic, mobile)
                        databaseRef.setValue(user).addOnCompleteListener {
                            if (it.isSuccessful) {
                                //redirect user to login activity
                                val intent = Intent(this, Login::class.java)
                                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                                startActivity(intent)
                                finish()
                            } else {
                                Toast.makeText(this, "Something went wrong, try again", Toast.LENGTH_SHORT).show()
                            }
                        }
                    } else {
                        Toast.makeText(this, "Something went wrong, try again", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }
}