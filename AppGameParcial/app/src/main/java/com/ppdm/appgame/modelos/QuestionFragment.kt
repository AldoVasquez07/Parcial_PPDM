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

        // Obteniendo TextView de la cantidad de monedas que se integrará
        val coinCount: TextView = view.findViewById(R.id.coin_count)
        coinCount.text = PerfilHelper.perfil.monedas.toString()

        // Asignando el estado de la barra de progreso
        progressBar = view.findViewById(R.id.progressBar)

        // En caso de que se voltee el celular, todo queda intacto
        if (savedInstanceState != null) {
            // Tiempo establecido de 30 segundos por pregunta
            val remainingTime = savedInstanceState.getInt("remainingTime", 30)
            progressBar.progress = remainingTime
            timerRunning = savedInstanceState.getBoolean("timerRunning", false)

            if (timerRunning) {
                // En caso de ya esté listo comenzar el recuento
                startTimer(remainingTime)
            } else {
                // En caso de no estar, establecer el limite
                progressBar.max = 30
            }

            // Obtenemos la opción seleccionada, en caso de no seleccionar, se coloca -1
            selectedOptionIndex = savedInstanceState.getInt("selectedOptionIndex", -1)
            // Obteniendo la cantidad de monedas establecidas en el diseño
            monedasUsuario = savedInstanceState.getInt("monedasUsuario", 100)
        } else {
            // Inicializando Barra de Progreso
            startTimer()
        }

        // Desplegando Pregunta y comodines
        displayQuestion(view)
        setupComodines(view)

        return view
    }

    private fun startTimer(initialTime: Int = 30) {
        progressBar.max = 30
        progressBar.progress = initialTime

        // Convirtiendo tiempo en segundos
        timer = object : CountDownTimer(initialTime * 1000L, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                // Determinar la cantidad de segundos restantes
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

        // Comprobando que la opción seleccionada es la respuesta correcta o incorrecta
        val action = QuestionFragmentDirections.actionQuestionFragmentToAnswerFragment(
            isCorrect ?: PreguntaHelper.preguntas[PreguntaHelper.index].opciones[selectedOptionIndex] == PreguntaHelper.preguntas[PreguntaHelper.index].respuestaCorrecta
        )

        // Imprimiendo para depurar
        println(PreguntaHelper.index)

        // Cambiando de Pregunta
        PreguntaHelper.index += 1
        // Estableciendo un valor no usable en las opciones
        selectedOptionIndex = -1

        // Estableciendo booleano para que el usuario pueda utilizar un comodin por pregunta
        comodinUsado = false
        findNavController().navigate(action)
    }


    private fun displayQuestion(view: View) {
        // Aspectos Establecidos para el diseño del despliegue de Preguntas
        val questionText: TextView = view.findViewById(R.id.questionText)
        val questionImage: ImageView = view.findViewById(R.id.questionImage)
        val options = listOf<Button>(
            view.findViewById(R.id.option1),
            view.findViewById(R.id.option2),
            view.findViewById(R.id.option3),
            view.findViewById(R.id.option4)
        )

        // Guardando el Objeto de la pregunta que se mostrará
        val currentQuestion = PreguntaHelper.preguntas[PreguntaHelper.index]
        // Actualizando Pregunta
        questionText.text = currentQuestion.texto
        // Determinando Imagen de la Pregunta
        questionImage.setImageResource(currentQuestion.imagen)

        // Estableciendo las opciones de la pregunta
        options.forEachIndexed { index, button ->
            button.text = currentQuestion.opciones[index]
            button.setOnClickListener {
                selectedOptionIndex = index
                checkAnswerAndNavigate()
            }
        }
    }

    private fun setupComodines(view: View) {
        // Declarando los aspectos empleados en el diseño
        val comodin1 = view.findViewById<ImageView>(R.id.icono_comodin)
        val comodin2 = view.findViewById<ImageView>(R.id.icono_descartar_uno)
        val comodin3 = view.findViewById<ImageView>(R.id.icono_mitad)
        val coinCount: TextView = view.findViewById(R.id.coin_count)

        // Caso de dar click en el primer Comodin
        comodin1.setOnClickListener {
            // En caso de que no se haya usado un comodin antes
            if (comodinUsado) {
                // Si se usó un comodin antes, se muestra que no se puede usar
                showComodinAlreadyUsedMessage()
                return@setOnClickListener
            }
            // Si el usuario cuenta con 50 monedas o más para usar el comodin
            if (monedasUsuario >= 50) {
                // Descontando monedas al usuario
                monedasUsuario -= 50
                PerfilHelper.perfil.monedas -= 50
                // Estableciendo que ya se ha usuado un comodin
                comodinUsado = true
                // Actualizando el valor de las monedas
                coinCount.text = PerfilHelper.perfil.monedas.toString()
                // Dando Automaticamente una respuesta correcta
                checkAnswerAndNavigate(isCorrect = true)
            } else {
                // Mostrar si no tiene monedas suficientes
                showInsufficientCoinsMessage()
            }
        }

        // Caso de dar click en el segundo comodin
        comodin2.setOnClickListener {
            // Caso de no utilizar un Comodin antes
            if (comodinUsado) {
                // Si se ha utilizado, se retorna un mensaje de no poder usar otro comodin
                showComodinAlreadyUsedMessage()
                return@setOnClickListener
            }
            // Preguntando si el usuario cuenta con la cantidad de monedas suficientes para el comodin
            if (monedasUsuario >= 15) {
                // Descontando monedas al usuario
                monedasUsuario -= 15
                PerfilHelper.perfil.monedas -= 15
                // Estableciendo que el usuario ya ha utilizado un comodin
                comodinUsado = true
                // Descartando una opción incorrecta
                descartarUnaOpcionIncorrecta(view)
                // Actualizando cantidad de monedas
                coinCount.text = PerfilHelper.perfil.monedas.toString()
            } else {
                // Mostrando si el usuario no tiene la cantidad de monedas insuficientes
                showInsufficientCoinsMessage()
            }
        }

        // Estableciendo comodin 3
        comodin3.setOnClickListener {
            // Caso si un comodin ha sido utilizado antes
            if (comodinUsado) {
                // Retornando un mensaje de haber utilizado un comodin antes
                showComodinAlreadyUsedMessage()
                return@setOnClickListener
            }
            // Preguntando si el usuario tiene la cantidad de monedas suficientes
            if (monedasUsuario >= 30) {
                // Descontando monedas al usuario
                monedasUsuario -= 30
                PerfilHelper.perfil.monedas -= 30
                // Estableciendo si ya se ha utilizado un comodin
                comodinUsado = true
                // Descartando la mitad de las opciones
                descartarMitadOpciones(view)
                coinCount.text = PerfilHelper.perfil.monedas.toString()
            } else {
                // Mostrando si no tiene la cantidad de monedas suficientes
                showInsufficientCoinsMessage()
            }
        }
    }

    private fun descartarUnaOpcionIncorrecta(view: View) {
        // Obteniendo las opciones de la pregunta
        val options = listOf<Button>(
            view.findViewById(R.id.option1),
            view.findViewById(R.id.option2),
            view.findViewById(R.id.option3),
            view.findViewById(R.id.option4)
        )

        // Filtrando las opciones incorrectas
        val incorrectOptions = options.filterIndexed { index, _ ->
            PreguntaHelper.preguntas[PreguntaHelper.index].opciones[index] != PreguntaHelper.preguntas[PreguntaHelper.index].respuestaCorrecta
        }

        // Ocultando opción incorrecta
        incorrectOptions.firstOrNull()?.visibility = View.GONE
    }

    private fun descartarMitadOpciones(view: View) {
        // Obteniendo las opciones de la pregunta
        val options = listOf<Button>(
            view.findViewById(R.id.option1),
            view.findViewById(R.id.option2),
            view.findViewById(R.id.option3),
            view.findViewById(R.id.option4)
        )

        // Filtrando las opciones incorrectas
        val incorrectOptions = options.filterIndexed { index, _ ->
            PreguntaHelper.preguntas[PreguntaHelper.index].opciones[index] != PreguntaHelper.preguntas[PreguntaHelper.index].respuestaCorrecta
        }

        // Ocultando opción incorrecta
        incorrectOptions.take(2).forEach { it.visibility = View.GONE }
    }

    private fun showInsufficientCoinsMessage() {
        // Mostrando un mensaje semi transparante
        Toast.makeText(requireContext(), "No cuenta con las monedas suficientes.", Toast.LENGTH_SHORT).show()
    }

    private fun showComodinAlreadyUsedMessage() {
        // Mostrando un mensaje semi transparante
        Toast.makeText(requireContext(), "Ya has utilizado un comodín en esta pregunta.", Toast.LENGTH_SHORT).show()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        // Guardar aspectos en caso de voltear el celular
        super.onSaveInstanceState(outState)
        outState.putInt("remainingTime", progressBar.progress)
        outState.putBoolean("timerRunning", timerRunning)
        outState.putInt("selectedOptionIndex", selectedOptionIndex)
        outState.putInt("monedasUsuario", monedasUsuario)
    }

    override fun onDestroyView() {
        // Destruyendo aspectos de la vista
        super.onDestroyView()
        if (::timer.isInitialized) {
            timer.cancel()
        }
        timerRunning = false
    }
}
