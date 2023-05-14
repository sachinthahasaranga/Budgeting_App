package com.example.budgetingapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.budgetingapp.databinding.ActivityHomeBinding
import com.example.budgetingapp.databinding.ActivityViewAnArticleBinding


class ViewAnArticle : AppCompatActivity() {
    private lateinit var binding: ActivityViewAnArticleBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityViewAnArticleBinding.inflate(layoutInflater)
        setContentView(binding.root)

        var title = intent.getStringExtra("title").toString()
        var id = intent.getStringExtra("id").toString()
        var description = intent.getStringExtra("description").toString()

        binding.tvTitle.text = title
        binding.tvDes.text = description


    }
}