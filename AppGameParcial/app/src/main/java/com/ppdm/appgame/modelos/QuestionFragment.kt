package com.ppdm.appgame.modelos

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.ppdm.appgame.R

class QuestionFragment: Fragment() {
    private val preguntas = PreguntaHelper.preguntas
    private var indicePreguntaActual = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_question, container, false)
        displayQuestion(view)
        return view
    }

    private fun displayQuestion(view: View){
        
    }
}