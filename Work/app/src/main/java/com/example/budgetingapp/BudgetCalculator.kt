package com.example.budgetingapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.budgetingapp.databinding.ActivityBudgetCalculatorBinding

class BudgetCalculator : AppCompatActivity() {
    private lateinit var binding: ActivityBudgetCalculatorBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBudgetCalculatorBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnAddNewCategory.setOnClickListener {
            startActivity(Intent(this,AddNewCategory::class.java))
        }

        binding.btnBudgetCategory.setOnClickListener {
            startActivity(Intent(this,ViewAllCategories::class.java))
        }


    }
}