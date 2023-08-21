package com.project.VeterinaryManager.view

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.view.View
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.firestore.FirebaseFirestore
import com.project.VeterinaryManager.databinding.ActivitySchedulingBinding
import java.time.LocalTime
import java.util.Calendar


class Scheduling : AppCompatActivity() {

    private lateinit var binding: ActivitySchedulingBinding
    private val calendar: Calendar = Calendar.getInstance()
    private var data: String = ""
    private var hora: String = ""
    private lateinit var selectedAnimal: String
    private lateinit var selectedSex: String
    private lateinit var selectedSize: String
    private var animalAge: Double = 0.0

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySchedulingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        val cliente = intent.extras?.getString("cliente").toString()


        binding.datePickerButton.setOnClickListener {
            showDatePickerDialog()
        }

        binding.timePickerButton.setOnClickListener {
            showTimePickerDialog()
        }

        binding.schedulingButton.setOnClickListener {
            val optDog = binding.optDog
            val optCat = binding.optCat
            val optMale = binding.optMale
            val optFemale = binding.optFemale
            val optSmall = binding.optSmall
            val optMedium = binding.optMedium
            val optBig = binding.optBig
            val ageEditText = binding.animalAge

            when {
                hora.isEmpty() -> {
                    message(it, "Preencha o horário!", "#FF0000")
                }

                LocalTime.parse(hora) < LocalTime.parse("08:00") ||
                        LocalTime.parse(hora) > LocalTime.parse("18:00") -> {
                    message(it, "Horário de atendimento: 08:00 às 18:00!", "#FF0000")
                }

                data.isEmpty() -> {
                    message(it, "Preencha a data!", "#FF0000")
                }

                !optDog.isChecked && !optCat.isChecked -> {
                    message(it, "Escolha um animal (Cachorro ou Gato)!", "#FF0000")
                }

                !optMale.isChecked && !optFemale.isChecked -> {
                    message(it, "Escolha um sexo (Macho ou Fêmea)!", "#FF0000")
                }

                !optSmall.isChecked && !optMedium.isChecked && !optBig.isChecked -> {
                    message(it, "Escolha um tamanho (Pequeno, Médio ou Grande)!", "#FF0000")
                }

                ageEditText.text.toString().isEmpty() -> {
                    message(it, "Informe a idade do animal!", "#FF0000")
                }

                else -> {
                    selectedAnimal = if (optDog.isChecked) "Cachorro" else "Gato"
                    selectedSex = if (optMale.isChecked) "Macho" else "Fêmea"
                    selectedSize = when {
                        optSmall.isChecked -> "Pequeno"
                        optMedium.isChecked -> "Médio"
                        else -> "Grande"
                    }
                    animalAge = ageEditText.text.toString().toDouble()

                    saveSchedule(it, cliente, selectedAnimal, data, hora, selectedSex, selectedSize, animalAge.toString())
                    navigateToHomeActivity()
                }
            }
        }


    }

    private fun showDatePickerDialog() {
        val datePicker = DatePickerDialog(
            this,
            { _, year, monthOfYear, dayOfMonth ->
                val month = monthOfYear + 1
                data = "$dayOfMonth / $month / $year"
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )
        datePicker.show()
    }

    private fun showTimePickerDialog() {
        val timePicker = TimePickerDialog(
            this,
            { _, hourOfDay, minute ->
                val formattedHour = if (hourOfDay < 10) "0$hourOfDay" else hourOfDay.toString()
                val formattedMinute = if (minute < 10) "0$minute" else minute.toString()
                hora = "$formattedHour:$formattedMinute"
            },
            calendar.get(Calendar.HOUR_OF_DAY),
            calendar.get(Calendar.MINUTE),
            true
        )
        timePicker.show()
    }

    //Função para levar para a homePage
    private fun navigateToHomeActivity() {
        val handler = Handler()
        handler.postDelayed({
            val intent = Intent(this, home::class.java)
            startActivity(intent)
        }, 3000)
    }



    private fun message(view: View, message: String, cor: String) {
        val snackbar = Snackbar.make(view, message, Snackbar.LENGTH_SHORT)
        snackbar.setBackgroundTint(Color.parseColor(cor))
        snackbar.setTextColor(Color.parseColor("#FFFFFF"))
        snackbar.show()
    }

    private fun saveSchedule(
        view: View,
        cliente: String,
        option: String,
        data: String,
        hora: String,
        selectedSex: String,
        selectedSize: String,
        animalAge: String
    ) {
        // Aqui a val cliente já contém o nome do cliente recuperado da intent
        val db = FirebaseFirestore.getInstance()
        val userData = hashMapOf(
            "cliente" to cliente,
            "option" to option,
            "data" to data,
            "hora" to hora,
            "sexo" to selectedSex,
            "porte" to selectedSize,
            "idade" to animalAge
        )

        // userID = para fins de autenticação no firebase!
        val userID = "Iago"

        if (userID != null) {
            db.collection("agendamentos").document(userID).set(userData)
                .addOnSuccessListener {
                    message(view, "Agendamento realizado com sucesso!", "#008000")
                    Handler().postDelayed({
                        navigateToHomeActivity()
                    }, 4000) // Delay de 4 segundos
                }
                .addOnFailureListener {
                    message(view, "Erro no servidor.", "#FF0000")
                }
        } else {
            // Não foi possível obter o ID do usuário autenticado
            message(view, "Erro na autenticação.", "#FF0000")
        }
    }

}