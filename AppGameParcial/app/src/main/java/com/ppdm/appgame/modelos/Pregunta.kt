data class Pregunta(
    val texto: String,
    val imagen: Int, // Recurso drawable para la imagen
    val opciones: List<String>,
    val respuestaCorrecta: String
)
