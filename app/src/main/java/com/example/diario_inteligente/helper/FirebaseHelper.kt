package com.example.diario_inteligente.helper

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

object FirebaseHelper {

    fun getAuth(): FirebaseAuth {
        return FirebaseAuth.getInstance()
    }

    fun getFirestore(): FirebaseFirestore {
        return FirebaseFirestore.getInstance()
    }
}