package com.ppdm.appgame.modelos
import Pregunta
import com.ppdm.appgame.R

object PreguntaHelper {
    val preguntas = listOf(
        Pregunta(
            texto = "¿Cuál es la capital de Francia?",
            imagen = R.drawable.icono_imagen,
            opciones = listOf("Madrid", "París", "Berlín", "Roma"),
            respuestaCorrecta = "París"
        ),
        Pregunta(
            texto = "¿Cuál es el planeta más grande?",
            imagen = R.drawable.icono_imagen,
            opciones = listOf("Marte", "Tierra", "Júpiter", "Urano"),
            respuestaCorrecta = "Jupiter"
        ),
        Pregunta(
            texto = "¿Cuándo se descubrió América?",
            imagen = R.drawable.icono_imagen,
            opciones = listOf("1491", "1493", "2025", "1942"),
            respuestaCorrecta = "1942"
        ),
        Pregunta(
            texto = "¿Cuánto es: 2 * 500 - 40 / 2?",
            imagen = R.drawable.icono_imagen,
            opciones = listOf("1020", "980", "2000", "970"),
            respuestaCorrecta = "980"
        )
    )
    var index: Int = 0
}
