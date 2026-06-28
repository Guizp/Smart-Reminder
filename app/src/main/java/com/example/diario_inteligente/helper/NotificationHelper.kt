package com.example.diario_inteligente.helper

import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import com.example.diario_inteligente.model.Reminder
import com.example.diario_inteligente.ui.MainActivity
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class NotificationHelper(private val context: Context) {
    private val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    private val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

    companion object {
        const val CHANNEL_ID = "smart_reminder_channel"
        const val CHANNEL_NAME = "Lembretes do Smart-Reminder"
    }

    fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                CHANNEL_NAME,
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = "Canal para avisos de prazos dos lembretes"
                enableLights(true)
                enableVibration(true)
            }
            notificationManager.createNotificationChannel(channel)
        }
    }

    fun agendarNotificacao(lembrete: Reminder, nomeUsuario: String) {
        // Valida se os novos campos de prazo nao estao vazios
        if (lembrete.prazoData.isEmpty() || lembrete.prazoHora.isEmpty()) return

        try {
            val formatoFormatador = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
            val dataTexto = "${lembrete.prazoData} ${lembrete.prazoHora}"
            val dataPrazoFinal = formatoFormatador.parse(dataTexto) ?: return

            val calendar = Calendar.getInstance()
            calendar.time = dataPrazoFinal
            val tempoPrazoFinalMillis = calendar.timeInMillis

            // Recupera a antecedência configurada nas SharedPreferences
            val preferences = context.getSharedPreferences("app_settings", Context.MODE_PRIVATE)
            val minutosAntecedencia = preferences.getInt("timer_minimo_lembrete", 5)
            val milissegundosSubtrair = minutosAntecedencia * 60L * 1000L

            // Define o momento exato em que o alarme deve tocar
            val tempoAlarmeMillis = tempoPrazoFinalMillis - milissegundosSubtrair

            if (tempoAlarmeMillis <= System.currentTimeMillis()) {
                Log.d("STUDENT_LOG", "O horario do alarme ja passou.")
                return
            }

            val intent = Intent("com.example.diario_inteligente.DISPARAR_ALERTA").apply {
                setPackage(context.packageName)
                putExtra("REMINDER_TITLE", lembrete.title)
                putExtra("REMINDER_TIME", lembrete.prazoHora)
                putExtra("USER_NAME", nomeUsuario)
            }

            val pendingIntent = PendingIntent.getBroadcast(
                context,
                lembrete.id.hashCode(),
                intent,
                PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
            )

            // Agendamento seguro tratando as mudancas de versoes do Android
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                alarmManager.setExactAndAllowWhileIdle(
                    AlarmManager.RTC_WAKEUP,
                    tempoAlarmeMillis,
                    pendingIntent
                )
            } else {
                alarmManager.setExact(
                    AlarmManager.RTC_WAKEUP,
                    tempoAlarmeMillis,
                    pendingIntent
                )
            }

            Log.d("STUDENT_LOG", "Alarme programado com sucesso para menos $minutosAntecedencia minutos.")

        } catch (e: Exception) {
            Log.e("STUDENT_LOG", "Erro ao agendar alarme", e)
        }
    }

    @android.annotation.SuppressLint("MissingPermission")
    fun dispararNotificacaoVisual(tituloReminder: String, horario: String, nomeUsuario: String) {
        val intent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }

        val pendingIntent = PendingIntent.getActivity(
            context,
            tituloReminder.hashCode(),
            intent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )

        val corpoTexto = "Hey $nomeUsuario, o seu Reminder \"$tituloReminder\" vence às $horario! Fique de olho!"

        val notification = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(android.R.drawable.ic_dialog_info)
            .setContentTitle("Prazo Próximo! ⏰")
            .setContentText(corpoTexto)
            .setStyle(NotificationCompat.BigTextStyle().bigText(corpoTexto))
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)
            .setDefaults(NotificationCompat.DEFAULT_ALL)
            .build()

        notificationManager.notify(tituloReminder.hashCode(), notification)
    }
}