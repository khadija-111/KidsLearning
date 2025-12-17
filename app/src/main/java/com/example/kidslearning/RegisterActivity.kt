package com.example.kidslearning

import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.OvershootInterpolator
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import com.example.kidslearning.data.UserDatabase
import com.example.kidslearning.databinding.ActivityRegisterBinding

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding
    private lateinit var db: UserDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val goToLogin = findViewById<TextView>(R.id.goToLogin)
        goToLogin.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }

        db = UserDatabase(this)

        // Animations
        animateEntrance()
        animateTitleContinuously()
        setupInputAnimations()

        binding.registerBtn.setOnClickListener {

            // Animation bouton
            animateButton(it)

            val name = binding.nameInput.text.toString()
            val email = binding.registerEmail.text.toString()
            val pass = binding.registerPassword.text.toString()

            // Champs vides
            if (name.isEmpty() || email.isEmpty() || pass.isEmpty()) {
                showCustomToast("⚠️ Remplissez tous les champs !", "#FF9800")
                shakeView(binding.nameInput)
                shakeView(binding.registerEmail)
                shakeView(binding.registerPassword)
                return@setOnClickListener
            }

            // Email deja kayn
            if (db.isUserExists(email)) {
                showCustomToast("❌ Cet email existe déjà !", "#F44336")
                shakeView(binding.registerEmail)
                return@setOnClickListener
            }

            // Inscription OK
            if (db.register(email, pass, name)) {
                showCustomToast("🎉 Inscription réussie ! Bienvenue $name !", "#4CAF50")

                // Animation success
                binding.registerBtn.animate()
                    .scaleX(1.2f)
                    .scaleY(1.2f)
                    .alpha(0.5f)
                    .setDuration(300)
                    .withEndAction {
                        startActivity(Intent(this, MainActivity::class.java))
                        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
                        finish()

                    }
                    .start()
            }
        }
    }


    private fun animateEntrance() {
        // Animation du titre
        findViewById<TextView>(R.id.txtRegisterTitle)?.apply {
            alpha = 0f
            scaleX = 0.3f
            scaleY = 0.3f
            animate()
                .alpha(1f)
                .scaleX(1f)
                .scaleY(1f)
                .setDuration(600)
                .setInterpolator(OvershootInterpolator())
                .start()
        }

        // Animation de la card principale
        binding.nameInput.parent.parent.let { cardView ->
            if (cardView is CardView) {
                cardView.alpha = 0f
                cardView.translationY = 100f
                cardView.animate()
                    .alpha(1f)
                    .translationY(0f)
                    .setDuration(500)
                    .setStartDelay(200)
                    .setInterpolator(AccelerateDecelerateInterpolator())
                    .start()
            }
        }

        // Animation du bouton S'inscrire
        binding.registerBtn.parent.let { cardView ->
            if (cardView is CardView) {
                cardView.alpha = 0f
                cardView.scaleX = 0.8f
                cardView.scaleY = 0.8f
                cardView.animate()
                    .alpha(1f)
                    .scaleX(1f)
                    .scaleY(1f)
                    .setDuration(500)
                    .setStartDelay(400)
                    .setInterpolator(OvershootInterpolator())
                    .start()
            }
        }
    }

    private fun animateTitleContinuously() {
        val titleView = findViewById<TextView>(R.id.txtRegisterTitle) ?: return

        // Animation de rotation subtile
        ObjectAnimator.ofFloat(titleView, "rotation", -2f, 2f).apply {
            duration = 2000
            repeatCount = ObjectAnimator.INFINITE
            repeatMode = ObjectAnimator.REVERSE
            start()
        }

        // Animation de changement de couleur
        val colors = intArrayOf(
            Color.parseColor("#FF6B6B"),
            Color.parseColor("#4ECDC4"),
            Color.parseColor("#45B7D1"),
            Color.parseColor("#FFA07A"),
            Color.parseColor("#FF6B6B")
        )

        ValueAnimator.ofArgb(*colors).apply {
            duration = 5000
            repeatCount = ValueAnimator.INFINITE
            addUpdateListener { animator ->
                titleView.setTextColor(animator.animatedValue as Int)
            }
            start()
        }

        // Animation de scale subtile
        ValueAnimator.ofFloat(1f, 1.05f).apply {
            duration = 1500
            repeatCount = ValueAnimator.INFINITE
            repeatMode = ValueAnimator.REVERSE
            addUpdateListener { animator ->
                val scale = animator.animatedValue as Float
                titleView.scaleX = scale
                titleView.scaleY = scale
            }
            start()
        }
    }

    private fun setupInputAnimations() {
        // Animation quand on focus sur un input
        val inputs = listOf(binding.nameInput, binding.registerEmail, binding.registerPassword)

        inputs.forEach { input ->
            input.setOnFocusChangeListener { view, hasFocus ->
                val cardView = view.parent as? CardView
                cardView?.animate()
                    ?.scaleX(if (hasFocus) 1.02f else 1f)
                    ?.scaleY(if (hasFocus) 1.02f else 1f)
                    ?.setDuration(200)
                    ?.start()

                // Changer la couleur du background
                if (hasFocus) {
                    cardView?.setCardBackgroundColor(Color.parseColor("#E3F2FD"))
                } else {
                    cardView?.setCardBackgroundColor(Color.parseColor("#F8F8F8"))
                }
            }
        }
    }

    private fun animateButton(view: android.view.View) {
        view.animate()
            .scaleX(0.95f)
            .scaleY(0.95f)
            .setDuration(100)
            .withEndAction {
                view.animate()
                    .scaleX(1f)
                    .scaleY(1f)
                    .setDuration(100)
                    .start()
            }
            .start()
    }

    private fun shakeView(view: android.view.View) {
        ObjectAnimator.ofFloat(view, "translationX", 0f, -25f, 25f, -20f, 20f, -10f, 10f, 0f).apply {
            duration = 500
            start()
        }

        // Changer temporairement la couleur en rouge
        val cardView = view.parent as? CardView
        val originalColor = Color.parseColor("#F8F8F8")
        val errorColor = Color.parseColor("#FFEBEE")

        cardView?.setCardBackgroundColor(errorColor)
        view.postDelayed({
            cardView?.setCardBackgroundColor(originalColor)
        }, 500)
    }

    private fun showCustomToast(message: String, colorHex: String) {
        val toast = Toast.makeText(this, message, Toast.LENGTH_LONG)
        toast.show()
    }
}