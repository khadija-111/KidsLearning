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
import com.example.kidslearning.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var db: UserDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        db = UserDatabase(this)

        // Animations d'entrée
        animateEntrance()

        // Animation du titre
        animateTitleContinuously()

        // Setup input animations
        setupInputAnimations()

        binding.loginBtn.setOnClickListener {
            // Animation du bouton
            animateButton(it)

            val email = binding.emailInput.text.toString()
            val pass = binding.passwordInput.text.toString()

            // Check fields
            if (email.isEmpty() || pass.isEmpty()) {
                showCustomToast("⚠️ Remplissez tous les champs !")
                shakeView(binding.emailInput)
                shakeView(binding.passwordInput)
                return@setOnClickListener
            }

            // Check if user exists
            if (!db.isUserExists(email)) {
                showCustomToast("❌ Email non trouvé, créez un compte !")
                shakeView(binding.emailInput)
                return@setOnClickListener
            }

            // Check password
            if (db.login(email, pass)) {
                showCustomToast("🎉 Connexion réussie ! Bienvenue !")

                // Animation de succès
                successAnimation()
            } else {
                showCustomToast("🔒 Mot de passe incorrect !")
                shakeView(binding.passwordInput)
            }
        }

        binding.goToRegister.setOnClickListener {
            // Animation du bouton
            animateButton(it)

            startActivity(Intent(this, RegisterActivity::class.java))
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
        }
    }

    private fun animateEntrance() {
        // Animation du titre
        findViewById<TextView>(R.id.txtLoginTitle)?.apply {
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

        // Animation de la card des inputs
        binding.emailInput.parent.parent.let { cardView ->
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

        // Animation du bouton Se connecter
        binding.loginBtn.parent.let { cardView ->
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

        // Animation du bouton Créer un compte
        binding.goToRegister.apply {
            alpha = 0f
            translationY = 50f
            animate()
                .alpha(1f)
                .translationY(0f)
                .setDuration(500)
                .setStartDelay(600)
                .setInterpolator(AccelerateDecelerateInterpolator())
                .start()
        }
    }

    private fun animateTitleContinuously() {
        val titleView = findViewById<TextView>(R.id.txtLoginTitle) ?: return

        // Animation de rotation subtile
        ObjectAnimator.ofFloat(titleView, "rotation", -2f, 2f).apply {
            duration = 2000
            repeatCount = ObjectAnimator.INFINITE
            repeatMode = ObjectAnimator.REVERSE
            start()
        }

        // Animation de changement de couleur
        val colors = intArrayOf(
            Color.parseColor("#4ECDC4"),
            Color.parseColor("#45B7D1"),
            Color.parseColor("#4CAF50"),
            Color.parseColor("#FFA07A"),
            Color.parseColor("#4ECDC4")
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
        val inputs = listOf(binding.emailInput, binding.passwordInput)

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

    private fun successAnimation() {
        // Animation du titre
        findViewById<TextView>(R.id.txtLoginTitle)?.let { titleView ->
            val originalText = titleView.text
            titleView.text = "🎊 Bienvenue ! 🎊"
            titleView.animate()
                .scaleX(1.3f)
                .scaleY(1.3f)
                .setDuration(300)
                .withEndAction {
                    titleView.postDelayed({
                        // Transition vers MainActivity
                        startActivity(Intent(this, SelectionActivity::class.java))
                        finish()
                        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)

                    }, 500)
                }
                .start()
        }

        // Animation du bouton
        binding.loginBtn.animate()
            .scaleX(1.2f)
            .scaleY(1.2f)
            .alpha(0.5f)
            .rotation(360f)
            .setDuration(500)
            .start()
    }

    private fun showCustomToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }
}