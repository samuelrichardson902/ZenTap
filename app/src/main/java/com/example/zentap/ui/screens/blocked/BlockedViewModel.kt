package com.example.zentap.ui.screens.blocked

import android.app.Application
import android.os.CountDownTimer
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import com.example.zentap.data.AppSettings
import com.example.zentap.data.BlockedMessages
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.util.concurrent.TimeUnit

// Represents the different states our screen can be in
sealed class BlockedScreenState {
    object Idle : BlockedScreenState()
    data class CountingDown(val timeLeftFormatted: String) : BlockedScreenState()
}

class BlockedViewModel(application: Application) : AndroidViewModel(application) {

    private val _screenState = MutableStateFlow<BlockedScreenState>(BlockedScreenState.Idle)
    val screenState = _screenState.asStateFlow()

    private val _unlockEvent = MutableStateFlow(false)
    val unlockEvent = _unlockEvent.asStateFlow()

    // --- START OF CHANGE ---
    // The ViewModel now holds the random message to ensure it doesn't
    // change on screen rotation or other recompositions.
    private val _blockedMessage = MutableStateFlow(BlockedMessages.getRandomMessage())
    val blockedMessage = _blockedMessage.asStateFlow()
    // --- END OF CHANGE ---

    private var timer: CountDownTimer? = null
    private var remainingTimeInMillis: Long = AppSettings.getWaitTime(getApplication())
    private var isTimerRunning = false

    fun startCountdown() {
        if (isTimerRunning) return
        _screenState.value = BlockedScreenState.CountingDown(formatTime(remainingTimeInMillis))
        resumeCountdown()
    }

    fun pauseCountdown() {
        if (!isTimerRunning) return
        timer?.cancel()
        isTimerRunning = false
    }

    fun resumeCountdown() {
        if (isTimerRunning || _screenState.value is BlockedScreenState.Idle) return
        isTimerRunning = true
        timer = object : CountDownTimer(remainingTimeInMillis, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                remainingTimeInMillis = millisUntilFinished
                _screenState.value = BlockedScreenState.CountingDown(formatTime(millisUntilFinished))
            }

            override fun onFinish() {
                _unlockEvent.value = true
                resetTimer()
            }
        }.start()
    }

    private fun resetTimer() {
        timer?.cancel()
        remainingTimeInMillis = AppSettings.getWaitTime(getApplication())
        isTimerRunning = false
        _screenState.value = BlockedScreenState.Idle
        // Get a new random message for the next time the screen is shown
        _blockedMessage.value = BlockedMessages.getRandomMessage()
    }

    private fun formatTime(millis: Long): String {
        return String.format("%02d:%02d",
            TimeUnit.MILLISECONDS.toMinutes(millis) % 60,
            TimeUnit.MILLISECONDS.toSeconds(millis) % 60
        )
    }

    override fun onCleared() {
        timer?.cancel()
        super.onCleared()
    }
}