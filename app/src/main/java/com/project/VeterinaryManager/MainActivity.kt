package com.project.VeterinaryManager

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.google.android.material.snackbar.Snackbar
import com.project.VeterinaryManager.databinding.ActivityMainBinding
import com.project.VeterinaryManager.view.home

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        binding.loginButton.setOnClickListener {
            val name = binding.editName.text.toString()
            val password = binding.editPassword.text.toString()

            when {
                name.isEmpty() -> {
                    message(it,"Coloque o seu nome!")
                } password.isEmpty() -> {
                    message(it,"Preencha a senha!")
                } password.length <= 5 -> {
                    message(it,"A senha precisa ter no mínimo 6 caracteres!")
                } else -> {
                    goToHome(name)
                }
            }
        }

    }
    private fun message(view: View, message: String) {
        val snackbar = Snackbar.make(view,message,Snackbar.LENGTH_SHORT)
        snackbar.setBackgroundTint(Color.parseColor("#FF000"))
        snackbar.setTextColor(Color.parseColor("#FFFFFF"))
        snackbar.show()
    }

    private fun goToHome(name: String) {
        val intent = Intent(this, home::class.java)
        intent.putExtra("nome",name)
        startActivity(intent)
    }

}
