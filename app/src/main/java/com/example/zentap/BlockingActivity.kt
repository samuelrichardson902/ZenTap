package com.example.zentap

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity

class BlockingActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_blocking)

        // ... (your existing findViewById and setText code is fine) ...
        val blockedPackage = intent.getStringExtra("blocked_package")
        val appName = getAppName(blockedPackage ?: "")

        val titleText: TextView = findViewById(R.id.titleText)
        val messageText: TextView = findViewById(R.id.messageText)
        val closeButton: Button = findViewById(R.id.closeButton)

        titleText.text = getString(R.string.app_blocked_title)
        messageText.text = getString(R.string.app_blocked_message, appName)
        supportActionBar?.hide()


        // --- START OF FIX ---
        closeButton.setOnClickListener {
            goToHomeScreen()
        }

        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                goToHomeScreen()
            }
        }
        onBackPressedDispatcher.addCallback(this, callback)
        // --- END OF FIX ---
    }

    private fun goToHomeScreen() {
        val homeIntent = Intent(Intent.ACTION_MAIN).apply {
            addCategory(Intent.CATEGORY_HOME)
            flags = Intent.FLAG_ACTIVITY_NEW_TASK
        }
        startActivity(homeIntent)
        // This is more forceful than finish(). It destroys the task completely.
        finishAndRemoveTask()
    }

    private fun getAppName(packageName: String): String {
        return try {
            val appInfo = packageManager.getApplicationInfo(packageName, 0)
            packageManager.getApplicationLabel(appInfo).toString()
        } catch (e: Exception) {
            "This app"
        }
    }
}