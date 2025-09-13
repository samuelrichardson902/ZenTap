package com.zentap.android.util

import android.content.Context
import android.widget.Toast

object ToastManager {

    private var currentToast: Toast? = null

    /**
     * @param context The application context to avoid memory leaks.
     * @param message The text to display in the toast.
     */
    fun showToast(context: Context, message: String) {
        currentToast?.cancel()
        currentToast = Toast.makeText(context.applicationContext, message, Toast.LENGTH_SHORT)
        currentToast?.show()
    }
}