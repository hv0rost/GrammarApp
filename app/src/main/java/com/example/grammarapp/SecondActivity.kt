package com.example.grammarapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner

class SecondActivity : AppCompatActivity() {
    var spinnerCategory : Spinner? = null
    var createTest : Button? = null
    var categories = arrayOf("Past Simple")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.second_activity)
        createTest = findViewById(R.id.createTest)
        spinnerCategory = findViewById(R.id.setCategory)

        spinnerCategory!!.adapter = ArrayAdapter<String>(this, R.layout.support_simple_spinner_dropdown_item, categories)
        spinnerCategory!!.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(parent: AdapterView<*>?, view: View?,
                position: Int, id: Long) {

            }

            override fun onNothingSelected(parent: AdapterView<*>?) {

            }
        }

        //todo other category on front and server
        createTest!!.setOnClickListener {
            val intent = Intent(this, ThirdActivity::class.java)
            startActivity(intent)
        }
    }
}