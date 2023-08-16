package com.project.VeterinaryManager.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.project.VeterinaryManager.R
import com.project.VeterinaryManager.databinding.ActivityHomeBinding

class home : AppCompatActivity() {

    private lateinit var binding: ActivityHomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val name = intent.extras?.getString("name")

        binding.userName.text = "Bem-vindo(a),$name"

    }
}