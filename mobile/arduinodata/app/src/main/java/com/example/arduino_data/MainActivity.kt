package com.example.arduino_data

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.arduino_data.Sensors.*
import com.google.android.material.bottomnavigation.BottomNavigationView


class MainActivity : AppCompatActivity() {
    private val CHANNEL: String = "1905663"
    private val APIKEY: String = "QB3CPWI4W3984MVK"

    private lateinit var menu: BottomNavigationView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    //    requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
        init()

        try {
        //    getResultFeed()
            replaceFragment(Sensor1())
        }
        catch (ex: Exception){
            print(ex)
        }



        menu.setOnItemSelectedListener{
                when(it.itemId){
                    R.id.sensor1 -> {
                        replaceFragment(Sensor1())
                        Toast.makeText(this, "Сенсор 1", Toast.LENGTH_SHORT).show()
                    }
                    R.id.sensor2 -> {
                        replaceFragment(Sensor2())
                        Toast.makeText(this, "Сенсор 2", Toast.LENGTH_SHORT).show()
                    }
                    R.id.sensor3 -> {
                        replaceFragment(Sensor3())
                        Toast.makeText(this, "Сенсор 3", Toast.LENGTH_SHORT).show()
                    }
                    R.id.sensor4 -> {
                        replaceFragment(Sensor4())
                        Toast.makeText(this, "Сенсор 4", Toast.LENGTH_SHORT).show()
                    }
                    R.id.sensor5 -> {
                        replaceFragment(Sensor5())
                        Toast.makeText(this, "Сенсор 5", Toast.LENGTH_SHORT).show()
                    }

                    else -> {

                    }
                }
                true

        }
    }

    private fun replaceFragment(fragment: Fragment){
//        val bundle = Bundle()
//        bundle.putParcelableArrayList("list", data);
//        fragment.arguments = bundle
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.frame_layout, fragment)

        fragmentTransaction.commit()
    }

//    private fun getResultFeed(){
//        var res = ""
//        val URL = "https://api.thingspeak.com/channels/$CHANNEL/feeds.json?api_key=$APIKEY&results=3"
//        val queue = Volley.newRequestQueue(this) //Инициализация переменной для передачи запроса
//        val stringRequest = StringRequest(Request.Method.GET, URL, { //Передача запроса и получение ответа
//                response -> //Случай удачного результата отклика api
//            val jsonObject = JSONTokener(response).nextValue() as JSONObject
//
//
//            val jsonArray = jsonObject.getJSONArray("feeds")
//            println("Key $jsonArray")
//        //    testView.text = jsonArray.toString()
//
//            for (i in 0 until jsonArray.length()) {
//                val field1 = jsonArray.getJSONObject(i).getString("field1")
//                val field2 = jsonArray.getJSONObject(i).getString("field2")
//
//                var field1Date = jsonArray.getJSONObject(i).getString("created_at")
//                var field2Date = jsonArray.getJSONObject(i).getString("created_at")
//                field1Date = checkText(field1Date, "T")
//                field1Date = checkText(field1Date, "Z")
//
//                field2Date = checkText(field1Date, "T")
//                field2Date = checkText(field1Date, "Z")
//
//                data.add(Data(field1, field1Date, field2, field2Date))
//            }
//
////            testView.text = res
//        //    getPlot()
//            replaceFragment(Sensor1())
//        }, {
//                error -> //Случай неудачного результата отклика api
//            println(error.toString())
//        })
//        queue.add(stringRequest) //Добавление запроса в очередь
//    }
//
//    private fun checkText(date: String, key: String): String {
//        return date.replace("$key", " ", false)
//    }

    private fun init(){
        menu = findViewById(R.id.bottom_navigation_view)
    }
}