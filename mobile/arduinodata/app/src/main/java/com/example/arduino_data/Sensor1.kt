package com.example.arduino_data

import android.annotation.SuppressLint
import android.app.Activity
import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.androidplot.util.PixelUtils.init
import com.jjoe64.graphview.GraphView
import com.jjoe64.graphview.LegendRenderer
import com.jjoe64.graphview.series.DataPoint
import com.jjoe64.graphview.series.LineGraphSeries

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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            val args = arguments
            param1 = args?.getParcelableArrayList<Data>(ARG_PARAM1)!!
        }
    }

    @SuppressLint("UseRequireInsteadOfGet")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_sensor1, container, false)

        getPlot(view!!)
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


        val graph: GraphView = view.findViewById(R.id.graph)
        val series: LineGraphSeries<DataPoint> = LineGraphSeries<DataPoint>()
        val series2: LineGraphSeries<DataPoint> = LineGraphSeries<DataPoint>()
        for (i in 0 until param1.size) {
            series.appendData(DataPoint(i.toDouble(), param1[i].field1.toDouble()), true, param1.size)
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
                activity,
                "Информация с датчика CO2: \n Показатель ${dataPoint.y} \nВремя ${param1[dataPoint.x.toInt()].field1Date}",
                Toast.LENGTH_SHORT
            ).show()
        }

        series2.setOnDataPointTapListener { series, dataPoint ->
            Toast.makeText(
                activity,
                "Информация с датчика о чем-то ином: \n Показатель ${dataPoint.y} \nВремя ${dataPoint.x}",
                Toast.LENGTH_SHORT
            ).show()
        }
    }
}