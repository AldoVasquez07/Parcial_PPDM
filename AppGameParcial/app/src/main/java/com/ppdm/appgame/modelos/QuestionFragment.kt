package com.ppdm.appgame.modelos

import android.os.Bundle
import android.os.CountDownTimer
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ProgressBar
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.ppdm.appgame.R
import kotlin.math.sign

class QuestionFragment : Fragment() {

    // Recibir el argumento pasado (índice de la pregunta) si lo hay
    val args: QuestionFragmentArgs by navArgs()

    // Variable para el índice actual de la pregunta
    private var indicePreguntaActual = 0
    private lateinit var progressBar: ProgressBar
    private lateinit var timer: CountDownTimer

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_question, container, false)

        // Actualizar el índice de la pregunta basado en el argumento recibido
        indicePreguntaActual = args.indicePregunta
        progressBar = view.findViewById(R.id.progressBar)
        startTimer()

        displayQuestion(view)
        return view
    }

    private fun startTimer() {
        progressBar.max = 30
        progressBar.progress = 30

        timer = object : CountDownTimer(30000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                val secondsLeft = (millisUntilFinished / 1000).toInt()
                progressBar.progress = secondsLeft
            }

            override fun onFinish() {
                // Navegar al finalFragment cuando se acabe el tiempo
                if (PreguntaHelper.index == PreguntaHelper.preguntas.size - 1){
                    PreguntaHelper.index = 0
                    findNavController().navigate(R.id.action_questionFragment_to_finalFragment)
                }
                else {
                    PreguntaHelper.index += 1
                    findNavController().navigate(R.id.action_questionFragment_to_questionFragment)
                }
            }
        }.start()
    }

    private fun displayQuestion(view: View) {
        val questionText: TextView = view.findViewById(R.id.questionText)
        val optionsGroup: RadioGroup = view.findViewById(R.id.optionsGroup)

        // Mostrar la pregunta actual
        questionText.text = PreguntaHelper.preguntas[PreguntaHelper.index].oracion

        // Limpiar el grupo de opciones y agregar nuevas opciones
        optionsGroup.removeAllViews()
        for (option in PreguntaHelper.preguntas[PreguntaHelper.index].opciones) {
            val radioButton = RadioButton(context)
            radioButton.text = option
            optionsGroup.addView(radioButton)
        }

        val submitButton: Button = view.findViewById(R.id.submitButton)
        submitButton.setOnClickListener {
            val selectedOption = optionsGroup.findViewById<RadioButton>(optionsGroup.checkedRadioButtonId)?.text.toString()
            val isCorrect = selectedOption == PreguntaHelper.preguntas[PreguntaHelper.index].repuestaCorrecta

            // Incrementar el índice de la pregunta para la siguiente pregunta
            val nextIndice = PreguntaHelper.index + 1

            // Navegar al fragmento de respuesta, pasando el estado de la respuesta
            val action = QuestionFragmentDirections.actionQuestionFragmentToAnswerFragment(isCorrect)

            // Si hay más preguntas, pasar el nuevo índice, de lo contrario navegar a finalFragment
            if (nextIndice < PreguntaHelper.preguntas.size) {
                PreguntaHelper.index = nextIndice  // Cambiar el índice aquí
                findNavController().navigate(action)
            } else {
                // Navegar al finalFragment cuando no hay más preguntas
                PreguntaHelper.index = 0
                findNavController().navigate(R.id.action_questionFragment_to_finalFragment)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        timer.cancel() // Cancelar el temporizador si se destruye el fragmento
    }

}
