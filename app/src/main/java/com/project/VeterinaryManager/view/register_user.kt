package com.project.VeterinaryManager.view

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.project.VeterinaryManager.MainActivity
import com.project.VeterinaryManager.R

class register_user : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register_user)

        val loginButton = findViewById<Button>(R.id.registerButton)
        loginButton.setOnClickListener {

            val nameEditText = findViewById<TextInputEditText>(R.id.editRegisterName)
            val passwordEditText = findViewById<TextInputEditText>(R.id.editPasswordRegister)
            val cpfEditText = findViewById<TextInputEditText>(R.id.editCPF)
            val streetEditText = findViewById<TextInputEditText>(R.id.editLocationStreet)
            val numberEditText = findViewById<TextInputEditText>(R.id.editLocationNumber)
            val bairroEditText = findViewById<TextInputEditText>(R.id.editLocationBairro)
            val emailEditText = findViewById<TextInputEditText>(R.id.editEmailRegister)
            val email = emailEditText.text.toString()
            val name = nameEditText.text.toString().trim()
            val password = passwordEditText.text.toString()
            val cpf = cpfEditText.text.toString().trim()
            val street = streetEditText.text.toString().trim()
            val number = numberEditText.text.toString().trim()
            val bairro = bairroEditText.text.toString().trim()

            if (validateFields()) {
                saveUser(it,name,email,password,cpf,street,number,bairro)
                navigateToMainActivity()
            }
        }
    }

    //Função para validar os dados do usuário
    private fun validateFields(): Boolean {

        //Nome
        val nameEditText = findViewById<TextInputEditText>(R.id.editRegisterName)
        val name = nameEditText.text.toString().trim()
        if (name.isEmpty()) {
                message(nameEditText,"Digite o seu nome!","#FF0000")
                return false;
            }

        //Email
        val emailEditText = findViewById<TextInputEditText>(R.id.editEmailRegister)
        val email = emailEditText.text.toString()
           if (email.isEmpty()) {
                message(emailEditText, "Digite o seu e-mail!", "#FF0000")
                return false // Retorna false para indicar que a validação falhou
            }

            val atIndex = email.lastIndexOf("@")
            if (atIndex != -1 && atIndex < email.length - 1) {
                val domain = email.substring(atIndex + 1)
                if (domain != domain.toLowerCase()) {
                    message(
                        emailEditText,
                        "Depois de > @ < do e-mail deve ser minúsculo!",
                        "#FF0000"
                    )
                    return false
                }
            } else {
                // Caso não tenha o "@" ou esteja no final, o e-mail é inválido
                message(emailEditText, "Digite um e-mail válido", "#FF0000")
                return false
            }


        //Senha
        val passwordEditText = findViewById<TextInputEditText>(R.id.editPasswordRegister)
        val password = passwordEditText.text.toString()

            if(password.length < 8) {
                message(passwordEditText, "A senha deve conter no mínimo 8 caracteres", "#FF0000")
                return false
            }
            if(password.isEmpty()) {
                message(passwordEditText,"Digite uma senha!","#FF0000")
            }

        // CPF
        val cpfEditText = findViewById<TextInputEditText>(R.id.editCPF)
        val cpf = cpfEditText.text.toString()

            if (cpf.length != 11) {
                message(cpfEditText, "CPF inválido: deve conter 11 dígitos", "#FF0000")
                return false // Retorna false para indicar que a validação falhou
            }

        //Localização
        val editLocationStreet = findViewById<TextInputEditText>(R.id.editLocationStreet)
        val street = editLocationStreet.text.toString()

        val editLocationNumber = findViewById<TextInputEditText>(R.id.editLocationNumber)
        val number = editLocationNumber.text.toString()

        val editLocationBairro = findViewById<TextInputEditText>(R.id.editLocationBairro)
        val bairro = editLocationBairro.text.toString()

        if (street.isEmpty()) {
            message(editLocationStreet, "Digite a rua!", "#FF0000")
            return false
        }

        if (number.isEmpty()) {
            message(editLocationNumber, "Digite o número!", "#FF0000")
            return false
        }

        if (bairro.isEmpty()) {
            message(editLocationBairro, "Digite o bairro!", "#FF0000")
            return false
        }

        val registerButton = findViewById<Button>(R.id.registerButton)
        message(registerButton, "Cadastro concluído com sucesso!", "#008000")
        return true
    }

    //Função para levar para a mainPage
    private fun navigateToMainActivity() {
        val handler = Handler()
        handler.postDelayed({
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }, 2000)
    }


    //Mensagens pop-up
    private fun message(view: View, message: String, cor: String) {
        val snackbar = Snackbar.make(view,message, Snackbar.LENGTH_SHORT)
        snackbar.setBackgroundTint(Color.parseColor(cor))
        snackbar.setTextColor(Color.parseColor("#FFFFFF"))
        snackbar.show()
    }

    private fun saveUser(view: View, nome: String, email: String, senha: String, cpf: String, rua: String, numero: String, bairro: String) {

        val db = FirebaseFirestore.getInstance()
        val userData = hashMapOf(
            "nome" to nome,
            "email" to email,
            "senha" to senha,
            "cpf" to cpf,
            "rua" to rua,
            "numero" to numero,
            "bairro" to bairro
        )

        val uID = "Iago"

        if (uID != null) {
            db.collection("users").document(uID).set(userData)
                .addOnSuccessListener {
                    message(view, "Cadastro realizado com sucesso!", "#008000")
                }
                .addOnFailureListener {
                    message(view, "Erro no servidor.", "#FF0000")
                }
        } else {
            // Não foi possível obter o UID do usuário autenticado
            message(view, "Erro na autenticação.", "#FF0000")
        }
    }
}
