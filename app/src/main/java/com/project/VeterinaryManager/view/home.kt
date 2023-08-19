package com.project.VeterinaryManager.view

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.FirebaseFirestore
import com.project.VeterinaryManager.R
import com.project.VeterinaryManager.databinding.ActivityHomeBinding

class home : AppCompatActivity() {

    private lateinit var binding: ActivityHomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()
        val name = intent.extras?.getString("name")

        binding.mySchedulings.setOnClickListener {
            fetchUserAgendas(name ?: "")
        }

        binding.scheduleButton.setOnClickListener {
            val intent = Intent(this, Scheduling::class.java)
            intent.putExtra("name", name)
            startActivity(intent)
        }
    }

    private fun fetchUserAgendas(name: String) {
        val db = FirebaseFirestore.getInstance()

        db.collection("agendamentos")
            .whereEqualTo("cliente", name)
            .get()
            .addOnSuccessListener { documents ->
                val agendas = mutableListOf<String>()

                for (document in documents) {
                    val option = document.getString("option")
                    val data = document.getString("data")
                    val hora = document.getString("hora")

                    agendas.add("Animal: $option\n Data: $data\n Hora: $hora\n--------------------------------------")
                }
                showMyAgendasPopup(agendas)
            }
            .addOnFailureListener { exception ->
                Toast.makeText(this, "Erro ao buscar as agendas.", Toast.LENGTH_SHORT).show()
            }
    }


    private fun showMyAgendasPopup(agendas: List<String>) {
        val dialogView = layoutInflater.inflate(R.layout.my_schedules_popup, null)
        val dialogBuilder = AlertDialog.Builder(this)
            .setView(dialogView)
        val alertDialog = dialogBuilder.create()

        val agendasTextView = dialogView.findViewById<TextView>(R.id.agendasTextView)
        agendasTextView.text = "Suas agendas:\n\n${agendas.joinToString("\n")}"

        val closeButton = dialogView.findViewById<Button>(R.id.closeButton)
        closeButton.setOnClickListener {
            alertDialog.dismiss()
        }

        alertDialog.show()
    }
}
