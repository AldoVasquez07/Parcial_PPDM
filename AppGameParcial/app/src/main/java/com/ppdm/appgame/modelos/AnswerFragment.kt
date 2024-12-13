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

class AnswerFragment: Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        saveInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_answer, container, false)
        // Obteniendo los argumentos pasados entre fragments
        val args = AnswerFragmentArgs.fromBundle(requireArguments())

        // Obteniendo TextoView para mostrar el mensaje de haber respondido
        val feedbackText: TextView = view.findViewById(R.id.feedbackText)

        // Consultando si la pregunta se ha respondido bien
        if (args.textView) {
            // Actualizando mensaje
            feedbackText.text = "¡Respuesta Correcta! Premio: 10 COINS"
            // Aumentando cantidad de monedas 
            PerfilHelper.perfil.monedas += 10
        }
        else {
            // Mostrando que se respondió incorrectamente
            feedbackText.text = "Respuesta Incorrecta"
        }
        
        val nextButton: Button = view.findViewById(R.id.nextButton)
        nextButton.setOnClickListener{
            // Preguntando si aún hay preguntas
            if (PreguntaHelper.index < PreguntaHelper.preguntas.size){
                // Redirigir a la pregunta
                findNavController().navigate(R.id.action_answerFragment_to_questionFragment)
            }
            else {
                // Reestablecer indice a 0 para seguir mostrando preguntas            
                PreguntaHelper.index=0
                findNavController().navigate(R.id.action_answerFragment_to_finalFragment)
            }
        }
        return view
    }
}
