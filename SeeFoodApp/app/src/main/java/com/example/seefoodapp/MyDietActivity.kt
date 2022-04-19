package com.example.seefoodapp

import android.os.Bundle
import android.widget.Button
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity

class MyDietActivity : AppCompatActivity() {

    private lateinit var scanButton: Button
    private lateinit var statsButton: Button
    private lateinit var clearDietButton: Button
    private lateinit var carbBar: ProgressBar
    private lateinit var proteinBar: ProgressBar
    private lateinit var fatBar: ProgressBar
    private var targetCarbs = 80
    private var targetProtein = 35
    private var targetFat = 15
    private var targetCalories = 1600
    private var currCarbs = 0
    private var currProtein = 0
    private var currFat = 0
    private var currCalories = 0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.my_diet_activity)
        scanButton = findViewById(R.id.newLabelBtn)
        clearDietButton = findViewById(R.id.clearDietBtn)
        statsButton = findViewById(R.id.statsBtn)
        carbBar = findViewById(R.id.macro1Bar)
        proteinBar = findViewById(R.id.macro2Bar)
        fatBar = findViewById(R.id.macro3Bar)

        scanButton.setOnClickListener(){
            scanLabel()
            updateBars()
        }

        clearDietButton.setOnClickListener(){
            clearDiet()
        }

        statsButton.setOnClickListener(){
            pullUpStats()
        }

        updateBars()
    }


    fun scanLabel(){
        var gCarb = 35
        var gProtein = 8
        var gFat = 3

        currCarbs += gCarb
        currProtein += gProtein
        currFat += gFat

    }

    fun clearDiet(){
        currCarbs = 0
        currProtein = 0
        currFat = 0
        updateBars()
    }

    fun pullUpStats(){

    }

    fun updateBars(){
        carbBar.max = targetCarbs
        proteinBar.max = targetFat
        fatBar.max = targetFat

        carbBar.setProgress(currCarbs, true)
        proteinBar.setProgress(currProtein, true)
        fatBar.setProgress(currFat, true)

    }
}