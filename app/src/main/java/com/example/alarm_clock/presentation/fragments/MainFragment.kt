package com.example.alarm_clock.presentation.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.alarm_clock.R
import com.example.alarm_clock.databinding.FragmentMainBinding


class MainFragment : Fragment() {
    private lateinit var binding: FragmentMainBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMainBinding.inflate(inflater, container, false)

        binding.alarm.setOnClickListener {
            val navController = findNavController()
            navController.navigate(R.id.action_mainFragment_to_alarmFragment)
        }
        binding.stopwatch.setOnClickListener {
            val navController = findNavController()
            navController.navigate(R.id.action_mainFragment_to_stopwatchFragment)
        }
        binding.timer.setOnClickListener {
            val navController = findNavController()
            navController.navigate(R.id.action_mainFragment_to_timerFragment)
        }

        return binding.root
    }
}

