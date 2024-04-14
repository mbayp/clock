package com.example.alarm_clock.presentation.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.alarm_clock.R
import kotlinx.coroutines.*

class StopwatchFragment : Fragment() {

    private lateinit var startButton: Button
    private lateinit var resetButton: Button
    private lateinit var stopwatchTextView: TextView

    private var isRunning = false
    private var elapsedTime = 0L

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_stopwatch, container, false)
        startButton = view.findViewById(R.id.start)
        resetButton = view.findViewById(R.id.stop)
        stopwatchTextView = view.findViewById(R.id.tv_sw)

        startButton.setOnClickListener {
            toggleStartStop()
        }

        resetButton.setOnClickListener {
            resetStopwatch()
        }

        return view
    }

    @SuppressLint("SetTextI18n")
    private fun toggleStartStop() {
        isRunning = !isRunning
        if (isRunning) {
            startButton.text = "НАЧАТЬ"
            startStopwatch()
        } else {
            startButton.text = "ПРОДОЛЖИТЬ"
            stopStopwatch()
        }
    }

    private fun startStopwatch() {
        CoroutineScope(Dispatchers.Main).launch {
            while (isRunning) {
                delay(1000)
                elapsedTime += 1000
                updateStopwatchText()
            }
        }
    }

    private fun stopStopwatch() {
    }

    @SuppressLint("SetTextI18n")
    private fun resetStopwatch() {
        isRunning = false
        elapsedTime = 0
        updateStopwatchText()
        startButton.text = "ПРОДОЛЖИТЬ"
    }

    private fun updateStopwatchText() {
        val seconds = elapsedTime / 1000
        val minutes = seconds / 60
        val remainingSeconds = seconds % 60
        stopwatchTextView.text = String.format("%02d:%02d", minutes, remainingSeconds)
    }
}
