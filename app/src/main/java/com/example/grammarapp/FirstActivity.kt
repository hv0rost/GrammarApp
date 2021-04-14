package com.example.grammarapp

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.GsonBuilder
import org.jsoup.HttpStatusException
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.select.Elements
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.net.MalformedURLException

class FirstActivity : AppCompatActivity() {
    private var postButton : Button? = null
    private var editTextUrl : EditText? = null
    private var grammarApi: GrammarApi? = null
    private var intentFlag : Boolean = true
    var errorText: TextView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.first_activity)

        val gson = GsonBuilder().serializeNulls().create()
        val retrofit = Retrofit.Builder()
                .baseUrl("https://grammar-app-heroku.herokuapp.com/")
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build()

        errorText = findViewById(R.id.errorText)
        postButton = findViewById(R.id.button_submit)
        editTextUrl = findViewById(R.id.editUrl)

        postButton!!.setOnClickListener{
            if(editTextUrl!!.text.toString().isNotEmpty())
                getTextFromWeb(editTextUrl!!.text.toString())
            else
                errorText!!.text = "Поле должно быть заполнено"

        }
        grammarApi = retrofit.create(GrammarApi::class.java)
    }
    private fun getTextFromWeb(url: String) {
        Thread(Runnable {
            val str = StringBuilder()
            try {
                var temp = ""
                val doc: Document = Jsoup.connect(url).get()
                val elements: Elements = doc.select("div")
                for (element in elements) {
                    if (!temp.contains(element.text())) {
                        if (element.text() != "") {
                            temp = element.text()
                            str.append(element.text())
                        }
                    }
                }
                val htmlContainer = PostText(str.toString())
                val call: Call<PostText> = grammarApi!!.createText(htmlContainer)
                call.enqueue(object : Callback<PostText> {
                    override fun onFailure(call: Call<PostText>, t: Throwable) {
                        errorText!!.text = "Не удается связаться с сервером"
                        intentFlag = false
                    }

                    override fun onResponse(call: Call<PostText>, response: Response<PostText>) {
                        if (!response.isSuccessful)
                            errorText!!.text = "Не удается связаться с сервером"
                        intentFlag = true
                    }
                })
                    val intent = Intent(this, SecondActivity::class.java)
                    startActivity(intent)
            } catch (ex: IllegalArgumentException) {
                runOnUiThread { errorText!!.text = "Введены некоректные даныне" }
            } catch (ex: HttpStatusException) {
                runOnUiThread { errorText!!.text = "Непраильно введен адрес" }
            } catch (ex: MalformedURLException) {
                runOnUiThread { errorText!!.text = "Введены некоректные даныне" }
            }

        }).start()

    }
}