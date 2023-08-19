package com.project.VeterinaryManager.view

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.graphics.Color
import android.os.Build
import android.os.Bundle
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

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySchedulingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        val name = intent.extras?.getString("nome").toString()

        binding.datePickerButton.setOnClickListener {
            showDatePickerDialog()
        }

        binding.timePickerButton.setOnClickListener {
            showTimePickerDialog()
        }

        binding.schedulingButton.setOnClickListener {

            val optDog = binding.optDog
            val optCat = binding.optCat

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

                optDog.isChecked && optCat.isChecked && data.isNotEmpty() && hora.isNotEmpty() -> {
                    saveSchedule(it, name, "Cachorro/Gato", data, hora)
                }

                optDog.isChecked && data.isNotEmpty() && hora.isNotEmpty() -> {
                    saveSchedule(it, name, "Cachorro", data, hora)
                }

                optCat.isChecked && data.isNotEmpty() && hora.isNotEmpty() -> {
                    saveSchedule(it, name, "Gato", data, hora)
                }

                else -> {
                    message(it, "Escolha algum animal!", "#FF0000")
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
        hora: String
    ) {
        // Aqui a val name pega o nome do usuário digitado na tela de login, alterar para um possível email!
        val name = intent.getStringExtra("name") ?: ""
        //
        val db = FirebaseFirestore.getInstance()
        val userData = hashMapOf(
            "cliente" to name,
            "option" to option,
            "data" to data,
            "hora" to hora
        )

        // userID = para fins de autenticação no firebase!
        val userID = "Iago"

        if (userID != null) {
            db.collection("agendamentos").document(userID).set(userData)
                .addOnSuccessListener {
                    message(view, "Agendamento realizado com sucesso!", "#008000")
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

