package com.example.kidslearning.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.kidslearning.databinding.ItemLetterBinding
import com.example.kidslearning.model.Letter
import android.graphics.Color

class LettersAdapter(
    private val letters: List<Letter>,
    private val onClick: (Letter) -> Unit
) : RecyclerView.Adapter<LettersAdapter.Holder>() {

    inner class Holder(val binding: ItemLetterBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        Holder(ItemLetterBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun onBindViewHolder(holder: Holder, position: Int) {
        val letter = letters[position]

        holder.binding.txtLetter.apply {
            text = letter.name
            textSize = 48f
            setTextColor(Color.WHITE)


            setShadowLayer(8f, 4f, 4f, Color.DKGRAY)

            val colors = listOf(
                Color.parseColor("#F44336"),
                Color.parseColor("#E91E63"),
                Color.parseColor("#9C27B0"),
                Color.parseColor("#3F51B5"),
                Color.parseColor("#2196F3"),
                Color.parseColor("#4CAF50"),
                Color.parseColor("#FF9800"),
                Color.parseColor("#FFEB3B"),
                Color.parseColor("#795548")
            )
            setBackgroundColor(colors[position % colors.size])

            setOnClickListener { onClick(letter) }
        }
    }


    override fun getItemCount() = letters.size
}
