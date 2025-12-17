package com.example.kidslearning

import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.animation.AccelerateDecelerateInterpolator
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import com.example.kidslearning.adapter.ViewPagerAdapter
import com.example.kidslearning.databinding.ActivityMainBinding
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class MainActivity : AppCompatActivity() {

    fun setSwipeEnabled(enabled: Boolean) {
        binding.viewPager.isUserInputEnabled = enabled
    }

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val fragments = listOf(FrenchFragment(), ArabicFragment(), LetterTracingFragment())

        // Titres avec emojis pour les kids
        val titles = listOf("🇫🇷 Français", "🇸🇦 العربية", "✏️ Tracer")

        binding.viewPager.adapter = ViewPagerAdapter(this, fragments)

        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            tab.text = titles[position]
        }.attach()

        // Récupérer la page sélectionnée depuis SelectionActivity
        val selectedPage = intent.getIntExtra("SELECTED_PAGE", 0)
        binding.viewPager.setCurrentItem(selectedPage, false)

        // Désactiver le swipe entre les fragments
        binding.viewPager.isUserInputEnabled = false

        // Setup du bouton retour
        setupBackButton()

        // Animations et customisation des tabs
        setupTabAnimations()

        // Animation du titre principal
        animateAppTitle()

        // Listener pour animer les tabs au changement
        binding.tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                tab?.view?.let { tabView ->
                    // Animation de bounce quand on sélectionne un tab
                    tabView.animate()
                        .scaleX(1.1f)
                        .scaleY(1.1f)
                        .setDuration(200)
                        .withEndAction {
                            tabView.animate()
                                .scaleX(1f)
                                .scaleY(1f)
                                .setDuration(200)
                                .start()
                        }
                        .start()
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
                tab?.view?.let { tabView ->
                    tabView.animate()
                        .scaleX(0.95f)
                        .scaleY(0.95f)
                        .setDuration(150)
                        .withEndAction {
                            tabView.scaleX = 1f
                            tabView.scaleY = 1f
                        }
                        .start()
                }
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {
                // Animation de shake quand on re-clique sur le même tab
                tab?.view?.let { tabView ->
                    ObjectAnimator.ofFloat(tabView, "rotation", 0f, -5f, 5f, -3f, 3f, 0f).apply {
                        duration = 400
                        start()
                    }
                }
            }
        })
    }

    private fun setupBackButton() {
        val btnBack = findViewById<CardView>(R.id.btnBack)

        btnBack.setOnClickListener {
            // Animation du bouton
            it.animate()
                .scaleX(0.9f)
                .scaleY(0.9f)
                .setDuration(100)
                .withEndAction {
                    it.animate()
                        .scaleX(1f)
                        .scaleY(1f)
                        .setDuration(100)
                        .withEndAction {
                            // Retour vers SelectionActivity
                            val intent = Intent(this, SelectionActivity::class.java)
                            startActivity(intent)
                            finish()
                            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
                        }
                        .start()
                }
                .start()
        }
    }

    private fun setupTabAnimations() {
        // Animation d'entrée pour chaque tab
        binding.tabLayout.post {
            for (i in 0 until binding.tabLayout.tabCount) {
                val tab = binding.tabLayout.getTabAt(i)
                tab?.view?.let { tabView ->
                    tabView.alpha = 0f
                    tabView.translationY = -50f
                    tabView.animate()
                        .alpha(1f)
                        .translationY(0f)
                        .setDuration(400)
                        .setStartDelay((i * 100).toLong())
                        .setInterpolator(AccelerateDecelerateInterpolator())
                        .start()
                }
            }
        }
    }

    private fun animateAppTitle() {
        val titleView = findViewById<TextView>(R.id.txtAppTitle)

        // Animation de rotation subtile
        ObjectAnimator.ofFloat(titleView, "rotation", -2f, 2f).apply {
            duration = 2000
            repeatCount = ObjectAnimator.INFINITE
            repeatMode = ObjectAnimator.REVERSE
            start()
        }

        // Animation de changement de couleur arc-en-ciel
        val rainbowColors = intArrayOf(
            Color.parseColor("#FF6B6B"), // Rouge
            Color.parseColor("#FFA07A"), // Saumon
            Color.parseColor("#F7DC6F"), // Jaune
            Color.parseColor("#52B788"), // Vert
            Color.parseColor("#45B7D1"), // Bleu
            Color.parseColor("#BB8FCE")  // Violet
        )

        ValueAnimator.ofArgb(*rainbowColors).apply {
            duration = 6000
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
}