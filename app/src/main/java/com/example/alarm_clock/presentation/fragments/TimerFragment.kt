package com.example.alarm_clock.presentation.fragments

import android.annotation.SuppressLint
import android.app.TimePickerDialog
import android.os.Bundle
import android.os.CountDownTimer
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.alarm_clock.R
import kotlinx.coroutines.*
import java.util.*

class TimerFragment : Fragment() {

    private lateinit var timerTextView: TextView
    private var countdownJob: Job? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_timer, container, false)
        timerTextView = view.findViewById(R.id.tv_timer)

        timerTextView.setOnClickListener {
            showTimePickerDialog()
        }

        return view
    }

    private fun showTimePickerDialog() {
        val currentTime = System.currentTimeMillis()
        val hour = TimeUtil.getHours(currentTime)
        val minute = TimeUtil.getMinutes(currentTime)

        val timePickerDialog = TimePickerDialog(
            requireContext(),
            TimePickerDialog.OnTimeSetListener { _, selectedHour, selectedMinute ->
                val selectedTime = TimeUtil.convertTimeToMillis(selectedHour, selectedMinute)
                timerTextView.text = String.format("%02d:%02d", selectedHour, selectedMinute)
                startCountdown(selectedTime)
            } as TimePickerDialog.OnTimeSetListener,
            hour,
            minute,
            true
        )
        timePickerDialog.show()

    }

    private fun startCountdown(selectedTime: Long) {
        countdownJob?.cancel()
        val currentTime = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }.timeInMillis

        val timeDifference = selectedTime - currentTime

        countdownJob = CoroutineScope(Dispatchers.Main).launch {
            object : CountDownTimer(timeDifference, 1000) {
                override fun onTick(millisUntilFinished: Long) {
                    val seconds = millisUntilFinished / 1000
                    val hours = seconds / 3600
                    val minutes = (seconds % 3600) / 60
                    val remainingSeconds = seconds % 60
                    timerTextView.text = String.format("%02d:%02d:%02d", hours, minutes, remainingSeconds)
                }

                override fun onFinish() {
                    timerTextView.text = "00:00:00"
                }
            }.start()
        }
    }


    override fun onDestroy() {
        super.onDestroy()
        countdownJob?.cancel()
    }
}

object TimeUtil {
    fun getHours(timeInMillis: Long): Int {
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = timeInMillis
        return calendar.get(Calendar.HOUR_OF_DAY)
    }

    fun getMinutes(timeInMillis: Long): Int {
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = timeInMillis
        return calendar.get(Calendar.MINUTE)
    }

    fun convertTimeToMillis(hour: Int, minute: Int): Long {
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.HOUR_OF_DAY, hour)
        calendar.set(Calendar.MINUTE, minute)
        calendar.set(Calendar.SECOND, 0)
        calendar.set(Calendar.MILLISECOND, 0)
        return if (hour < 12) {
            calendar.timeInMillis
        } else {
            calendar.add(Calendar.HOUR_OF_DAY, -12)
            calendar.timeInMillis
        }
    }
}
