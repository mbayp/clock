package com.example.alarm_clock.presentation.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TimePicker
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.alarm_clock.R
import kotlinx.coroutines.*
import java.util.*

class AlarmFragment : Fragment() {

    private lateinit var timePicker: TimePicker
    private lateinit var setUpButton: Button

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_alarm, container, false)
        timePicker = view.findViewById(R.id.timePicker)
        setUpButton = view.findViewById(R.id.setUp)

        setUpButton.setOnClickListener {
            val selectedTime = getSelectedTimeInMillis()
            setAlarm(selectedTime)
        }

        return view
    }

    private fun getSelectedTimeInMillis(): Long {
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.HOUR_OF_DAY, timePicker.hour)
        calendar.set(Calendar.MINUTE, timePicker.minute)
        calendar.set(Calendar.SECOND, 0)
        calendar.set(Calendar.MILLISECOND, 0)
        val currentTime = System.currentTimeMillis()
        val selectedTime = calendar.timeInMillis
        return if (selectedTime <= currentTime) {
            selectedTime + 24 * 60 * 60 * 1000
        } else {
            selectedTime
        }
    }

    private fun setAlarm(selectedTime: Long) {
        CoroutineScope(Dispatchers.Main).launch {
            val delayMillis = selectedTime - System.currentTimeMillis()
            delay(delayMillis)
            showAlarmToast()
        }
    }

    private fun showAlarmToast() {
        Toast.makeText(requireContext(), "Time to wake up!", Toast.LENGTH_SHORT).show()
    }
}
