package com.example.seefoodapp

import android.os.Bundle
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity

class NewDietActivity : AppCompatActivity() {

    private lateinit var dietName: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.new_diet_activity)
    }

}