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
        }
    }
}