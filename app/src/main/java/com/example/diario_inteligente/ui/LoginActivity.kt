package com.example.diario_inteligente.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.diario_inteligente.databinding.ActivityLoginBinding
import com.google.firebase.auth.FirebaseAuth

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private val auth = FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.txtIrParaCadastro.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }

        binding.btnLogar.setOnClickListener {
            val email = binding.edtEmailLogin.text.toString().trim()
            val senha = binding.edtSenhaLogin.text.toString().trim()

            if (email.isEmpty() || senha.isEmpty()) {
                Toast.makeText(this, "Preencha o e-mail e a senha!", Toast.LENGTH_SHORT).show()
                Log.w("LOGIN_VALIDACAO", "Tentativa de login com campos em branco")
                return@setOnClickListener
            }

            Log.d("LOGIN_FIREBASE", "Tentando autenticar o e-mail: $email")

            auth.signInWithEmailAndPassword(email, senha)
                .addOnSuccessListener { authResult ->
                    Log.d("LOGIN_FIREBASE", "Login deu certo! UID: ${authResult.user?.uid}")
                    Toast.makeText(this, "Bem-vindo de volta! 👋", Toast.LENGTH_SHORT).show()

                    startActivity(Intent(this, MainActivity::class.java))
                    finish()
                }
                .addOnFailureListener { excecao ->
                    Log.e("LOGIN_FIREBASE_ERRO", "Erro ao logar", excecao)
                    Toast.makeText(this, "Erro no login: ${excecao.message}", Toast.LENGTH_LONG).show()
                }
        }
    }
}