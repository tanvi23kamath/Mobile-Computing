package com.example.guiapp

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView

class MainActivity : AppCompatActivity() {

    var ch = 1
    var font = 30f

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val t: TextView = findViewById(R.id.textView)
        val b1: Button = findViewById(R.id.button1)
        val b2: Button = findViewById(R.id.button2)

        b1.setOnClickListener {
            t.textSize = font
            font += 5
            if (font == 50f) font = 30f
        }

        b2.setOnClickListener {
            when (ch) {
                1 -> t.setTextColor(Color.RED)
                2 -> t.setTextColor(Color.GREEN)
                3 -> t.setTextColor(Color.BLUE)
                4 -> t.setTextColor(Color.CYAN)
                5 -> t.setTextColor(Color.YELLOW)
                6 -> t.setTextColor(Color.MAGENTA)
            }
            ch++
            if (ch == 7) ch = 1
        }
    }
}