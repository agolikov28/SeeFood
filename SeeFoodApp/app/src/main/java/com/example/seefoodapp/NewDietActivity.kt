package com.example.seefoodapp

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import java.sql.Time
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*


class NewDietActivity : AppCompatActivity() {
    //    private val CENTIMETERS_IN_METER = 100
    private val INCHES_IN_FOOT = 12
    private val BMI_IMPERIAL_WEIGHT_SCALAR = 703

    private val BMI_UNDERWEIGHT = "Underweight"
    private val BMI_HEALTHY = "Healthy Weight Range"
    private val BMI_OVERWEIGHT = "Overweight"
    private val BMI_OBESE = "Obese"

    private lateinit var sharedpreferences: SharedPreferences
    private lateinit var mName: EditText
    private lateinit var mDate: String
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
    private lateinit var mInfoButton: ImageButton
    private lateinit var mInfoButton2: ImageButton

    private lateinit var clearButton: Button

    private lateinit var saveButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.new_diet_activity)

        sharedpreferences = getSharedPreferences("shared_preferences", Context.MODE_PRIVATE)

        saveButton = findViewById<Button>(R.id.saveBtn)
        clearButton = findViewById(R.id.clearBtn)
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
        val cBMI = findViewById<TextView>(R.id.bmiText)
        val bBMI = findViewById<View>(R.id.bmiClass)
        val macrosText = findViewById<TextView>(R.id.macrosTitle)
        val calsText = findViewById<TextView>(R.id.caloriesTitle)
        val prosText = findViewById<TextView>(R.id.proteinsTitle)
        val carbsText = findViewById<TextView>(R.id.carbsTitle)
        val fatsText = findViewById<TextView>(R.id.fatsTitle)
        val lowBtn = findViewById<RadioButton>(R.id.lowButton)
        val medBtn = findViewById<RadioButton>(R.id.mediumButton)
        val highBtn = findViewById<RadioButton>(R.id.highButton)

        mInfoButton = findViewById(R.id.infoButton)
        mInfoButton.setOnClickListener() {
            Toast.makeText(applicationContext,"How many times do you exercise a week? Low: 0-1 Medium: 3-4 High: 5-7",Toast.LENGTH_LONG).show()
        }

        mInfoButton2 = findViewById(R.id.infoButton2)
        mInfoButton2.setOnClickListener() {
            Toast.makeText(applicationContext,"This is the daily recommend intake, based on your BMI",Toast.LENGTH_LONG).show()
        }

        val calculateBtn = findViewById<Button>(R.id.calculateButton)
        calculateBtn.setOnClickListener(View.OnClickListener {
            var failFlag = false
            if (mName.text.toString().trim().length === 0) {
                failFlag = true
                mName.error = "A value is required"
            }
            if (mAge.text.toString().trim().length === 0) {
                failFlag = true
                mAge.error = "A value is required"
            }
            if (mFeet.text.toString().trim().length === 0) {
                failFlag = true
                mFeet.error = "A value is required"
            }
            if (mInches.text.toString().trim().length === 0) {
                failFlag = true
                mInches.error = "A value is required"
            }
            if (mWeight.text.toString().trim().length === 0) {
                failFlag = true
                mWeight.error = "A value is required"
            }
            // if all are fine
            if (!failFlag) {
                showHide(calculateBtn)
                showHide(cBMI)
                showHide(bBMI)
                showHide(macrosText)
                showHide(calsText)
                showHide(prosText)
                showHide(carbsText)
                showHide(fatsText)
                showHide(saveButton)
                showHide(clearButton)
                showHide(mCalories)
                showHide(mFats)
                showHide(mCarbs)
                showHide(mProteins)
                showHide(mInfoButton2)

                mName.isEnabled = false
                mGender.isEnabled = false
                mAge.isEnabled = false
                mFeet.isEnabled = false
                mInches.isEnabled = false
                mWeight.isEnabled = false
                lowBtn.isEnabled = false
                medBtn.isEnabled = false
                highBtn.isEnabled = false

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
                var level = if (activityId == R.id.lowButton) {
                    1.2
                } else if (activityId == R.id.mediumButton) {
                    1.55
                } else {
                    1.725
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
                mProteins.text = (userMacros * 0.05625).toInt().toString().plus("g")
                mCarbs.text = (userMacros * 0.1375).toInt().toString().plus("g")
                mFats.text = (userMacros * 0.03).toInt().toString().plus("g")

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
        })


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

        clearButton.setOnClickListener() {
            mName.text.clear()
            mAge.text.clear()
            mWeight.text.clear()
            mFeet.text.clear()
            mInches.text.clear()
            mActivityLevel.check(R.id.lowButton)
            mGender.isChecked = false

            showHide(calculateBtn)
            showHide(cBMI)
            showHide(bBMI)
            showHide(macrosText)
            showHide(calsText)
            showHide(prosText)
            showHide(carbsText)
            showHide(fatsText)
            showHide(mCalories)
            showHide(mFats)
            showHide(mCarbs)
            showHide(mProteins)
            showHide(saveButton)
            showHide(clearButton)

            mName.isEnabled = true
            mGender.isEnabled = true
            mAge.isEnabled = true
            mFeet.isEnabled = true
            mInches.isEnabled = true
            mWeight.isEnabled = true
            lowBtn.isEnabled = true
            medBtn.isEnabled = true
            highBtn.isEnabled = true
        }

        //opens up user diet when they click "save and continue"
        saveButton.setOnClickListener() {
            var gender = if (mGender.isChecked) {
                1
            } else {
                0
            }
            val activityId = mActivityLevel.checkedRadioButtonId
            var level = if (activityId == R.id.lowButton) {
                1.2
            } else if (activityId == R.id.mediumButton) {
                1.55
            } else {
                1.725
            }
            val userMacros = calculateMacrosImperial(
                mAge.text.toString().toInt(),
                gender,
                mFeet.text.toString().toDouble(),
                mInches.text.toString().toDouble(),
                mWeight.text.toString().toDouble(),
                level
            )

            mDate = getCurrentDate().toString()

            val editor = sharedpreferences.edit()
            editor.putString("nameOfDiet", mName.text.toString())
            editor.putString("date", mDate.toString())
            editor.putInt("gender", gender)
            editor.putInt("age", mAge.text.toString().toInt())
            editor.putFloat("heightFeet", mFeet.text.toString().toDouble().toFloat())
            editor.putFloat("heightInches", mInches.text.toString().toDouble().toFloat())
            editor.putFloat("weight", mWeight.text.toString().toDouble().toFloat())
            editor.putInt("targetCalories", userMacros.toInt())
            editor.putInt("targetProteins", (userMacros * 0.05625).toInt())
            editor.putInt("targetCarbs", (userMacros * 0.1375).toInt())
            editor.putInt("targetFats", (userMacros * 0.03).toInt())
            editor.putInt("currCalories", 0)
            editor.putInt("currProteins", 0)
            editor.putInt("currCarbs", 0)
            editor.putInt("currFats", 0)
            editor.commit()

            val myDietIntent = Intent(this, MyDietActivity::class.java)
            startActivity(myDietIntent)
        }
    }

    //HELPER FUNCTIONS


    fun getCurrentDate(): String? {
        val time =  LocalDateTime.now()
        val formatter = DateTimeFormatter.ofPattern("MM-dd-yyyy")
        val formatted = time.format(formatter)
        return (formatted.toString())
    }

    fun showHide(view:View) {
        view.visibility = if (view.visibility == View.VISIBLE){
            View.INVISIBLE
        } else{
            View.VISIBLE
        }
    }

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
