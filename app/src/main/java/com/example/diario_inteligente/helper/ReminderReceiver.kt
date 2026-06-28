package com.example.diario_inteligente.helper

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log

class ReminderReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        Log.d("STUDENT_LOG", "ReminderReceiver acionado!")

        val titulo = intent.getStringExtra("REMINDER_TITLE") ?: "Lembrete"
        val horario = intent.getStringExtra("REMINDER_TIME") ?: "--:--"
        val nomeUsuario = intent.getStringExtra("USER_NAME") ?: "Usuário"

        try {
            val notificationHelper = NotificationHelper(context)
            notificationHelper.createNotificationChannel()

            notificationHelper.dispararNotificacaoVisual(
                tituloReminder = titulo,
                horario = horario,
                nomeUsuario = nomeUsuario
            )
        } catch (e: Exception) {
            Log.e("STUDENT_LOG", "Erro no Receiver", e)
        }
    }
}