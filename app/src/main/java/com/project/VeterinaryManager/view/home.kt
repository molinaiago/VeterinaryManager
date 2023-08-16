package com.project.VeterinaryManager.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.project.VeterinaryManager.R
import com.project.VeterinaryManager.adapter.ServicesAdapter
import com.project.VeterinaryManager.databinding.ActivityHomeBinding
import com.project.VeterinaryManager.model.Services

class home : AppCompatActivity() {

    private lateinit var binding: ActivityHomeBinding
    private lateinit var servicesAdapter: ServicesAdapter
    private val listServices: MutableList<Services> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()
        val name = intent.extras?.getString("name")

        binding.userName.text = "Bem-vindo(a),$name"
        val recyclerViewServices = binding.recyclerViewServices
        recyclerViewServices.layoutManager = GridLayoutManager(this,1)
        servicesAdapter = ServicesAdapter(this,listServices)
        recyclerViewServices.setHasFixedSize(true)
        recyclerViewServices.adapter = servicesAdapter
        getServices()

        binding.scheduleButton.setOnClickListener {
            val intent = Intent(this, Scheduling::class.java)
            intent.putExtra("name",name)
            startActivity(intent)
        }
    }

    private fun getServices() {
        val service1 = Services(R.drawable.dog,"Cachorro")
        listServices.add(service1)

        val service2 = Services(R.drawable.cat,"Gato")
        listServices.add(service2)


    }
}