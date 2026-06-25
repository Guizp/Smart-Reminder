package com.example.diario_inteligente.ui

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.diario_inteligente.auth.AuthManager
import com.example.diario_inteligente.databinding.ActivitySettingsBinding

class SettingsActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySettingsBinding
    private lateinit var preferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        preferences = getSharedPreferences(
            "app_settings",
            MODE_PRIVATE
        )

        carregarConfiguracoes()


        binding.switchTema.setOnCheckedChangeListener { _, isChecked ->
            preferences.edit()
                .putBoolean("tema_automatico", isChecked)
                .apply()

            Toast.makeText(
                this,
                "Configuração salva",
                Toast.LENGTH_SHORT
            ).show()
        }

        binding.switchNotificacoes.setOnCheckedChangeListener { _, isChecked ->
            preferences.edit()
                .putBoolean("notificacoes", isChecked)
                .apply()

            Toast.makeText(
                this,
                "Configuração salva",
                Toast.LENGTH_SHORT
            ).show()
        }

        binding.switchProximidade.setOnCheckedChangeListener { _, isChecked ->
            preferences.edit()
                .putBoolean("sensor_proximidade", isChecked)
                .apply()

            Toast.makeText(
                this,
                "Configuração salva",
                Toast.LENGTH_SHORT
            ).show()
        }

        binding.btnLogout.setOnClickListener {

            AuthManager().logout()

            startActivity(
                Intent(
                    this,
                    LoginActivity::class.java
                )
            )

            finishAffinity()
        }
    }

    private fun carregarConfiguracoes() {

        binding.switchTema.isChecked =
            preferences.getBoolean(
                "tema_automatico",
                true
            )

        binding.switchNotificacoes.isChecked =
            preferences.getBoolean(
                "notificacoes",
                true
            )

        binding.switchProximidade.isChecked =
            preferences.getBoolean(
                "sensor_proximidade",
                true
            )
    }
}