package com.example.arduino_data

import android.content.Context
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import org.json.JSONObject
import org.json.JSONTokener
import java.lang.Exception

class ApiHelper {
    private var CHANNEL: String
    private var APIKEY: String
    private var data: Data? = null

    constructor(channel: String, apiKey: String){
        this.CHANNEL = channel
        this.APIKEY = apiKey
    }


    public fun getResultFeed(context: Context, field: String){
        var field1 = ""
        var field1Date = ""
        var res = ""
        val URL = "https://api.thingspeak.com/channels/$CHANNEL/feeds.json?api_key=$APIKEY&results=1"
        val queue = Volley.newRequestQueue(context) //Инициализация переменной для передачи запроса
        val stringRequest = StringRequest(Request.Method.GET, URL, { //Передача запроса и получение ответа
                response -> //Случай удачного результата отклика api
            val jsonObject = JSONTokener(response).nextValue() as JSONObject


            val jsonArray = jsonObject.getJSONArray("feeds")
            println("Key $jsonArray")

            for (i in 0 until jsonArray.length()) {
                field1 = jsonArray.getJSONObject(i).getString(field)
                println("field1Test $field1")

                field1Date = jsonArray.getJSONObject(i).getString("created_at")

                field1Date = checkText(field1Date, "T")


                field1Date = checkText(field1Date, "Z")

            }
            setData(field1, field1Date)

        }, {
                error -> //Случай неудачного результата отклика api
            println(error.toString())
        })
        queue.add(stringRequest) //Добавление запроса в очередь

    }

    private fun setData(field: String, date: String){
        data = Data(field, date)
    }

    fun getData(): Data{
            return data!!
    }

    private fun checkText(date: String, key: String): String {
        return date.replace("$key", " ", false)
    }
}