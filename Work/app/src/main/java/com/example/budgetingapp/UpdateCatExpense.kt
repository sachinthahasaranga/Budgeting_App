package com.example.budgetingapp
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

import android.content.Intent
import android.widget.Toast
import com.example.budgetingapp.databinding.ActivityUpdateCatExpenseBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class UpdateCatExpense : AppCompatActivity() {
    private lateinit var binding: ActivityUpdateCatExpenseBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var databaseRef: DatabaseReference
    private lateinit var uid: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUpdateCatExpenseBinding.inflate(layoutInflater)
        setContentView(binding.root)

        var name = intent.getStringExtra("name").toString()
        val amt = intent.getStringExtra("amt").toString()
        val exId = intent.getStringExtra("exId").toString()
        val catId = intent.getStringExtra("catId").toString()
        val catName = intent.getStringExtra("catName").toString()


        //bind values to editTexts
        binding.etExName.setText(name)
        binding.etExAmt.setText(amt)

        //initialize variables
        auth = FirebaseAuth.getInstance()
        uid = auth.currentUser?.uid.toString()
        databaseRef = FirebaseDatabase.getInstance().reference.child("CategoryExpenses").child(uid!!).child(catId!!).child(exId!!)

        binding.btnAdd.setOnClickListener {
            var type = binding.etExName.text.toString()
            var amt = binding.etExAmt.text.toString()

            if (amt.isEmpty() || type.isEmpty()) {

                if (amt.isEmpty()) {
                    binding.etExName.error = "Enter amount"
                }
                if (type.isEmpty()) {
                    binding.etExAmt.error = "Enter type"
                }
            } else {

                val map = HashMap<String,Any>()

                //add data to hashMap
                map["amt"] = amt
                map["name"] = type

                //update database from hashMap
                databaseRef.updateChildren(map).addOnCompleteListener {
                    if( it.isSuccessful){
                        intent = Intent(applicationContext, ViewACategory::class.java).also {
                            it.putExtra("name",catName)
                            it.putExtra("id", catId)
                            startActivity(it)
                        }
                        Toast.makeText(this, "Expense Updated", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(this, it.exception?.message, Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }


}