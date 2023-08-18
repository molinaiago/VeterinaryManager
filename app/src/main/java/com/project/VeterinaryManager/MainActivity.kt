package com.project.VeterinaryManager

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import com.google.android.material.snackbar.Snackbar
import com.project.VeterinaryManager.databinding.ActivityMainBinding
import com.project.VeterinaryManager.view.RegisterUser
import com.project.VeterinaryManager.view.home

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        window.statusBarColor = Color.parseColor("#FFFFFF")

        // Levar para tela home
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

        // Levar para tela de registro
        binding.txtScreenRegister.setOnClickListener {
            val intent = Intent(this, RegisterUser::class.java)
            startActivity(intent)
        }

    }
    private fun message(view: View, message: String) {
        val snackbar = Snackbar.make(view, message, Snackbar.LENGTH_SHORT)
        snackbar.view.setBackgroundColor(Color.parseColor("#FF0000"))
        snackbar.setTextColor(Color.parseColor("#FFFFFF"))
        snackbar.show()
    }
    private fun goToHome(name: String) {
        val intent = Intent(this, home::class.java)
        intent.putExtra("name",name)
        startActivity(intent)
    }

    private fun login(view: View) {
        val progressBar = binding.progressBar
        progressBar.visibility = View.VISIBLE
        binding.loginButton.isEnabled = false
        binding.loginButton.setTextColor(Color.parseColor("#FFFFFF"))

        Handler(Looper.getMainLooper()).postDelayed({
            message(view, "Login efetuado com sucesso!")
        },3000)
    }
}


