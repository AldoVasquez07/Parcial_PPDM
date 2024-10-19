package com.ppdm.appgame.modelos

object PreguntaHelper {
    public val preguntas = listOf(
        Pregunta("¿Cuál es la capital de Francia?", listOf("París", "Londres", "Madrid", "Arequipa"), "París"),
        Pregunta("¿Cuál es el planeta más grande?", listOf("Marte", "Tierra", "Júpiter", "Urano"), "Júpiter"),
        Pregunta("¿Cuándo se descubrió América?", listOf("1491", "1493", "2025", "1942"), "1942"),
        Pregunta("¿Cuánto es: 2 * 500 - 40 / 2?", listOf("1020", "980", "2000", "970"), "980")
    )
}