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
    private var selectedOptionIndex: Int = -1

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_question, container, false)

        progressBar = view.findViewById(R.id.progressBar)

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
            selectedOptionIndex = savedInstanceState.getInt("selectedOptionIndex", -1)
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
                checkAnswerAndNavigate()
            }
        }.start()

        timerRunning = true
    }

    private fun checkAnswerAndNavigate() {
        val selectedOption = if (selectedOptionIndex != -1) {
            PreguntaHelper.preguntas[PreguntaHelper.index].opciones[selectedOptionIndex]
        } else null
        val isCorrect = selectedOption == PreguntaHelper.preguntas[PreguntaHelper.index].repuestaCorrecta

        val action = QuestionFragmentDirections.actionQuestionFragmentToAnswerFragment(isCorrect)
        PreguntaHelper.index += 1
        selectedOptionIndex = -1

        findNavController().navigate(action)
    }

    private fun displayQuestion(view: View) {
        val questionText: TextView = view.findViewById(R.id.questionText)
        val optionButtons = listOf(
            view.findViewById<Button>(R.id.option1),
            view.findViewById<Button>(R.id.option2),
            view.findViewById<Button>(R.id.option3),
            view.findViewById<Button>(R.id.option4)
        )

        questionText.text = PreguntaHelper.preguntas[PreguntaHelper.index].oracion

        for ((index, button) in optionButtons.withIndex()) {
            button.text = PreguntaHelper.preguntas[PreguntaHelper.index].opciones[index]
            button.setOnClickListener {
                selectedOptionIndex = index
                button.setBackgroundResource(R.drawable.option_button_background)
                optionButtons.forEachIndexed { i, btn ->
                    if (i != index) btn.setBackgroundResource(R.drawable.option_button_background_selected)
                }
            }
        }

        val submitButton: Button = view.findViewById(R.id.submitButton)
        submitButton.setOnClickListener {
            checkAnswerAndNavigate()
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt("remainingTime", progressBar.progress)
        outState.putBoolean("timerRunning", timerRunning)
        outState.putInt("selectedOptionIndex", selectedOptionIndex)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        if (::timer.isInitialized) {
            timer.cancel()
        }
        timerRunning = false
    }
}
