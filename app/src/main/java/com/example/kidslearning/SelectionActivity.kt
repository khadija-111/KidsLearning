package com.example.kidslearning

import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.OvershootInterpolator
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView

class SelectionActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_selection)

        val cardFrench = findViewById<CardView>(R.id.cardFrench)
        val cardArabic = findViewById<CardView>(R.id.cardArabic)
        val cardTracing = findViewById<CardView>(R.id.cardTracing)

        // Animations d'entrée
        animateEntrance()

        // Animation du titre
        animateTitleContinuously()

        // Click listeners avec animations
        cardFrench.setOnClickListener {
            animateCardClick(it)
            navigateToMainActivity(0) // Position 0 = Fragment Français
        }

        cardArabic.setOnClickListener {
            animateCardClick(it)
            navigateToMainActivity(1) // Position 1 = Fragment Arabe
        }

        cardTracing.setOnClickListener {
            animateCardClick(it)
            navigateToMainActivity(2) // Position 2 = Fragment Tracer
        }

        // Hover effects (animations continues)
        setupHoverEffects(cardFrench)
        setupHoverEffects(cardArabic)
        setupHoverEffects(cardTracing)
    }

    private fun animateEntrance() {
        // Animation du titre
        findViewById<TextView>(R.id.txtWelcomeTitle)?.apply {
            alpha = 0f
            scaleX = 0.3f
            scaleY = 0.3f
            animate()
                .alpha(1f)
                .scaleX(1f)
                .scaleY(1f)
                .setDuration(700)
                .setInterpolator(OvershootInterpolator())
                .start()
        }

        // Animation des cards avec délai progressif
        val cards = listOf(
            findViewById<CardView>(R.id.cardFrench),
            findViewById<CardView>(R.id.cardArabic),
            findViewById<CardView>(R.id.cardTracing)
        )

        cards.forEachIndexed { index, card ->
            card?.apply {
                alpha = 0f
                translationX = -300f
                rotation = -10f
                animate()
                    .alpha(1f)
                    .translationX(0f)
                    .rotation(0f)
                    .setDuration(600)
                    .setStartDelay((index * 150 + 300).toLong())
                    .setInterpolator(OvershootInterpolator())
                    .start()
            }
        }
    }

    private fun animateTitleContinuously() {
        val titleView = findViewById<TextView>(R.id.txtWelcomeTitle) ?: return

        // Animation de rotation subtile
        ObjectAnimator.ofFloat(titleView, "rotation", -2f, 2f).apply {
            duration = 2000
            repeatCount = ObjectAnimator.INFINITE
            repeatMode = ObjectAnimator.REVERSE
            start()
        }

        // Animation arc-en-ciel
        val rainbowColors = intArrayOf(
            Color.parseColor("#FF6B6B"),
            Color.parseColor("#FFA07A"),
            Color.parseColor("#F7DC6F"),
            Color.parseColor("#4ECDC4"),
            Color.parseColor("#45B7D1"),
            Color.parseColor("#BB8FCE"),
            Color.parseColor("#FF6B6B")
        )

        ValueAnimator.ofArgb(*rainbowColors).apply {
            duration = 6000
            repeatCount = ValueAnimator.INFINITE
            addUpdateListener { animator ->
                titleView.setTextColor(animator.animatedValue as Int)
            }
            start()
        }

        // Animation de scale
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

    private fun setupHoverEffects(card: CardView) {
        // Animation subtile de levitation
        val floatAnimator = ValueAnimator.ofFloat(0f, -10f, 0f).apply {
            duration = 2000
            repeatCount = ValueAnimator.INFINITE
            addUpdateListener { animator ->
                card.translationY = animator.animatedValue as Float
            }
        }
        floatAnimator.start()

        // Rotation subtile
        val rotationAnimator = ObjectAnimator.ofFloat(card, "rotation", -1f, 1f).apply {
            duration = 3000
            repeatCount = ObjectAnimator.INFINITE
            repeatMode = ObjectAnimator.REVERSE
        }
        rotationAnimator.start()
    }

    private fun animateCardClick(view: View) {
        // Animation de pulse
        view.animate()
            .scaleX(0.95f)
            .scaleY(0.95f)
            .setDuration(100)
            .withEndAction {
                view.animate()
                    .scaleX(1.1f)
                    .scaleY(1.1f)
                    .setDuration(200)
                    .withEndAction {
                        view.animate()
                            .scaleX(1f)
                            .scaleY(1f)
                            .setDuration(200)
                            .start()
                    }
                    .start()
            }
            .start()

        // Animation de rotation
        ObjectAnimator.ofFloat(view, "rotation", 0f, 360f).apply {
            duration = 500
            interpolator = AccelerateDecelerateInterpolator()
            start()
        }

        // Animation de fade out avant transition
        view.animate()
            .alpha(0.7f)
            .setDuration(300)
            .setStartDelay(200)
            .start()
    }

    private fun navigateToMainActivity(selectedPage: Int) {
        // Attendre la fin de l'animation
        findViewById<View>(android.R.id.content).postDelayed({
            val intent = Intent(this, MainActivity::class.java)
            intent.putExtra("SELECTED_PAGE", selectedPage)
            startActivity(intent)
            finish()
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
        }, 500)
    }
}