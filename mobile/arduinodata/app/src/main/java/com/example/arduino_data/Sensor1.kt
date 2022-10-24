package com.example.arduino_data

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.jjoe64.graphview.GraphView
import com.jjoe64.graphview.LegendRenderer
import com.jjoe64.graphview.series.DataPoint
import com.jjoe64.graphview.series.LineGraphSeries
import org.json.JSONObject
import org.json.JSONTokener
import java.lang.Exception

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "list"



/**
 * A simple [Fragment] subclass.
 * Use the [Sensor1.newInstance] factory method to
 * create an instance of this fragment.
 */
class Sensor1 : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1 = ArrayList<Data>()
    private lateinit var apiHelper: ApiHelper
    private var series: LineGraphSeries<DataPoint> = LineGraphSeries<DataPoint>()
    private lateinit var graph: GraphView
    private var graph2LastXValue = 0.0

    private var field1 = ""
    private var field1Date = ""

    private val CHANNEL: String = "1905663"
    private val APIKEY: String = "QB3CPWI4W3984MVK"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
//            val args = arguments
//            param1 = args?.getParcelableArrayList<Data>(ARG_PARAM1)!!
        }
    }

    @SuppressLint("UseRequireInsteadOfGet")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_sensor1, container, false)
        apiHelper = ApiHelper("1905663", "QB3CPWI4W3984MVK")
        getPlot(view!!)

        series.setOnDataPointTapListener { series, dataPoint ->
            Toast.makeText(
                activity,
                "Информация с датчика CO2: \n Показатель ${dataPoint.y} \nВремя ${field1Date}",
                Toast.LENGTH_SHORT
            ).show()
        }

        val mainHandler = Handler(Looper.getMainLooper())

        mainHandler.post(object : Runnable {
            override fun run() {

                try
                {

                    getResultFeed("field1")
                    if (field1 != "null"){
                        graph2LastXValue += 1.0;
                        series.appendData(DataPoint(graph2LastXValue, field1.toDouble()), true, 60)
                    }

                }
                catch (ex: Exception){
                    println(ex)
                }
                mainHandler.postDelayed(this, 20000)
            }
        })
        return view
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            Sensor1().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                }
            }
    }

        @SuppressLint("RestrictedApi")
    private fun getPlot(view: View) {


        graph = view.findViewById(R.id.graph)

        series.color = Color.rgb(0, 80, 100)
        series.title = "CO2"
        series.isDrawDataPoints = true
        series.dataPointsRadius = 15f
        series.thickness = 2

        graph.addSeries(series)

        graph.title = "CO2"
        graph.titleTextSize = 50F
        graph.titleColor = Color.RED


        graph.legendRenderer.isVisible = true
        graph.legendRenderer.align = LegendRenderer.LegendAlign.TOP

        graph.viewport.isScalable = true;

        graph.viewport.isScrollable = true;

        graph.viewport.setScalableY(true);

        graph.viewport.setScrollableY(true);

        graph.legendRenderer.align = LegendRenderer.LegendAlign.TOP;

        series.setOnDataPointTapListener { series, dataPoint ->
            Toast.makeText(
                activity,
                "Информация с датчика CO2: \n Показатель ${dataPoint.y} \nВремя ${param1[dataPoint.x.toInt()].field1Date}",
                Toast.LENGTH_SHORT
            ).show()
        }
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
                field1 = jsonArray.getJSONObject(i).getString(field)
                println("field1Test $field1")

                field1Date = jsonArray.getJSONObject(i).getString("created_at")

                field1Date = checkText(field1Date, "T")


                field1Date = checkText(field1Date, "Z")

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