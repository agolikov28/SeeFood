package com.example.seefoodapp

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity

class NewDietActivity : AppCompatActivity() {

    private lateinit var mName: EditText
    private lateinit var mWeight: EditText
    private lateinit var mHeight: EditText
    private lateinit var saveButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.new_diet_activity)

        saveButton = findViewById<Button>(R.id.saveBtn)
        mName = findViewById<EditText>(R.id.nameText)
        mWeight = findViewById<EditText>(R.id.weightText)
        mHeight = findViewById<EditText>(R.id.heightText)


        val myDietIntent = Intent(this, MyDietActivity::class.java)

        saveButton.setOnClickListener() {
            startActivity(myDietIntent)
        }
    }

}