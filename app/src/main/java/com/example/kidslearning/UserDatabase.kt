package com.example.kidslearning.data

import android.content.Context

class UserDatabase(context: Context) {

    private val prefs = context.getSharedPreferences("users", Context.MODE_PRIVATE)

    fun register(email: String, password: String, name: String): Boolean {
        if (prefs.contains(email)) return false  // déjà inscrit

        prefs.edit()
            .putString("$email-password", password)
            .putString("$email-name", name)
            .apply()

        return true
    }

    fun login(email: String, password: String): Boolean {
        val savedPass = prefs.getString("$email-password", null)
        return savedPass == password
    }

    fun isUserExists(email: String): Boolean {
        return prefs.contains("$email-password")
    }
}
