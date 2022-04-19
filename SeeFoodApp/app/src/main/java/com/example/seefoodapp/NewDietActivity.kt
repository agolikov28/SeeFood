package com.example.seefoodapp

import android.content.Intent
import android.graphics.Color
import android.graphics.Paint
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity


class NewDietActivity : AppCompatActivity() {
    //    private val CENTIMETERS_IN_METER = 100
    private val INCHES_IN_FOOT = 12
    private val BMI_IMPERIAL_WEIGHT_SCALAR = 703

    private val BMI_UNDERWEIGHT = "Underweight"
    private val BMI_HEALTHY = "Healthy Weight Range"
    private val BMI_OVERWEIGHT = "Overweight"
    private val BMI_OBESE = "Obese"

    private lateinit var mName: EditText
    private lateinit var mWeight: EditText
    private lateinit var mFeet: EditText
    private lateinit var mInches: EditText
    private lateinit var mBMI: TextView
    private lateinit var mClass: View

    private lateinit var saveButton: Button


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.new_diet_activity)

<<<<<<< HEAD

    }

=======
        saveButton = findViewById<Button>(R.id.saveBtn)
        mName = findViewById<EditText>(R.id.nameText)
        mWeight = findViewById<EditText>(R.id.weightText)
        mFeet = findViewById<EditText>(R.id.heightFeet)
        mInches = findViewById<EditText>(R.id.heightInches)
        val shape: Drawable? = applicationContext.getDrawable(R.drawable.bmi_square)
        mClass = findViewById<View>(R.id.bmiClass)
        mClass.background = shape
        mBMI = findViewById<TextView>(R.id.bmiResult)

        val textWatcher: TextWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence,start: Int,count: Int,after: Int) {}
            override fun onTextChanged(s: CharSequence,start: Int,before: Int,count: Int){}
            override fun afterTextChanged(s: Editable) {
                if ((s.length in 2..3) || (s.contains('.') && s.length in 2..5)) {

                    //computes users BMI
                    val userBMI = calculateBMIImperial(
                        mFeet.text.toString().toDouble(),
                        mInches.text.toString().toDouble(), mWeight.text.toString().toDouble()
                    )

                    //sets color of BMI range (maybe explain colors in infotip)
                    val bmiClass = classifyBMI(userBMI)
                    if (bmiClass == BMI_UNDERWEIGHT) {
                        mClass.setBackgroundColor(applicationContext.resources.getColor(R.color.bmiBlue))
                    } else if (bmiClass == BMI_HEALTHY) {
                        mClass.setBackgroundColor(applicationContext.resources.getColor(R.color.bmiGreen))
                    } else if (bmiClass == BMI_OVERWEIGHT) {
                        mClass.setBackgroundColor(applicationContext.resources.getColor(R.color.bmiYellow))
                    } else {
                        //BMI_OBESE
                        mClass.setBackgroundColor(applicationContext.resources.getColor(R.color.bmiOrange))
                    }
                    mBMI.setText(
                        userBMI.toInt().toString()
                    ) // set TextView text to Text inside EditText
                }
            }
        }

        // add listener to the weight edittext
        mWeight.addTextChangedListener(textWatcher)

        //makes sure all fields are filled out before enabling button
        saveButton.isEnabled = false
        val editTexts = listOf(mName, mWeight, mFeet, mInches)
        for (editText in editTexts) {
            editText.addTextChangedListener(object : TextWatcher {
                override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                    var et1 = mName.text.toString().trim()
                    var et2 = mWeight.text.toString().trim()
                    var et3 = mFeet.text.toString().trim()
                    var et4 = mInches.text.toString().trim()

                    saveButton.isEnabled = et1.isNotEmpty()
                            && et2.isNotEmpty()
                            && et3.isNotEmpty()
                            && et4.isNotEmpty()
                }
                override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int){}
                override fun afterTextChanged(s: Editable){}
            })
        }

        //opens up user diet when they "save and continue
        val myDietIntent = Intent(this, MyDietActivity::class.java)
        saveButton.setOnClickListener() {
            startActivity(myDietIntent)
        }
    }

    //HELPER FUNCTIONS

    //calculates BMI based on imperial system
    fun calculateBMIImperial(heightFeet: Double, heightInches: Double, weightLbs: Double): Double {
        val totalHeightInInches = heightFeet * INCHES_IN_FOOT + heightInches
        return BMI_IMPERIAL_WEIGHT_SCALAR * weightLbs / (totalHeightInInches * totalHeightInInches)
    }

    //classify users bmi range
    fun classifyBMI(bmi: Double): String? {
        return if (bmi < 18.5) {
            BMI_UNDERWEIGHT
        } else if (bmi >= 18.5 && bmi < 25) {
            BMI_HEALTHY
        } else if (bmi >= 25 && bmi < 30) {
            BMI_OVERWEIGHT
        } else {
            BMI_OBESE
        }
    }

    //calculates BMI based on metric system (maybe make this an option later?
    //    fun calculateBMIMetric(heightCm: Double, weightKg: Double): Double {
//        return weightKg / (heightCm / CENTIMETERS_IN_METER * (heightCm / CENTIMETERS_IN_METER))
//    }
>>>>>>> 63549819755ed68018a60265bbf86d9ab6dfea02

}