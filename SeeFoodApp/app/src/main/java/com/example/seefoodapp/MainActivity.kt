package com.example.seefoodapp

import com.example.seefoodapp.R;
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat


class MainActivity : AppCompatActivity() {

    private lateinit var myDietButton: Button
    private lateinit var newDietButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        myDietButton = findViewById<Button>(R.id.currDietBtn)
        newDietButton = findViewById<Button>(R.id.newDietBtn)

        val myDietIntent = Intent(this, MyDietActivity::class.java)
        val newDietIntent = Intent(this, NewDietActivity::class.java)

        myDietButton.setOnClickListener() {
            startActivity(myDietIntent)
        }

        newDietButton.setOnClickListener(){
            startActivity(newDietIntent)
        }

    }

}