package com.example.budgetingapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

import android.content.Intent
import android.widget.Toast
import com.example.budgetingapp.databinding.ActivityUpdateExpenseBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class UpdateExpense : AppCompatActivity() {
    private lateinit var binding: ActivityUpdateExpenseBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var databaseRef: DatabaseReference
    private lateinit var uid: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUpdateExpenseBinding.inflate(layoutInflater)
        setContentView(binding.root)

        var type = intent.getStringExtra("type").toString()
        val amt = intent.getStringExtra("amt").toString()


        //bind values to editTexts
        binding.etType.setText(type)
        binding.etAmount.setText(amt)

        //initialize variables
        auth = FirebaseAuth.getInstance()
        uid = auth.currentUser?.uid.toString()
        databaseRef = FirebaseDatabase.getInstance().reference.child("expenses").child(uid)

        binding.btnSubmit.setOnClickListener {
            updateExpense()
        }
    }

    private fun updateExpense() {
        var amt = binding.etAmount.text.toString()
        var type = binding.etType.text.toString()

        if (amt.isEmpty() || type.isEmpty()) {

            if (amt.isEmpty()) {
                binding.etAmount.error = "Enter amount"
            }
            if (type.isEmpty()) {
                binding.etType.error = "Enter type"
            }
        } else {
            val map = HashMap<String,Any>()

            //add data to hashMap
            map["amt"] = amt
            map["type"] = type

            val id = intent.getStringExtra("id").toString()

            //update database from hashMap
            databaseRef.child(id).updateChildren(map).addOnCompleteListener {
                if( it.isSuccessful){
                    startActivity(Intent(this,ExpenseTracker::class.java))
                    Toast.makeText(this, "Expense Updated", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, it.exception?.message, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

}