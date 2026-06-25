package com.example.diario_inteligente.ui

import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.diario_inteligente.R
import com.example.diario_inteligente.databinding.ActivityProfileBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class ProfileActivity : AppCompatActivity() {

    private lateinit var binding: ActivityProfileBinding

    private val auth = FirebaseAuth.getInstance()
    private val db = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        carregarDados()

        binding.btnSalvarPerfil.setOnClickListener {
            salvarNome()
        }

    }

    private fun carregarDados() {

        val uid = auth.currentUser?.uid ?: return

        db.collection("users")
            .document(uid)
            .get()
            .addOnSuccessListener { document ->

                binding.edtNome.setText(
                    document.getString("name")
                )

                binding.edtEmail.setText(
                    document.getString("email")
                )
            }
    }

    private fun salvarNome() {

        val uid = auth.currentUser?.uid ?: return

        val novoNome =
            binding.edtNome.text.toString().trim()

        db.collection("users")
            .document(uid)
            .update("name", novoNome)
            .addOnSuccessListener {

                Toast.makeText(
                    this,
                    "Perfil atualizado!",
                    Toast.LENGTH_SHORT
                ).show()
            }
    }
}