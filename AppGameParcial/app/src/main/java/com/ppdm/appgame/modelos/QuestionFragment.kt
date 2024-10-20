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
import androidx.navigation.NavDirections
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.ppdm.appgame.R

class QuestionFragment : Fragment() {

    private val args: QuestionFragmentArgs by navArgs()
    private var indicePreguntaActual = 0
    private lateinit var progressBar: ProgressBar
    private lateinit var timer: CountDownTimer
    private var timerRunning: Boolean = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_question, container, false)

        progressBar = view.findViewById(R.id.progressBar)

        // Recuperar el índice de la pregunta y el tiempo restante si existe
        if (savedInstanceState != null) {
            val remainingTime = savedInstanceState.getInt("remainingTime", 30)
            progressBar.progress = remainingTime
            timerRunning = savedInstanceState.getBoolean("timerRunning", false)

            if (timerRunning) {
                startTimer(remainingTime)
            } else {
                progressBar.max = 30
            }

            // Recuperar la opción seleccionada
            PreguntaHelper.opcion = savedInstanceState.getInt("selectedOption", -1)
        } else {
            startTimer()
        }

        displayQuestion(view)
        return view
    }

    private fun startTimer(initialTime: Int = 30) {
        progressBar.max = 30
        progressBar.progress = initialTime

        timer = object : CountDownTimer(initialTime * 1000L, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                val secondsLeft = (millisUntilFinished / 1000).toInt()
                progressBar.progress = secondsLeft
            }

            override fun onFinish() {
                // Al finalizar, comprobar la respuesta y navegar a la pantalla de respuesta
                checkAnswerAndNavigate()
            }
        }.start()

        timerRunning = true
    }

    private fun checkAnswerAndNavigate() {
        val selectedOption = PreguntaHelper.preguntas[PreguntaHelper.index].opciones[PreguntaHelper.opcion]
        val isCorrect = selectedOption == PreguntaHelper.preguntas[PreguntaHelper.index].repuestaCorrecta

        // Navegar al fragmento de respuesta, pasando el estado de la respuesta
        val action = QuestionFragmentDirections.actionQuestionFragmentToAnswerFragment(isCorrect)
        PreguntaHelper.index += 1
        PreguntaHelper.opcion = 0
        // Navegar al fragmento de respuesta
        findNavController().navigate(action)
    }

    private fun displayQuestion(view: View) {
        val questionText: TextView = view.findViewById(R.id.questionText)
        val optionsGroup: RadioGroup = view.findViewById(R.id.optionsGroup)

        // Mostrar la pregunta actual
        questionText.text = PreguntaHelper.preguntas[PreguntaHelper.index].oracion

        // Limpiar el grupo de opciones y agregar nuevas opciones
        optionsGroup.removeAllViews()
        for ((index, option) in PreguntaHelper.preguntas[PreguntaHelper.index].opciones.withIndex()) {
            val radioButton = RadioButton(context)
            radioButton.text = option
            radioButton.id = View.generateViewId() // Asignar un ID único al RadioButton
            optionsGroup.addView(radioButton)

            // Si la opción coincide con la guardada, marcarla como seleccionada
            if (index == PreguntaHelper.opcion) {
                optionsGroup.check(radioButton.id)
            }
        }

        // Listener para saber qué opción fue seleccionada
        optionsGroup.setOnCheckedChangeListener { _, checkedId ->
            val selectedRadioButton: RadioButton? = view.findViewById(checkedId)
            PreguntaHelper.opcion = optionsGroup.indexOfChild(selectedRadioButton) // Actualizar la opción seleccionada
        }

        val submitButton: Button = view.findViewById(R.id.submitButton)
        submitButton.setOnClickListener {
            // Verificar la respuesta al hacer click en el botón de envío
            checkAnswerAndNavigate()
        }
    }

    private fun navigateToNextQuestion(action: NavDirections? = null) {
        if (PreguntaHelper.index < PreguntaHelper.preguntas.size - 1) {
            PreguntaHelper.index += 1
            if (action != null) {
                findNavController().navigate(action)
            } else {
                resetTimer()
                displayQuestion(requireView())
            }
        } else {
            PreguntaHelper.index = 0
            findNavController().navigate(R.id.action_questionFragment_to_finalFragment)
        }
    }

    private fun resetTimer() {
        if (::timer.isInitialized) {
            timer.cancel() // Cancelar el temporizador actual
        }
        startTimer() // Iniciar un nuevo temporizador
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        if (::progressBar.isInitialized) { // Verificar si progressBar está inicializado
            outState.putInt("remainingTime", progressBar.progress)
        }
        outState.putBoolean("timerRunning", timerRunning)

        // Guardar la opción seleccionada
        outState.putInt("selectedOption", PreguntaHelper.opcion)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        if (::timer.isInitialized) {
            timer.cancel() // Cancelar el temporizador si se destruye el fragmento
        }
        timerRunning = false // Asegurarse de que el temporizador no esté en ejecución
    }
}
