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
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.ppdm.appgame.R

class QuestionFragment : Fragment() {

    private var indicePreguntaActual = 0
    private lateinit var progressBar: ProgressBar
    private lateinit var timer: CountDownTimer
    private var timerRunning: Boolean = false
    private var monedasUsuario: Int = 100 // Número inicial de monedas del usuario

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
                checkAnswerAndNavigate(false) // Tiempo agotado, respuesta incorrecta
            }
        }.start()

        timerRunning = true
    }

    private fun checkAnswerAndNavigate(isCorrect: Boolean) {
        val action = QuestionFragmentDirections.actionQuestionFragmentToAnswerFragment(isCorrect)
        indicePreguntaActual++
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

        val currentQuestion = PreguntaHelper.preguntas[indicePreguntaActual]

        questionText.text = currentQuestion.texto
        questionImage.setImageResource(currentQuestion.imagen)

        options.forEachIndexed { index, button ->
            button.text = currentQuestion.opciones[index]
            button.setOnClickListener {
                val isCorrect = currentQuestion.opciones[index] == currentQuestion.respuestaCorrecta
                checkAnswerAndNavigate(isCorrect)
            }
        }
    }

    private fun setupComodines(view: View) {
        val comodin1 = view.findViewById<ImageView>(R.id.icono_comodin)
        val comodin2 = view.findViewById<ImageView>(R.id.icono_descartar_uno)
        val comodin3 = view.findViewById<ImageView>(R.id.icono_mitad)
        val coinCount: TextView = view.findViewById(R.id.coin_count)

        coinCount.text = monedasUsuario.toString()

        comodin1.setOnClickListener {
            if (monedasUsuario >= 50) {
                monedasUsuario -= 50
                coinCount.text = monedasUsuario.toString()
                checkAnswerAndNavigate(true) // Usar comodín para responder correctamente
            } else {
                showInsufficientCoinsMessage()
            }
        }

        comodin2.setOnClickListener {
            if (monedasUsuario >= 15) {
                monedasUsuario -= 15
                coinCount.text = monedasUsuario.toString()
                descartarUnaOpcionIncorrecta(view)
            } else {
                showInsufficientCoinsMessage()
            }
        }

        comodin3.setOnClickListener {
            if (monedasUsuario >= 30) {
                monedasUsuario -= 30
                coinCount.text = monedasUsuario.toString()
                descartarMitadOpciones(view)
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
            PreguntaHelper.preguntas[indicePreguntaActual].opciones[index] != PreguntaHelper.preguntas[indicePreguntaActual].respuestaCorrecta
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
            PreguntaHelper.preguntas[indicePreguntaActual].opciones[index] != PreguntaHelper.preguntas[indicePreguntaActual].respuestaCorrecta
        }

        incorrectOptions.take(incorrectOptions.size / 2).forEach { it.visibility = View.GONE }
    }

    private fun showInsufficientCoinsMessage() {
        // Mostrar mensaje indicando que no hay suficientes monedas.
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt("remainingTime", progressBar.progress)
        outState.putBoolean("timerRunning", timerRunning)
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
