package com.ppdm.appgame.modelos

data class Pregunta(
    val oracion: String,
    val opciones: List<String>,
    val repuestaCorrecta: String
)
