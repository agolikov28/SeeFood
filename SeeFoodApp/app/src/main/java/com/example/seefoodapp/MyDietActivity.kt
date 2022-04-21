package com.example.seefoodapp

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.widget.Button
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.tasks.Task
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.Text
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.latin.TextRecognizerOptions
import java.util.regex.Matcher
import java.util.regex.Pattern
import kotlin.coroutines.CoroutineContext


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

    private fun scanLabel() {

        val bitmap: Bitmap = BitmapFactory.decodeResource(resources, R.drawable.sample_food_label)
        val task = processImage(bitmap)
        task.addOnCompleteListener {
            val macros = processFireBaseText(task.result)
            var gFat = macros[0]
            var gCarb = macros[1]
            var gProtein = macros[2]

            currCarbs += gCarb
            currProtein += gProtein
            currFat += gFat
            updateBars()
        }
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

    private fun processImage(bitmap: Bitmap) : Task<Text> {
        val image = InputImage.fromBitmap(bitmap, 0)
        val textRecognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS)

        val result = textRecognizer.process(image)
            .addOnSuccessListener {
                //process success
                Toast.makeText(this, "Image Processed Successfully", Toast.LENGTH_SHORT)
                    .show()
            }
            .addOnFailureListener {
                //process failure
                Toast.makeText(this, "${it.localizedMessage}", Toast.LENGTH_SHORT)
                    .show()
                Log.i(TAG, "Image Processing Result: ${it.localizedMessage}")
            }

        return result

    }

    private fun processFireBaseText(fText: Text): Array<Int> {

        val fatPattern = Pattern.compile("Total Fat (\\d+)g")
        var fat = ""
        val carbPattern = Pattern.compile("Total Carbohydrate (\\d+)g")
        var carb = ""
        val proteinPattern = Pattern.compile("Protein (\\d+)g")
        var protein = ""

        Log.i(TAG, "Text: ${fText.text}}")

        for (tb in fText.textBlocks) {
            //get text in a block
            //read line by line
            for (l in tb.lines) {
                val fMatcher: Matcher = fatPattern.matcher(l.text)
                val cMatcher: Matcher = carbPattern.matcher(l.text)
                val pMatcher: Matcher = proteinPattern.matcher(l.text)

                when {
                    fMatcher.find() -> {
                        fat = fMatcher.group(1)
                        Log.i(TAG, "Fat: $fat")
                    }
                    cMatcher.find() -> {
                        carb = cMatcher.group(1)
                        Log.i(TAG, "Carbohydrates: $carb")
                    }
                    pMatcher.find() -> {
                        protein = pMatcher.group(1)
                        Log.i(TAG, "Protein: $protein")
                    }
                }
            }
        }

        val iFat = fat.toInt()
        val iCarb = carb.toInt()
        val iProtein = protein.toInt()

        return arrayOf(iFat, iCarb, iProtein)
    }

    companion object{
        const val TAG = "SeeFood-MyDietActivity"
    }


}