package com.example.arduino_data

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
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

    private lateinit var testView: TextView
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
        try{


        }
        catch (ex: Exception){
            println(ex)
        }

    }

    @SuppressLint("RestrictedApi")
    private fun getPlot() {


        val graph: GraphView = findViewById(R.id.graph)
        val series: LineGraphSeries<DataPoint> = LineGraphSeries<DataPoint>()
        val series2: LineGraphSeries<DataPoint> = LineGraphSeries<DataPoint>()
        for (i in 0 until data.size) {
            series.appendData(DataPoint(i.toDouble(), data[i].field1.toDouble()), true, data.size)
            println("series: ${series.lowestValueY}")
        }

        for (i in 0 until 10) {
            series2.appendData(DataPoint(i.toDouble(), Math.random() + 200), true, 10)
            println("series: ${series.lowestValueY}")
        }

        //Задаємо зовнішній вигляд кривої
        //Задаємо зовнішній вигляд кривої
        series.color = Color.rgb(0, 80, 100) //встановити колір кривої
        series.title = "CO2" // встановити назву кривої для легенди
        series.isDrawDataPoints = true // промальовувати точки
        series.dataPointsRadius = 15f // радіус точки даних
        series.thickness = 2 //товщина лінії

        series2.color = Color.rgb(234, 80, 100) //встановити колір кривої
        series2.title = "Что-то иное" // встановити назву кривої для легенди
        series2.isDrawDataPoints = true // промальовувати точки
        series2.dataPointsRadius = 15f // радіус точки даних
        series2.thickness = 2 //товщина лінії


        graph.addSeries(series)
        graph.addSeries(series2)

        //Назва графіка

        //Назва графіка
        graph.title = "Expenses"
        graph.titleTextSize = 50F
        graph.titleColor = Color.RED
        //Легенда
        //Легенда
        graph.legendRenderer.isVisible = true
        graph.legendRenderer.align = LegendRenderer.LegendAlign.TOP

        graph.viewport.isScalable = true;

// activate horizontal scrolling
        graph.viewport.isScrollable = true;

// activate horizontal and vertical zooming and scrolling
        graph.viewport.setScalableY(true);

// activate vertical scrolling
        graph.viewport.setScrollableY(true);

        graph.legendRenderer.align = LegendRenderer.LegendAlign.TOP;

        series.setOnDataPointTapListener { series, dataPoint ->
            Toast.makeText(
                getActivity(this),
                "Информация с датчика CO2: \n Показатель ${dataPoint.y} \nВремя ${data[dataPoint.x.toInt()].field1Date}",
                Toast.LENGTH_SHORT
            ).show()
        }

        series2.setOnDataPointTapListener { series, dataPoint ->
            Toast.makeText(
                getActivity(this),
                "Информация с датчика о чем-то ином: \n Показатель ${dataPoint.y} \nВремя ${dataPoint.x}",
                Toast.LENGTH_SHORT
            ).show()
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
        //    testView.text = jsonArray.toString()

            for (i in 0 until jsonArray.length()) {
                val field1 = jsonArray.getJSONObject(i).getString("field1")
                val field2 = jsonArray.getJSONObject(i).getString("field2")

                var field1Date = jsonArray.getJSONObject(i).getString("created_at")
                var field2Date = jsonArray.getJSONObject(i).getString("created_at")
                field1Date = checkText(field1Date, "T")
                field1Date = checkText(field1Date, "Z")

                field2Date = checkText(field1Date, "Z")
                field2Date = checkText(field1Date, "Z")

                data.add(Data(field1, field1Date, field2, field2Date))
            }
            for (i in 0 until  data.size){
                res = res + "field 1: ${data[i].field1} dateF1: ${data[i].field1Date} \n" +
                        "field 2: ${data[i].field2} dateF2: ${data[i].field2Date} \n"

            }
            testView.text = res
            getPlot()
        }, {
                error -> //Случай неудачного результата отклика api
            println(error.toString())
        })
        queue.add(stringRequest) //Добавление запроса в очередь
    }

    private fun init(){
        testView = findViewById(R.id.testView)
    }

    private fun checkText(date: String, key: String): String {
        return date.replace("$key", " ", false)
    }


}