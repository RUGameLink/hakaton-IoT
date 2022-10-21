package com.example.arduino_data

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import org.json.JSONObject
import org.json.JSONTokener

class MainActivity : AppCompatActivity() {
    private val CHANNEL: String = "1603688"
    private val APIKEY: String = "PFX7AGK7AGX15KLA"

    private val data = ArrayList<Data>()

    private lateinit var testView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        init()

        try {
            getResultFeed()
        }
        catch (ex: Exception){
            print(ex)
        }

    }

    private fun getResultFeed(){
        var res = ""

        val URL = "https://api.thingspeak.com/channels/$CHANNEL/feeds.json?api_key=$APIKEY&results=2"
        val queue = Volley.newRequestQueue(this) //Инициализация переменной для передачи запроса
        val stringRequest = StringRequest(Request.Method.GET, URL, { //Передача запроса и получение ответа
                response -> //Случай удачного результата отклика api
            val jsonObject = JSONTokener(response).nextValue() as JSONObject


            val jsonArray = jsonObject.getJSONArray("feeds")
            testView.text = jsonArray.toString()

            for (i in 0 until jsonArray.length()) {
                val field1 = jsonArray.getJSONObject(i).getString("field1")
                val field2 = jsonArray.getJSONObject(i).getString("field2")

                val field1Date = jsonArray.getJSONObject(i).getString("created_at")
                val field2Date = jsonArray.getJSONObject(i).getString("created_at")

                data.add(Data(field1, field1Date, field2, field2Date))
            }
            for (i in 0 until  data.size){
                res = res + "field 1: ${data[i].field1} dateF1: ${data[i].field1Date} \n" +
                        "field 2: ${data[i].field2} dateF2: ${data[i].field2Date} \n"
            }
            testView.text = res
        }, {
                error -> //Случай неудачного результата отклика api
            println(error.toString())
        })
        queue.add(stringRequest) //Добавление запроса в очередь
    }

    private fun init(){
        testView = findViewById(R.id.testView)
    }
}