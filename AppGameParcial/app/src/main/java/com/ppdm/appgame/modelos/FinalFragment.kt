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

class FinalFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_final, container, false)

        // Suponiendo que tendrás un TextView para mostrar algún mensaje final
        val finalMessage: TextView = view.findViewById(R.id.finalMessage)
        finalMessage.text = "¡Has completado el cuestionario! Gracias por participar."

        // Botón para volver a la pantalla de inicio
        val restartButton: Button = view.findViewById(R.id.restartButton)
        restartButton.setOnClickListener {
            // Navegar de regreso al WelcomeFragment
            findNavController().navigate(R.id.action_finalFragment_to_welcomeFragment)
        }

        return view
    }
}
