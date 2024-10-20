package com.ppdm.appgame.modelos

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.ppdm.appgame.R

class QuestionFragment: Fragment() {
    private val preguntas = PreguntaHelper.preguntas
    private var indicePreguntaActual = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_question, container, false)
        displayQuestion(view)
        return view
    }

    private fun displayQuestion(view: View){
        val questionText: TextView = view.findViewById(R.id.questionText)
        val optionsGroup: RadioGroup = view.findViewById(R.id.optionsGroup)

        // Integrando Oración Establecida para la Pregunta
        questionText.text = preguntas[indicePreguntaActual].oracion

        // Integrando Opciones de la Pregunta
        optionsGroup.removeAllViews()
        for (option in preguntas[indicePreguntaActual].opciones){
            val radioButton = RadioButton(context)
            radioButton.text = option
            optionsGroup.addView(radioButton)
        }

        val submitButton: Button = view.findViewById(R.id.submitButton)
        submitButton.setOnClickListener{
            val selectedOption = optionsGroup.findViewById<RadioButton>(optionsGroup.checkedRadioButtonId)?.text.toString()
            val isCorrect = selectedOption == preguntas[indicePreguntaActual].repuestaCorrecta
            val action = QuestionFragmentDirections.actionQuestionFragmentToAnswerFragment(isCorrect)
            findNavController().navigate(action)

            // Incrementar el índice para mostrar la siguiente pregunta
            indicePreguntaActual++

            // Verificar si hay más preguntas, sino reiniciar o finalizar
            if (indicePreguntaActual < preguntas.size) {
                // Mostrar la siguiente pregunta
                displayQuestion(view)
            } else {
                // Si no hay más preguntas, navega a un fragmento final o reinicia el cuestionario
                // Aquí puedes navegar a una pantalla final o reiniciar el cuestionario
                indicePreguntaActual = 0 // Si deseas reiniciar el cuestionario
                // O navega a un fragmento final, por ejemplo:
                // findNavController().navigate(R.id.action_questionFragment_to_finalFragment)
            }
        }
    }
}