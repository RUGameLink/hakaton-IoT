package com.example.arduino_data

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
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
    private lateinit var apiHelper: ApiHelper

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

        series.color = Color.rgb(0, 80, 100)
        series.title = "CO2"
        series.isDrawDataPoints = true
        series.dataPointsRadius = 15f
        series.thickness = 2

        graph.addSeries(series)

        graph.title = "Expenses"
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

    override fun onResume() {
        super.onResume()
        var mTimer2 = object : Runnable {
            override fun run() {
                graph2LastXValue += 1.0
                mSeries2.appendData(DataPoint(graph2LastXValue, getRandom()), true, 40)
                mHandler.postDelayed(this, 200)
            }
        }
        mHandler.postDelayed(mTimer2, 1000)
    }
}