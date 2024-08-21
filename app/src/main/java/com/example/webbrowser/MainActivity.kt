package com.example.webbrowser

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.EditorInfo
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.airbnb.lottie.LottieAnimationView

class MainActivity : AppCompatActivity() {

    private lateinit var webView: WebView
    private lateinit var lottieLoading: LottieAnimationView
    private lateinit var lottieSearch: LottieAnimationView
    private lateinit var reloadSearch: LottieAnimationView
    private lateinit var cancelButton: ImageButton
    private lateinit var editText: EditText
    private lateinit var prevButton: ImageButton
    private lateinit var reloadButton: ImageButton
    private lateinit var nextButton: ImageButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Initialize views
        webView = findViewById(R.id.webView)
        lottieLoading = findViewById(R.id.lottieLoading)
        lottieSearch = findViewById(R.id.lottieSearch)
        reloadSearch = findViewById(R.id.reloadSearch)
        cancelButton = findViewById(R.id.cancelButton)
        editText = findViewById(R.id.editText)
        prevButton = findViewById(R.id.prevButton)
        reloadButton = findViewById(R.id.reloadButton)
        nextButton = findViewById(R.id.nextButton)

        setupWebView()
        setupEditText()
        setupButtons()
    }

    private fun setupWebView() {
        webView.webViewClient = object : WebViewClient() {
            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)
                lottieSearch.visibility = View.GONE
                lottieLoading.visibility = View.GONE
                reloadSearch.visibility = View.GONE
                reloadButton.visibility = View.VISIBLE
                cancelButton.visibility = View.VISIBLE
            }

            override fun onReceivedError(
                view: WebView?,
                errorCode: Int,
                description: String?,
                failingUrl: String?
            ) {
                super.onReceivedError(view, errorCode, description, failingUrl)
                lottieLoading.visibility = View.GONE
                lottieSearch.visibility = View.GONE
                lottieSearch.setAnimation(R.raw.error_animation)
                lottieSearch.playAnimation()
                showToast("Error loading page")
            }
        }
    }

    private fun setupEditText() {
        editText.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                val url = editText.text.toString().trim()
                if (!url.startsWith("https://")) {
                    showToast("Invalid URL format")
                } else {
                    loadUrl(url)
                }
                true
            } else {
                false
            }
        }

        cancelButton.setOnClickListener {
            editText.text.clear()
            lottieSearch.visibility = View.GONE
        }
    }

    private fun setupButtons() {
        reloadButton.setOnClickListener {
            if (webView.url != null) {  // Ensure there is a page to reload
                webView.reload()
                reloadButton.visibility = View.GONE
                reloadButton.visibility = View.VISIBLE
            } else {
                showToast("No page to reload")
            }
        }

        prevButton.setOnClickListener {
            if (webView.canGoBack()) {
                webView.goBack()
            } else {
                startActivity(Intent(this, ErrorActivity::class.java))
                showToast("No previous page")
                lottieLoading.setAnimation(R.raw.error_animation)
                lottieLoading.playAnimation()
                lottieLoading.visibility = View.VISIBLE
                lottieLoading.visibility = View.GONE
            }
        }

        nextButton.setOnClickListener {
            if (webView.canGoForward()) {
                webView.goForward()
            } else {
                startActivity(Intent(this, ErrorActivity::class.java))
                showToast("No next page")
                lottieLoading.setAnimation(R.raw.error_animation)
                lottieLoading.playAnimation()
                lottieLoading.visibility = View.VISIBLE
                lottieLoading.visibility = View.GONE
            }
        }
    }

    private fun loadUrl(url: String) {
        webView.loadUrl(url)
        lottieSearch.playAnimation()
        cancelButton.visibility = View.GONE
        lottieLoading.visibility = View.VISIBLE
        lottieSearch.visibility = View.VISIBLE
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}