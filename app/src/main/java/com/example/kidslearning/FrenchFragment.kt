package com.example.kidslearning

import android.graphics.Color
import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import com.example.kidslearning.databinding.ActivityLettersBinding
import com.example.kidslearning.model.Letter
import com.google.android.flexbox.AlignContent
import com.google.android.flexbox.AlignItems
import com.google.android.flexbox.FlexWrap
import com.google.android.flexbox.FlexboxLayout
import com.google.android.flexbox.JustifyContent
import org.json.JSONArray
import java.util.Locale

class FrenchFragment : Fragment() {

    private var _binding: ActivityLettersBinding? = null
    private val binding get() = _binding!!
    private val letters = mutableListOf<Letter>()
    private lateinit var tts: TextToSpeech

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = ActivityLettersBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        tts = TextToSpeech(requireContext()) { status ->
            if (status == TextToSpeech.SUCCESS) tts.language = Locale.FRENCH
        }

        loadLetters("letters_fr.json")

        val container = binding.flexContainer as FlexboxLayout
        container.justifyContent = JustifyContent.CENTER
        container.alignItems = AlignItems.CENTER
        container.alignContent = AlignContent.CENTER
        container.flexWrap = FlexWrap.WRAP
        container.setBackgroundColor(Color.parseColor("#F5F5F5"))

        val modernColors = listOf(
            Color.parseColor("#FF6B6B"), Color.parseColor("#4ECDC4"),
            Color.parseColor("#45B7D1"), Color.parseColor("#FFA07A"),
            Color.parseColor("#98D8C8"), Color.parseColor("#F7DC6F"),
            Color.parseColor("#BB8FCE"), Color.parseColor("#85C1E2"),
            Color.parseColor("#F8B739"), Color.parseColor("#52B788"),
            Color.parseColor("#E63946"), Color.parseColor("#457B9D")
        )

        letters.forEachIndexed { index, letter ->

            val cardView = CardView(requireContext()).apply {
                radius = 40f
                cardElevation = 16f
                setCardBackgroundColor(modernColors[index % modernColors.size])

                setOnClickListener {
                    animate().scaleX(0.92f).scaleY(0.92f)
                        .setDuration(100).withEndAction {
                            animate().scaleX(1.0f).scaleY(1.0f)
                                .setDuration(100).start()
                        }.start()

                    tts.speak(letter.name, TextToSpeech.QUEUE_FLUSH, null, null)
                }

                val size = 180
                val params = FlexboxLayout.LayoutParams(size, size)
                params.setMargins(20, 20, 20, 20)
                layoutParams = params
            }

            val frameLayout = FrameLayout(requireContext()).apply {
                layoutParams = ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT
                )
            }

            val textView = TextView(requireContext()).apply {
                text = letter.name
                textSize = 64f
                setTextColor(Color.WHITE)
                gravity = Gravity.CENTER
                typeface = android.graphics.Typeface.create(
                    android.graphics.Typeface.DEFAULT,
                    android.graphics.Typeface.BOLD
                )
                setShadowLayer(6f, 3f, 3f, Color.parseColor("#50000000"))

                val textParams = FrameLayout.LayoutParams(
                    FrameLayout.LayoutParams.MATCH_PARENT,
                    FrameLayout.LayoutParams.MATCH_PARENT
                )
                textParams.gravity = Gravity.CENTER
                layoutParams = textParams
                includeFontPadding = false
            }

            frameLayout.addView(textView)
            cardView.addView(frameLayout)
            container.addView(cardView)

            cardView.alpha = 0f
            cardView.scaleX = 0.3f
            cardView.scaleY = 0.3f
            cardView.animate()
                .alpha(1f)
                .scaleX(1f)
                .scaleY(1f)
                .setDuration(350)
                .setStartDelay((index * 50).toLong())
                .start()
        }
    }

    private fun loadLetters(fileName: String) {
        val json = requireContext().assets.open(fileName).bufferedReader().use { it.readText() }
        val arr = JSONArray(json)
        for (i in 0 until arr.length()) {
            val obj = arr.getJSONObject(i)
            letters.add(Letter(obj.getString("name"), obj.getString("name")))

        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        tts.stop()
        tts.shutdown()
        _binding = null
    }
}
