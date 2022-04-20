package com.example.seefoodapp

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.tasks.Task
import com.google.firebase.ml.vision.FirebaseVision
import com.google.firebase.ml.vision.common.FirebaseVisionImage
import com.google.firebase.ml.vision.text.FirebaseVisionText
import java.util.regex.Matcher
import java.util.regex.Pattern


class MyDietActivity : AppCompatActivity() {

    private lateinit var scanButton: Button
    private lateinit var statsButton: Button
    private lateinit var clearDietButton: Button
    private lateinit var carbBar: ProgressBar
    private lateinit var proteinBar: ProgressBar
    private lateinit var fatBar: ProgressBar
    private lateinit var calorieBar: ProgressBar
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
        calorieBar = findViewById(R.id.calorieBar)

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


    private fun scanLabel(){

        val bitmap: Bitmap = BitmapFactory.decodeResource(resources, R.drawable.sample_food_label)
        val macros = processImage(bitmap)

        Log.i(TAG, "Macros in scanLabel Method: $macros")
        var gCarb = 35
        var gProtein = 8
        var gFat = 3

        currCarbs += gCarb
        currProtein += gProtein
        currFat += gFat

    }

    private fun clearDiet(){
        currCarbs = 0
        currProtein = 0
        currFat = 0
        currCalories = 0
        updateBars()
    }

    private fun pullUpStats(){

    }

    private fun updateBars(){
        carbBar.max = targetCarbs
        proteinBar.max = targetProtein
        fatBar.max = targetFat
        calorieBar.max = targetCalories

        carbBar.setProgress(currCarbs, true)
        proteinBar.setProgress(currProtein, true)
        fatBar.setProgress(currFat, true)
        calorieBar.setProgress(currCalories, true)


    }

    private fun processImage(bitmap: Bitmap) : Array<Int> {
        var macros : Array<Int> = arrayOf(0)
        val image = FirebaseVisionImage.fromBitmap(bitmap)
        val textRecognizer = FirebaseVision.getInstance().onDeviceTextRecognizer

        val result: Task<FirebaseVisionText> = textRecognizer.processImage(image)
            .addOnSuccessListener {
                //process success
                Toast.makeText(this, "Image Processed Successfully", Toast.LENGTH_SHORT)
                    .show()
                macros = processFireBaseText(it)
            }
            .addOnFailureListener {
                //process failure
                Toast.makeText(this, "Failed to Process Image: $it", Toast.LENGTH_SHORT)
                    .show()
            }

        Log.i(TAG, "Image Processing Result: $result")


        return macros
    }

    private fun processFireBaseText(fText: FirebaseVisionText) : Array<Int> {

        val calPattern = Pattern.compile("^(Calories: )(%d)?$")

        for (tb in fText.textBlocks) {
            //get text in a block
            tb.text
            //read line by line
            for (l in tb.lines) {
                val matcher: Matcher = calPattern.matcher(l.text)
                if(matcher.find()){

                }

            }
        }

        return arrayOf(0)

    }

    companion object{
        const val TAG = "SeeFood-MyDietActivity"
    }


}