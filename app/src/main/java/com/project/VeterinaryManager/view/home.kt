package com.project.VeterinaryManager.view

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
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

        binding.scheduleButton.setOnClickListener {
            val intent = Intent(this, Scheduling::class.java)
            intent.putExtra("name",name)
            startActivity(intent)
        }
    }

    fun openDialog(view: View) {
        val dialogView = layoutInflater.inflate(R.layout.dialog_layout, null)
        val dialogBuilder = AlertDialog.Builder(this)
            .setView(dialogView)
        val alertDialog = dialogBuilder.create()
        val dialogText = dialogView.findViewById<TextView>(R.id.dialogText)
        val dialogButton = dialogView.findViewById<Button>(R.id.dialogButton)

        dialogText.text = "INFORMAÇÕES IMPORTANTES:"

        dialogButton.setOnClickListener {
            alertDialog.dismiss()
        }
        alertDialog.show()
    }


}