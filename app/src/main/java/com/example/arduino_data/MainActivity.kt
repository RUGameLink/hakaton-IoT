package com.example.arduino_data

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.android.material.bottomnavigation.BottomNavigationMenuView
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.internal.ContextUtils.getActivity
import com.jjoe64.graphview.GraphView
import com.jjoe64.graphview.LegendRenderer
import com.jjoe64.graphview.series.DataPoint
import com.jjoe64.graphview.series.LineGraphSeries
import org.json.JSONObject
import org.json.JSONTokener


class MainActivity : AppCompatActivity() {
    private val CHANNEL: String = "1603688"
    private val APIKEY: String = "PFX7AGK7AGX15KLA"

    private val data = ArrayList<Data>()

    private lateinit var menu: BottomNavigationView
 //   private lateinit var testView: TextView
//    private var plot: XYPlot? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    //    requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
        init()

        try {
            getResultFeed()
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
        val bundle = Bundle()
        bundle.putParcelableArrayList("list", data);
        fragment.arguments = bundle
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.frame_layout, fragment)

        fragmentTransaction.commit()
    }




    private fun getResultFeed(){
        var res = ""

        val URL = "https://api.thingspeak.com/channels/$CHANNEL/feeds.json?api_key=$APIKEY&results=2"
        val queue = Volley.newRequestQueue(this) //Инициализация переменной для передачи запроса
        val stringRequest = StringRequest(Request.Method.GET, URL, { //Передача запроса и получение ответа
                response -> //Случай удачного результата отклика api
            val jsonObject = JSONTokener(response).nextValue() as JSONObject


            val jsonArray = jsonObject.getJSONArray("feeds")
        //    testView.text = jsonArray.toString()

            for (i in 0 until jsonArray.length()) {
                val field1 = jsonArray.getJSONObject(i).getString("field1")
                val field2 = jsonArray.getJSONObject(i).getString("field2")

                var field1Date = jsonArray.getJSONObject(i).getString("created_at")
                var field2Date = jsonArray.getJSONObject(i).getString("created_at")
                field1Date = checkText(field1Date, "T")
                field1Date = checkText(field1Date, "Z")

                field2Date = checkText(field1Date, "T")
                field2Date = checkText(field1Date, "Z")

                data.add(Data(field1, field1Date, field2, field2Date))
            }
            for (i in 0 until  data.size){
                res = res + "field 1: ${data[i].field1} dateF1: ${data[i].field1Date} \n" +
                        "field 2: ${data[i].field2} dateF2: ${data[i].field2Date} \n"

            }
//            testView.text = res
        //    getPlot()
            replaceFragment(Sensor1())
        }, {
                error -> //Случай неудачного результата отклика api
            println(error.toString())
        })
        queue.add(stringRequest) //Добавление запроса в очередь
    }

    private fun checkText(date: String, key: String): String {
        return date.replace("$key", " ", false)
    }

    private fun init(){
        menu = findViewById(R.id.bottom_navigation_view)
    }

}