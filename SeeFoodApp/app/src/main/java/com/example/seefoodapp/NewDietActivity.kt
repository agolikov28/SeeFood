package com.example.seefoodapp

import android.content.Intent
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.*
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
    private lateinit var mGender: Switch
    private lateinit var mAge: EditText
    private lateinit var mWeight: EditText
    private lateinit var mFeet: EditText
    private lateinit var mInches: EditText
    private lateinit var mBMI: TextView
    private lateinit var mClass: View
    private lateinit var mCalories: TextView
    private lateinit var mProteins: TextView
    private lateinit var mCarbs: TextView
    private lateinit var mFats: TextView
    private lateinit var mActivityLevel: RadioGroup

    private lateinit var saveButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.new_diet_activity)

        saveButton = findViewById<Button>(R.id.saveBtn)
        mName = findViewById<EditText>(R.id.nameText)
        mGender = findViewById<Switch>(R.id.genderSwitch)
        mAge = findViewById<EditText>(R.id.ageText)
        mWeight = findViewById<EditText>(R.id.weightText)
        mFeet = findViewById<EditText>(R.id.heightFeet)
        mInches = findViewById<EditText>(R.id.heightInches)
        val shape: Drawable? = applicationContext.getDrawable(R.drawable.bmi_square)
        mClass = findViewById<View>(R.id.bmiClass)
        mClass.background = shape
        mBMI = findViewById<TextView>(R.id.bmiResult)
        mCalories = findViewById<TextView>(R.id.caloriesText)
        mProteins = findViewById<TextView>(R.id.proteinsText)
        mCarbs = findViewById<TextView>(R.id.carbsText)
        mFats = findViewById<TextView>(R.id.fatsText)
        mActivityLevel = findViewById(R.id.activityGroup)

        val textWatcher: TextWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence,start: Int,count: Int,after: Int) {}
            override fun onTextChanged(s: CharSequence,start: Int,before: Int,count: Int){}
            override fun afterTextChanged(s: Editable) {
                if ((s.length in 2..3) || (s.contains('.') && s.length in 2..5)) {

                    //computes users BMI and stores into userBMI
                    val userBMI = calculateBMIImperial(
                        mFeet.text.toString().toDouble(),
                        mInches.text.toString().toDouble(),
                        mWeight.text.toString().toDouble()
                    )

                    // SET GENDER VALUE FROM SWITCH
                    var gender = if (mGender.isChecked) {
                        1
                    } else {
                        0
                    }

                    // SET ACTIVITY LEVEL VALUE FROM RADIOGROUP
                    val activityId = mActivityLevel.checkedRadioButtonId
                    var level = 0.0
                    if (activityId == R.id.lowButton) {
                        level = 1.2
                    }
                    else if (activityId == R.id.mediumButton) {
                        level = 1.55
                    }
                    else {
                        level = 1.725
                    }

                    val userMacros = calculateMacrosImperial(
                        mAge.text.toString().toInt(),
                        gender,
                        mFeet.text.toString().toDouble(),
                        mInches.text.toString().toDouble(),
                        mWeight.text.toString().toDouble(),
                        level
                    )

                    mCalories.text = userMacros.toInt().toString().plus(" cals")
                    mProteins.text = ((userMacros * 0.4) / 4.0).toInt().toString().plus("g")
                    mCarbs.text = ((userMacros * 0.3) / 4.0).toInt().toString().plus("g")
                    mFats.text = ((userMacros * 0.3) / 4.0).toInt().toString().plus("g")

                    // sets color of BMI range (maybe explain colors in infotip)
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

                    // set TextView text to BMI result
                    mBMI.text = userBMI.toInt().toString()
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

    // TODO: SHOULD ADD *ACTIVITY LEVEL*
    // Calculates Macros based on imperial system
    fun calculateMacrosImperial(age: Int, gender: Int, heightFeet: Double, heightInches: Double, weightLbs: Double, activityLevel: Double): Double {
        // Calculate macros for women
        if(gender == 1) {
            val totalHeightInCm = (heightFeet * INCHES_IN_FOOT + heightInches) * 2.54
            val BMR = 655.1 + (9.563 * (weightLbs * 0.453592)) + (1.85 * totalHeightInCm) - (4.676 * age)
            return (BMR * activityLevel)
        }
        // Calculate macros for men
        else {
            val totalHeightInCm = (heightFeet * INCHES_IN_FOOT + heightInches) * 2.54
            val BMR = 66.5 + (13.75 * (weightLbs * 0.453592)) + (5.003 * totalHeightInCm) - (6.75 * age)
            return (BMR * activityLevel)
        }
    }

    //calculates BMI based on metric system (maybe make this an option later?
    //    fun calculateBMIMetric(heightCm: Double, weightKg: Double): Double {
//        return weightKg / (heightCm / CENTIMETERS_IN_METER * (heightCm / CENTIMETERS_IN_METER))
//    }

}