package com.ppdm.appgame.modelos

import android.os.Bundle
import android.os.CountDownTimer
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.ppdm.appgame.R

class QuestionFragment : Fragment() {

    // private var PreguntaHelper.index = 0
    private lateinit var progressBar: ProgressBar
    private lateinit var timer: CountDownTimer
    private var timerRunning: Boolean = false
    private var selectedOptionIndex: Int = -1
    private var monedasUsuario: Int = 100 // Número inicial de monedas del usuario
    private var comodinUsado: Boolean = false

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

            selectedOptionIndex = savedInstanceState.getInt("selectedOptionIndex", -1)
            monedasUsuario = savedInstanceState.getInt("monedasUsuario", 100)
        } else {
            startTimer()
        }

        displayQuestion(view)
        setupComodines(view)

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

    private fun checkAnswerAndNavigate(isCorrect: Boolean? = null) {
        // Si no ha seleccionado ninguna opción, asignar automáticamente la primera
        if (selectedOptionIndex == -1) {
            selectedOptionIndex = 0
        }

        val action = QuestionFragmentDirections.actionQuestionFragmentToAnswerFragment(
            isCorrect ?: PreguntaHelper.preguntas[PreguntaHelper.index].opciones[selectedOptionIndex] == PreguntaHelper.preguntas[PreguntaHelper.index].respuestaCorrecta
        )

        // PreguntaHelper.index++
        println(PreguntaHelper.index)

        PreguntaHelper.index += 1
        selectedOptionIndex = -1

        comodinUsado = false
        findNavController().navigate(action)
    }


    private fun displayQuestion(view: View) {
        val questionText: TextView = view.findViewById(R.id.questionText)
        val questionImage: ImageView = view.findViewById(R.id.questionImage)
        val options = listOf<Button>(
            view.findViewById(R.id.option1),
            view.findViewById(R.id.option2),
            view.findViewById(R.id.option3),
            view.findViewById(R.id.option4)
        )

        val currentQuestion = PreguntaHelper.preguntas[PreguntaHelper.index]

        questionText.text = currentQuestion.texto
        questionImage.setImageResource(currentQuestion.imagen)

        options.forEachIndexed { index, button ->
            button.text = currentQuestion.opciones[index]
            button.setOnClickListener {
                selectedOptionIndex = index
                checkAnswerAndNavigate()
            }
        }
    }

    private fun setupComodines(view: View) {
        val comodin1 = view.findViewById<ImageView>(R.id.icono_comodin)
        val comodin2 = view.findViewById<ImageView>(R.id.icono_descartar_uno)
        val comodin3 = view.findViewById<ImageView>(R.id.icono_mitad)
        val coinCount: TextView = view.findViewById(R.id.coin_count)

        comodin1.setOnClickListener {
            if (comodinUsado) {
                showComodinAlreadyUsedMessage()
                return@setOnClickListener
            }
            if (monedasUsuario >= 50) {
                monedasUsuario -= 50
                comodinUsado = true
                coinCount.text = monedasUsuario.toString()
                checkAnswerAndNavigate(isCorrect = true)
            } else {
                showInsufficientCoinsMessage()
            }
        }

        comodin2.setOnClickListener {
            if (comodinUsado) {
                showComodinAlreadyUsedMessage()
                return@setOnClickListener
            }
            if (monedasUsuario >= 15) {
                monedasUsuario -= 15
                comodinUsado = true
                descartarUnaOpcionIncorrecta(view)
                coinCount.text = monedasUsuario.toString()
            } else {
                showInsufficientCoinsMessage()
            }
        }

        comodin3.setOnClickListener {
            if (comodinUsado) {
                showComodinAlreadyUsedMessage()
                return@setOnClickListener
            }
            if (monedasUsuario >= 30) {
                monedasUsuario -= 30
                comodinUsado = true
                descartarMitadOpciones(view)
                coinCount.text = monedasUsuario.toString()
            } else {
                showInsufficientCoinsMessage()
            }
        }
    }

    private fun descartarUnaOpcionIncorrecta(view: View) {
        val options = listOf<Button>(
            view.findViewById(R.id.option1),
            view.findViewById(R.id.option2),
            view.findViewById(R.id.option3),
            view.findViewById(R.id.option4)
        )

        val incorrectOptions = options.filterIndexed { index, _ ->
            PreguntaHelper.preguntas[PreguntaHelper.index].opciones[index] != PreguntaHelper.preguntas[PreguntaHelper.index].respuestaCorrecta
        }

        incorrectOptions.firstOrNull()?.visibility = View.GONE
    }

    private fun descartarMitadOpciones(view: View) {
        val options = listOf<Button>(
            view.findViewById(R.id.option1),
            view.findViewById(R.id.option2),
            view.findViewById(R.id.option3),
            view.findViewById(R.id.option4)
        )

        val incorrectOptions = options.filterIndexed { index, _ ->
            PreguntaHelper.preguntas[PreguntaHelper.index].opciones[index] != PreguntaHelper.preguntas[PreguntaHelper.index].respuestaCorrecta
        }

        incorrectOptions.take(2).forEach { it.visibility = View.GONE }
    }

    private fun showInsufficientCoinsMessage() {
        Toast.makeText(requireContext(), "No cuenta con las monedas suficientes.", Toast.LENGTH_SHORT).show()
    }

    private fun showComodinAlreadyUsedMessage() {
        Toast.makeText(requireContext(), "Ya has utilizado un comodín en esta pregunta.", Toast.LENGTH_SHORT).show()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt("remainingTime", progressBar.progress)
        outState.putBoolean("timerRunning", timerRunning)
        outState.putInt("selectedOptionIndex", selectedOptionIndex)
        outState.putInt("monedasUsuario", monedasUsuario)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        if (::timer.isInitialized) {
            timer.cancel()
        }
        timerRunning = false
    }
}
