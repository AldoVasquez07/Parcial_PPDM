package com.ppdm.appgame.modelos

import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.ppdm.appgame.R

class ForgotPasswordFragment : Fragment() {

    private lateinit var emailInput: EditText
    private lateinit var resetPasswordButton: Button
    private lateinit var mAuth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_forgot_password, container, false)
        mAuth = FirebaseAuth.getInstance()

        emailInput = view.findViewById(R.id.emailInput)
        resetPasswordButton = view.findViewById(R.id.resetPasswordButton)

        resetPasswordButton.setOnClickListener {
            val email = emailInput.text.toString()

            if (validateEmail(email)) {
                resetPassword(email)
            }
        }

        return view
    }

    private fun validateEmail(email: String): Boolean {
        if (TextUtils.isEmpty(email)) {
            Toast.makeText(requireContext(), "Por favor, introduce tu correo", Toast.LENGTH_SHORT).show()
            return false
        }

        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Toast.makeText(requireContext(), "Correo no válido", Toast.LENGTH_SHORT).show()
            return false
        }

        return true
    }

    private fun resetPassword(email: String) {
        mAuth.sendPasswordResetEmail(email)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Toast.makeText(requireContext(), "Correo enviado para restablecer contraseña", Toast.LENGTH_SHORT).show()
                    findNavController().navigate(R.id.action_forgotPasswordFragment_to_loginFragment)
                } else {
                    Toast.makeText(requireContext(), "Error: ${task.exception?.localizedMessage}", Toast.LENGTH_SHORT).show()
                }
            }
    }
}
