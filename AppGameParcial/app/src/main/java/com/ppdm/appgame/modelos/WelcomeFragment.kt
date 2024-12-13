package com.ppdm.appgame.modelos

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.ppdm.appgame.R

class WelcomeFragment: Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_welcome, container, false)
        // Estableciendo nombre del usuairo
        val welcomeText: TextView = view.findViewById(R.id.welcomeText)
        welcomeText.text = PerfilHelper.perfil.nombre

        // Estableciendo las monedas del usuario
        val coins: TextView = view.findViewById(R.id.coin_count)
        coins.text = PerfilHelper.perfil.monedas.toString()

        // Estableciendo puntaje del usuario
        val points: TextView = view.findViewById(R.id.puntaje)
        points.text = PerfilHelper.perfil.puntaje.toString()

        val startButton: Button = view.findViewById(R.id.startButton)
        startButton.setOnClickListener{
            // Pasar al aspecto del formulario si se da click al botón para jugar
            findNavController().navigate(R.id.action_welcomeFragment_to_questionFragment)
        }
        val rankingButton: Button = view.findViewById(R.id.rankingButton)
        rankingButton.setOnClickListener {
            // Pasar al aspecto del ranking si se da click al botón ranking
            findNavController().navigate(R.id.action_welcomeFragment_to_rankingFragment)
        }
        return view
    }
}
