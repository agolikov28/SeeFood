package com.example.seefoodapp

import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class MyDietActivity : AppCompatActivity() {

    private lateinit var scanLabelCamera: Button
    private lateinit var scanLabelImage: Button
    private lateinit var clearDiet: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.my_diet_activity)
    }
}