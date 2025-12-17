package com.example.kidslearning

import android.util.Log
import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.app.AlertDialog
import android.graphics.Color
import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.*
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import com.example.kidslearning.databinding.ActivityDrawingBinding
import com.example.kidslearning.model.Letter
import com.google.android.flexbox.FlexboxLayout
import org.json.JSONArray
import java.util.Locale

class LetterTracingFragment : Fragment() {

    private var _binding: ActivityDrawingBinding? = null
    private val binding get() = _binding!!

    private lateinit var tts: TextToSpeech
    private var isTtsReady = false

    private val allLetters = mutableListOf<Letter>()
    private var currentLetterIndex = 0
    private var isArabic = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = ActivityDrawingBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupLanguageButtons()

        loadLetters(if (isArabic) "letters_ar.json" else "letters_fr.json")

        initTTS()

        updateLetterDisplay()

        binding.btnPlay.setOnClickListener {
            tts.speak("Test", TextToSpeech.QUEUE_FLUSH, null, "test_id")

            if (!isTtsReady) return@setOnClickListener
            val sound = allLetters[currentLetterIndex].sound
            tts.speak(sound, TextToSpeech.QUEUE_FLUSH, null, "ID")
            animateLetter()
        }

        binding.btnClear.setOnClickListener {
            binding.myCanvas.clearCanvas()
            binding.myCanvas.drawLetterGuide(allLetters[currentLetterIndex].name)
        }

        binding.btnValidate.setOnClickListener {
            goNextLetter()
        }

        binding.btnShowAllLetters.setOnClickListener {
            showAllLettersDialog()
        }
    }

    private fun loadLetters(fileName: String) {
        allLetters.clear()
        try {
            val json = requireContext().assets.open(fileName).bufferedReader().readText()
            val arr = JSONArray(json)

            for (i in 0 until arr.length()) {
                val obj = arr.getJSONObject(i)
                allLetters.add(
                    Letter(
                        name = obj.getString("name"),
                        sound = obj.getString("sound")
                    )
                )
            }

        } catch (e: Exception) {
            Log.e("JSON", "Error: ${e.message}")
        }
    }

    private fun initTTS() {
        tts = TextToSpeech(requireContext()) { status ->
            if (status == TextToSpeech.SUCCESS) {

                val result = if (isArabic)
                    tts.setLanguage(Locale("ar"))
                else
                    tts.setLanguage(Locale.FRENCH)

                if (result == TextToSpeech.LANG_NOT_SUPPORTED) {
                    Log.e("TTS", "Language not supported")
                } else {
                    isTtsReady = true
                }
            }
        }
    }

    private fun setupLanguageButtons() {
        binding.btnFrenchLetters.setOnClickListener {
            if (isArabic) {
                isArabic = false
                switchLanguage()
            }
        }

        binding.btnArabicLetters.setOnClickListener {
            if (!isArabic) {
                isArabic = true
                switchLanguage()
            }
        }
        updateButtonsColors()
    }

    private fun switchLanguage() {
        loadLetters(if (isArabic) "letters_ar.json" else "letters_fr.json")

        tts.language = if (isArabic) Locale("ar") else Locale.FRENCH

        currentLetterIndex = 0
        updateLetterDisplay()
        updateButtonsColors()
    }

    private fun updateButtonsColors() {
        if (isArabic) {
            binding.btnArabicLetters.setCardBackgroundColor(Color.RED)
            binding.btnFrenchLetters.setCardBackgroundColor(Color.GRAY)
        } else {
            binding.btnFrenchLetters.setCardBackgroundColor(Color.GREEN)
            binding.btnArabicLetters.setCardBackgroundColor(Color.GRAY)
        }
    }

    private fun updateLetterDisplay() {
        val letter = allLetters[currentLetterIndex].name
        binding.txtLetterBig.text = letter
        binding.myCanvas.clearCanvas()
        binding.myCanvas.drawLetterGuide(letter)
    }

    private fun goNextLetter() {
        currentLetterIndex = (currentLetterIndex + 1) % allLetters.size
        updateLetterDisplay()
    }

    private fun showAllLettersDialog() {
        val layout = FlexboxLayout(requireContext()).apply {
            flexWrap = com.google.android.flexbox.FlexWrap.WRAP
        }

        allLetters.forEachIndexed { index, letter ->
            val card = CardView(requireContext()).apply {
                radius = 20f
                setCardBackgroundColor(Color.CYAN)
                layoutParams = FlexboxLayout.LayoutParams(120, 120).apply {
                    setMargins(10, 10, 10, 10)
                }
            }

            val txt = TextView(requireContext()).apply {
                text = letter.name
                textSize = 32f
                setTextColor(Color.WHITE)
                gravity = android.view.Gravity.CENTER
            }

            card.addView(txt)
            card.setOnClickListener {
                currentLetterIndex = index
                updateLetterDisplay()
            }

            layout.addView(card)
        }

        AlertDialog.Builder(requireContext())
            .setView(layout)
            .setNegativeButton("Close", null)
            .show()
    }

    private fun animateLetter() {
        binding.txtLetterBig.animate()
            .scaleX(1.2f).scaleY(1.2f)
            .setDuration(200)
            .withEndAction {
                binding.txtLetterBig.animate().scaleX(1f).scaleY(1f).duration = 200
            }.start()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        tts.shutdown()
        _binding = null
    }
}
