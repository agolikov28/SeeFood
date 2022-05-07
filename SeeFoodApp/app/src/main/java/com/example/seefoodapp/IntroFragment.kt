package com.example.seefoodapp

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class IntroFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    var pageTitle: String = ""
    private lateinit var mTextView: TextView
    private lateinit var mImageView: ImageView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_intro, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val f1 = "Welcome to SeeFood!"
        val f2 = "STEP 1:\nCreate a new diet and enter your measurements"

        mTextView = view.findViewById(R.id.fragment1) as TextView


        mTextView!!.text = pageTitle
        if(mTextView.text.equals(f1)){
            mImageView = view.findViewById(R.id.image1) as ImageView
            showHide(mImageView)
        }
        else if(mTextView.text.equals(f2)){
            mImageView = view.findViewById(R.id.image2) as ImageView
            showHide(mImageView)
        }
        else {
            mImageView = view.findViewById(R.id.image3) as ImageView
            showHide(mImageView)
        }
    }

    fun setTitle(title: String) {
        pageTitle = title
    }

    fun showHide(view:View) {
        view.visibility = if (view.visibility == View.VISIBLE){
            View.INVISIBLE
        } else{
            View.VISIBLE
        }
    }

    companion object {
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            IntroFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}