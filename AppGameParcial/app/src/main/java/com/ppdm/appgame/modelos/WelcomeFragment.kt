package com.ppdm.appgame.modelos

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.ppdm.appgame.R

class WelcomeFragment: Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_welcome, container, false)
        val startButton: Button = view.findViewById(R.id.startButton)
        startButton.setOnClickListener{
            findNavController().navigate(R.id.action_welcomeFragment_to_questionFragment)
        }
        val rankingButton: Button = view.findViewById(R.id.rankingButton)
        rankingButton.setOnClickListener {
            findNavController().navigate(R.id.action_welcomeFragment_to_rankingFragment)
        }
        return view
    }
}