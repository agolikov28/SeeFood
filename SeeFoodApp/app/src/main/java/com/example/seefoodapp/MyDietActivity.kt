package com.example.seefoodapp

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.text.InputType
import android.util.Log
import android.widget.*
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.tasks.Task
import com.google.android.material.textfield.TextInputLayout
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.Text
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.latin.TextRecognizerOptions
import java.util.regex.Matcher
import java.util.regex.Pattern


class MyDietActivity : AppCompatActivity() {

    private lateinit var scanButton: Button
    private lateinit var editBtn: Button
    private lateinit var clearDietButton: Button
    private lateinit var carbBar: ProgressBar
    private lateinit var proteinBar: ProgressBar
    private lateinit var fatBar: ProgressBar
    private lateinit var calorieBar: ProgressBar
    private lateinit var image: InputImage
    private lateinit var startForResult : ActivityResultLauncher<Intent>
    private lateinit var sharedpreferences: SharedPreferences
    private var targetCarbs = 40
    private var targetProtein = 40
    private var targetFat = 40
    private var targetCalories = 40
    private var currCalories = 0
    private var currCarbs = 0
    private var currProtein = 0
    private var currFat = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        Log.i(TAG, "Entered on Create")
        super.onCreate(savedInstanceState)
        setContentView(R.layout.my_diet_activity)
        scanButton = findViewById(R.id.newLabelBtn)
        clearDietButton = findViewById(R.id.clearDietBtn)
        editBtn = findViewById(R.id.editBtn)
        carbBar = findViewById(R.id.carbBar)
        proteinBar = findViewById(R.id.proteinBar)
        calorieBar = findViewById(R.id.calorieBar)
        fatBar = findViewById(R.id.fatBar)

        sharedpreferences = getSharedPreferences("shared_preferences", Context.MODE_PRIVATE)
        targetCarbs = sharedpreferences.getInt("carbs", 0)
        targetProtein = sharedpreferences.getInt("proteins", 0)
        targetCalories = sharedpreferences.getInt("calories", 0)
        targetFat = sharedpreferences.getInt("fats", 0)

        startForResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult())
        { result: ActivityResult ->
            if (result.resultCode == Activity.RESULT_OK) {
                val selectedImage : Uri = result.data!!.data!!

                image = InputImage.fromFilePath(this, selectedImage)
                val task = processImage(image)

                task.addOnCompleteListener {
                    val macros = processFireBaseText(task.result)
                    var gFat = macros[0]
                    var gCarb = macros[1]
                    var gProtein = macros[2]
                    var calories = macros[3]

                    currCarbs += gCarb
                    currProtein += gProtein
                    currFat += gFat
                    currCalories += calories
                    updateBars()
                }
            }

        }

        scanButton.setOnClickListener {
            scanLabel()
            updateBars()
        }

        clearDietButton.setOnClickListener {
            clearDiet()
        }

        editBtn.setOnClickListener {
            editStats()
            updateBars()
        }

        updateBars()
    }

    private fun scanLabel() {
        Log.i(TAG, "Scan label button pressed")
        val isBitmap = false

        if (isBitmap){
            val bitmap: Bitmap = BitmapFactory.decodeResource(resources, R.drawable.sample_food_label)
            image = InputImage.fromBitmap(bitmap, 0)
        } else {
            val getIntent = Intent(Intent.ACTION_GET_CONTENT)
            getIntent.type = "image/*"

            val pickIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            pickIntent.type = "image/*"

            val chooserIntent = Intent.createChooser(getIntent, "Select Image")
            chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, arrayOf(pickIntent))

            startForResult.launch(chooserIntent)

        }

    }

    private fun clearDiet(){
        Log.i(TAG, "Clear diet button pressed")
        currCarbs = 0
        currProtein = 0
        currFat = 0
        currCalories = 0
        updateBars()
    }

    private fun editStats(){

        val alert = AlertDialog.Builder(this)
        alert.setTitle("Edit Target Macros")

        val layout = LinearLayout(this)
        layout.orientation = LinearLayout.VERTICAL

        val calorieBox = EditText(this)
        calorieBox.hint = "Target Calories"
        calorieBox.inputType = InputType.TYPE_CLASS_NUMBER
        layout.addView(calorieBox)

        val fatBox = EditText(this)
        fatBox.hint = "Target Grams of Fat"
        fatBox.inputType = InputType.TYPE_CLASS_NUMBER
        layout.addView(fatBox)

        val carbBox = EditText(this)
        carbBox.hint = "Target Grams of Carbohydrates"
        carbBox.inputType = InputType.TYPE_CLASS_NUMBER
        layout.addView(carbBox)

        val proteinBox = EditText(this)
        proteinBox.hint = "Target Grams of Protein"
        proteinBox.inputType = InputType.TYPE_CLASS_NUMBER
        layout.addView(proteinBox)


        alert.setView(layout)

        alert.setPositiveButton(
            "Save"
        ) { dialog, whichButton ->
            targetFat = fatBox.text.toString().toInt()
            targetCarbs = carbBox.text.toString().toInt()
            targetProtein = proteinBox.text.toString().toInt()
            targetCalories = calorieBox.text.toString().toInt()
            updateBars()
            Toast.makeText(this, "Saved Successfully", Toast.LENGTH_SHORT).show()
        }

        alert.setNegativeButton(
            "Cancel"
        ) { dialog, whichButton ->
            // what ever you want to do with No option.
        }

        alert.show()
    }


    private fun updateBars(){
        Log.i(TAG, "Bars Updated")
        carbBar.max = targetCarbs
        proteinBar.max = targetProtein
        fatBar.max = targetFat
        calorieBar.max = targetCalories

        carbBar.setProgress(currCarbs, true)
        proteinBar.setProgress(currProtein, true)
        fatBar.setProgress(currFat, true)
        calorieBar.setProgress(currCalories, true)
    }

    private fun processImage(image: InputImage) : Task<Text> {
        Log.i(TAG, "Entered process image")
        val textRecognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS)

        val result = textRecognizer.process(image)
            .addOnSuccessListener {
                //process success
                Toast.makeText(this, "Image Processed Successfully", Toast.LENGTH_SHORT)
                    .show()
            }
            .addOnFailureListener {
                //process failure
                Toast.makeText(this, it.localizedMessage, Toast.LENGTH_SHORT)
                    .show()
                Log.i(TAG, "Image Processing Result: ${it.localizedMessage}")
            }

        return result

    }

    private fun processFireBaseText(fText: Text): Array<Int> {

        val caloriePattern = Pattern.compile("Calories (\\d+)")
        var calorie = ""
        val fatPattern = Pattern.compile("Total Fat (\\d+)g")
        var fat = ""
        val carbPattern = Pattern.compile("Carbohydrate (\\d+)g")
        var carb = ""
        val proteinPattern = Pattern.compile("Protein (\\d+)g")
        var protein = ""

        Log.i(TAG, "Text: ${fText.text}}")

        for (tb in fText.textBlocks) {
            //get text in a block
            //read line by line
            for (l in tb.lines) {
                val calMatcher: Matcher = caloriePattern.matcher(l.text)
                val fMatcher: Matcher = fatPattern.matcher(l.text)
                val cMatcher: Matcher = carbPattern.matcher(l.text)
                val pMatcher: Matcher = proteinPattern.matcher(l.text)

                when {
                    fMatcher.find() -> {
                        fat = fMatcher.group(1)!!
                        Log.i(TAG, "Fat: $fat")
                    }
                    cMatcher.find() -> {
                        carb = cMatcher.group(1)!!
                        Log.i(TAG, "Carbohydrates: $carb")
                    }
                    pMatcher.find() -> {
                        protein = pMatcher.group(1)!!
                        Log.i(TAG, "Protein: $protein")
                    }
                    calMatcher.find() -> {
                        calorie = calMatcher.group(1)
                        Log.i(TAG, "Calories: $calorie")
                    }
                }
            }
        }

        var iFat : Int = 0
        var iCarb : Int = 0
        var iProtein : Int = 0
        var iCalorie : Int = 0

        if (fat != ""){
            iFat = fat.toInt()
        } else {
            //create dialog box prompting for manual fat input
            val alert = AlertDialog.Builder(this)
            alert.setTitle("Input Grams of Fat")

            val layout = LinearLayout(this)
            layout.orientation = LinearLayout.VERTICAL

            val fatBox = EditText(this)
            fatBox.hint = "Grams of Fat on Label"
            fatBox.inputType = InputType.TYPE_CLASS_NUMBER
            layout.addView(fatBox)
            alert.setView(layout)

            alert.setPositiveButton(
                "Save"
            ) { dialog, whichButton ->
                iFat = fatBox.text.toString().toInt()
                currFat += iFat
                updateBars()
                Toast.makeText(this, "$iFat Grams of Fat Inputted Successfully", Toast.LENGTH_SHORT).show()
            }

            alert.setNegativeButton(
                "Cancel"
            ) { dialog, whichButton ->
                // what ever you want to do with No option.
                iFat = 0
            }

            alert.show()
        }
        if (carb != ""){
            iCarb = carb.toInt()
        } else {
            //create dialog box prompting for manual carb input
            val alert = AlertDialog.Builder(this)
            alert.setTitle("Input Grams of Carbohydrates")

            val layout = LinearLayout(this)
            layout.orientation = LinearLayout.VERTICAL

            val carbBox = EditText(this)
            carbBox.hint = "Grams of Carbohydrates on Label"
            carbBox.inputType = InputType.TYPE_CLASS_NUMBER
            layout.addView(carbBox)
            alert.setView(layout)

            alert.setPositiveButton(
                "Save"
            ) { dialog, whichButton ->
                iCarb = carbBox.text.toString().toInt()
                currCarbs += iCarb
                updateBars()
                Toast.makeText(this, "$iCarb Grams of Carbohydrates Inputted Successfully", Toast.LENGTH_SHORT).show()
            }

            alert.setNegativeButton(
                "Cancel"
            ) { dialog, whichButton ->
                // what ever you want to do with No option.
                iCarb = 0
            }

            alert.show()
        }
        if (protein != ""){
            iProtein = protein.toInt()
        } else {
            val alert = AlertDialog.Builder(this)
            alert.setTitle("Input Grams of Protein")

            val layout = LinearLayout(this)
            layout.orientation = LinearLayout.VERTICAL

            val proteinBox = EditText(this)
            proteinBox.hint = "Grams of Protein on Label"
            proteinBox.inputType = InputType.TYPE_CLASS_NUMBER
            layout.addView(proteinBox)
            alert.setView(layout)

            alert.setPositiveButton(
                "Save"
            ) { dialog, whichButton ->
                iProtein= proteinBox.text.toString().toInt()
                currProtein += iProtein
                updateBars()
                Toast.makeText(this, "$iProtein Grams of Protein Inputted Successfully", Toast.LENGTH_SHORT).show()
            }

            alert.setNegativeButton(
                "Cancel"
            ) { dialog, whichButton ->
                // what ever you want to do with No option.
                iProtein = 0
            }

            alert.show()
        }

        if (calorie != ""){
            iCalorie = calorie.toInt()
        } else {
            val alert = AlertDialog.Builder(this)
            alert.setTitle("Input Number of Calories")

            val layout = LinearLayout(this)
            layout.orientation = LinearLayout.VERTICAL

            val calorieBox = EditText(this)
            calorieBox.hint = "Number of Calories on Label"
            calorieBox.inputType = InputType.TYPE_CLASS_NUMBER
            layout.addView(calorieBox)
            alert.setView(layout)

            alert.setPositiveButton(
                "Save"
            ) { dialog, whichButton ->
                iCalorie= calorieBox.text.toString().toInt()
                currCalories += iCalorie
                updateBars()
                Toast.makeText(this, "$iCalorie Calories Inputted Successfully", Toast.LENGTH_SHORT).show()
            }

            alert.setNegativeButton(
                "Cancel"
            ) { dialog, whichButton ->
                // what ever you want to do with No option.
                iProtein = 0
            }

            alert.show()
        }

        return arrayOf(iFat, iCarb, iProtein, iCalorie)
    }

    companion object{
        const val TAG = "SeeFood-MyDietActivity"
    }


}