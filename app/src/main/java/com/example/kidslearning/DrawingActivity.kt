package com.example.kidslearning

import android.os.Bundle
import android.speech.tts.TextToSpeech
import androidx.appcompat.app.AppCompatActivity
import com.example.kidslearning.databinding.ActivityDrawingBinding
import java.util.Locale

class DrawingActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDrawingBinding
    private lateinit var tts: TextToSpeech

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDrawingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val letter = intent.getStringExtra("letter") ?: "A"
        binding.txtLetterBig.text = letter

        tts = TextToSpeech(this) { status ->
            if (status == TextToSpeech.SUCCESS) {
                tts.language = if (letter[0].toInt() in 0x0600..0x06FF) Locale("ar") else Locale.FRENCH
            }
        }

        binding.myCanvas.drawLetterGuide(letter)

        binding.btnPlay.setOnClickListener { tts.speak(letter, TextToSpeech.QUEUE_FLUSH, null, null) }
        binding.btnClear.setOnClickListener {
            binding.myCanvas.clearCanvas()
            binding.myCanvas.drawLetterGuide(letter)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        tts.stop()
        tts.shutdown()
    }
}
