package com.example.arduino_data.Sensors

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.*
import androidx.annotation.RequiresApi
import com.example.arduino_data.R

class Sensor5 : Fragment() {
    private lateinit var webView: WebView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_sensor5, container, false)
        init(view)
        var request = "https://rugamelink.github.io/hakaton-IoT/web/index.html"
        loadPage(request)
        return view
    }

    @SuppressLint("UseRequireInsteadOfGet")
    private fun init(view: View?) {
        webView = view!!.findViewById(R.id.webView)
    }

    private fun loadPage(request: String) {
        webView.webViewClient = WebViewClient()
        webView.webChromeClient = WebChromeClient()
        // включаем поддержку JavaScript
        webView.settings.javaScriptEnabled = true
        webView.loadUrl(request)

        webView.webViewClient = object: WebViewClient(){

            override fun onReceivedError(
                view: WebView?,
                request: WebResourceRequest?,
                error: WebResourceError?
            ) {
                super.onReceivedError(view, request, error)
            }
        }
    }

    companion object {
        fun newInstance(param1: String, param2: String) =
            Sensor5().apply {
                arguments = Bundle().apply {
                }
            }
    }
}