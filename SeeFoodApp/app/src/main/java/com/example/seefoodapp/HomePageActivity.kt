package com.example.seefoodapp

import com.example.seefoodapp.R;
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat


class HomePageActivity : AppCompatActivity() {

    private lateinit var myDietButton: Button
    private lateinit var newDietButton: Button
    private lateinit var queryButton: ImageButton


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.homepage_activity)

        myDietButton = findViewById<Button>(R.id.currDietBtn)
        newDietButton = findViewById<Button>(R.id.newDietBtn)
        queryButton = findViewById(R.id.queryButton)

        val myDietIntent = Intent(this, MyDietActivity::class.java)
        val newDietIntent = Intent(this, NewDietActivity::class.java)

        myDietButton.setOnClickListener() {
            startActivity(myDietIntent)
        }

        newDietButton.setOnClickListener() {
            startActivity(newDietIntent)
        }

        queryButton.setOnClickListener() {
            val intent = Intent(this, PopUpActivity::class.java)
            intent.putExtra("popuptitle", "How it Works")
            intent.putExtra(
                "popuptext",
                "\nTo start, click on\n\"CREATE NEW DIET\"\n This will calculate your BMI and" +
                        " determine your daily macro intake\n\n" +
                        "If you previously created your diet, view it using" +
                        " \n\"MY CURRENT DIET\"\n"
            )
            intent.putExtra("popupbtn", "Start Tracking!")
            intent.putExtra("darkstatusbar", false)
            startActivity(intent)
        }

    }

}