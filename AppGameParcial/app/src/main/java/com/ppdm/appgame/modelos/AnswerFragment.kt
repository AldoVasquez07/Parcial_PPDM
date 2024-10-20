package com.ppdm.appgame.modelos

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.ppdm.appgame.R

class AnswerFragment: Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        saveInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_answer, container, false)
        val args = AnswerFragmentArgs.fromBundle(requireArguments())

        val feedbackText: TextView = view.findViewById(R.id.feedbackText)

        if (args.textView) {
            feedbackText.text = "Respuesta Correcta"
        }
        else {
            feedbackText.text = "Respuesta Incorrecta"
        }
        val nextButton: Button = view.findViewById(R.id.nextButton)
        nextButton.setOnClickListener{
            if (PreguntaHelper.index < PreguntaHelper.preguntas.size){
                findNavController().navigate(R.id.action_answerFragment_to_questionFragment)
            }
            else {
                findNavController().navigate(R.id.action_answerFragment_to_finalFragment)
            }

        }
        return view
    }
}