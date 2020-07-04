package com.example.inspector.Controller.Fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.ProgressBar
import com.example.inspector.R

/**
 * A simple [Fragment] subclass.
 */
class RulesFragment : Fragment() {
    private lateinit var webView: WebView
    private lateinit var progressBar: ProgressBar
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_rules, container, false)
        webView = root.findViewById(R.id.webView)
        progressBar = root.findViewById(R.id.progressBar)
        val pdf = "http://appasp.cnen.gov.br/seguranca/normas/pdf/Nrm610.pdf"
        loadPDF(pdf)
        return root
    }

    private fun loadPDF(url: String) {
        webView.settings.setSupportZoom(true)
        webView.settings.javaScriptEnabled = true
        progressBar.progress =0
        webView.webChromeClient = object: WebChromeClient() {
            override fun onProgressChanged(view: WebView?, newProgress: Int) {
                super.onProgressChanged(view, newProgress)
                progressBar.progress = newProgress
                if(newProgress == 100) {
                    progressBar.visibility = View.GONE
                }
            }
        }
        webView.loadUrl("https://docs.google.com/gview?embedded=true&url=$url")


    }

}
