package com.example.arduino_data.Sensors

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.arduino_data.R
import com.jjoe64.graphview.GraphView
import com.jjoe64.graphview.LegendRenderer
import com.jjoe64.graphview.series.DataPoint
import com.jjoe64.graphview.series.LineGraphSeries
import org.json.JSONObject
import org.json.JSONTokener
import java.lang.Exception



class Sensor3 : Fragment() {
    private var series: LineGraphSeries<DataPoint> = LineGraphSeries<DataPoint>()
    private lateinit var graph: GraphView
    private lateinit var text: TextView
    private var graph2LastXValue = 0.0

    private var field3 = ""
    private var field3Date = ""

    private var field4 = ""
    private var field4Date = ""

    private val CHANNEL: String = "1905663"
    private val APIKEY: String = "QB3CPWI4W3984MVK"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_sensor3, container, false)
        text = view.findViewById(R.id.testView3)
        getPlot(view!!)

        series.setOnDataPointTapListener { series, dataPoint ->
            Toast.makeText(
                activity,
                "Информация с датчика Температуры: \n Показатель ${dataPoint.y} \nВремя ${field3Date}",
                Toast.LENGTH_SHORT
            ).show()
        }

        val mainHandler = Handler(Looper.getMainLooper())

        mainHandler.post(object : Runnable {
            override fun run() {

                try
                {

                    getResultFeed("field3")
                    if (field3 != "null"){
                        graph2LastXValue += 0.1;
                        series.appendData(DataPoint(graph2LastXValue, field3.toDouble()), true, 60)
                    }
                }
                catch (ex: Exception){
                    println(ex)
                }
                mainHandler.postDelayed(this, 1000)
            }
        })
        return view
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            Sensor3().apply {
                arguments = Bundle().apply {
                }
            }
    }

    @SuppressLint("RestrictedApi")
    private fun getPlot(view: View) {


        graph = view.findViewById(R.id.graph3)

        series.color = Color.rgb(0, 80, 100)
        series.title = "Температура"
        series.isDrawDataPoints = true
        series.dataPointsRadius = 15f
        series.thickness = 2

        graph.addSeries(series)

        graph.title = "Температура"
        graph.titleTextSize = 50F
        graph.titleColor = Color.RED


        graph.legendRenderer.isVisible = true
        graph.legendRenderer.align = LegendRenderer.LegendAlign.TOP

        graph.viewport.isScalable = true;

        graph.viewport.isScrollable = true;

        graph.viewport.setScalableY(true);

        graph.viewport.setScrollableY(true);

        /////////////////////////////////////////////////////

    }

    fun getResultFeed(field: String){

        var res = ""
        val URL = "https://api.thingspeak.com/channels/$CHANNEL/feeds.json?api_key=$APIKEY&results=1"
        val queue = Volley.newRequestQueue(context) //Инициализация переменной для передачи запроса
        val stringRequest = StringRequest(Request.Method.GET, URL, { //Передача запроса и получение ответа
                response -> //Случай удачного результата отклика api
            val jsonObject = JSONTokener(response).nextValue() as JSONObject


            val jsonArray = jsonObject.getJSONArray("feeds")
            println("Key $jsonArray")

            for (i in 0 until jsonArray.length()) {
                field3 = jsonArray.getJSONObject(i).getString(field)
                println("field1Test $field3")

                field3Date = jsonArray.getJSONObject(i).getString("created_at")

                field3Date = checkText(field3Date, "T")


                field3Date = checkText(field3Date, "Z")

                //////////////////////////////////////////////////////

                field4 = jsonArray.getJSONObject(i).getString(field)
                println("field1Test $field4")

                field4Date = jsonArray.getJSONObject(i).getString("created_at")

                field4Date = checkText(field4Date, "T")


                field4Date = checkText(field4Date, "Z")

            }

        }, {
                error -> //Случай неудачного результата отклика api
            println(error.toString())
        })
        queue.add(stringRequest) //Добавление запроса в очередь

    }

    private fun checkText(date: String, key: String): String {
        return date.replace("$key", " ", false)
    }
}