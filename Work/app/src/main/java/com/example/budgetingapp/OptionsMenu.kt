package com.example.budgetingapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.budgetingapp.databinding.ActivityOptionsMenuBinding

class OptionsMenu : AppCompatActivity() {
    private lateinit var binding: ActivityOptionsMenuBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOptionsMenuBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnViewProfile.setOnClickListener {
           startActivity(Intent(this,UserProfile::class.java))
        }


    }
}