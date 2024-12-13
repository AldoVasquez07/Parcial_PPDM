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

class SignUpFragment : Fragment() {

    private lateinit var emailInput: EditText
    private lateinit var passwordInput: EditText
    private lateinit var confirmPasswordInput: EditText
    private lateinit var signUpButton: Button
    private lateinit var mAuth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_sign_up, container, false)
        // Obteniendo objeto Firebase
        mAuth = FirebaseAuth.getInstance()

        // Declarando aspectos del diseño
        emailInput = view.findViewById(R.id.emailInput)
        passwordInput = view.findViewById(R.id.passwordInput)
        confirmPasswordInput = view.findViewById(R.id.confirmPasswordInput)
        signUpButton = view.findViewById(R.id.signUpButton)

        // Listener para realizar el registro de usuario
        signUpButton.setOnClickListener {
            val email = emailInput.text.toString()
            val password = passwordInput.text.toString()
            val confirmPassword = confirmPasswordInput.text.toString()

            // Validando aspectos ingresados para el registro de usuario
            if (validateInput(email, password, confirmPassword)) {
                registerUser(email, password)
            }
        }

        return view
    }

    private fun validateInput(email: String, password: String, confirmPassword: String): Boolean {
        // Caso de tener algun campo vacío
        if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password) || TextUtils.isEmpty(confirmPassword)) {
            Toast.makeText(requireContext(), "Por favor, completa todos los campos", Toast.LENGTH_SHORT).show()
            return false
        }

        // Utilizando servicio para verificar si el correo es válido
        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Toast.makeText(requireContext(), "Correo no válido", Toast.LENGTH_SHORT).show()
            return false
        }

        // Determinando rudeza de la contraseña
        if (password.length < 6) {
            Toast.makeText(requireContext(), "La contraseña debe tener al menos 6 caracteres", Toast.LENGTH_SHORT).show()
            return false
        }

        // Confirmando que las contraseñas sean iguales
        if (password != confirmPassword) {
            Toast.makeText(requireContext(), "Las contraseñas no coinciden", Toast.LENGTH_SHORT).show()
            return false
        }

        return true
    }

    private fun registerUser(email: String, password: String) {
        // Registrando usuario en firebase
        mAuth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Toast.makeText(requireContext(), "Registro exitoso", Toast.LENGTH_SHORT).show()
                    findNavController().navigate(R.id.action_signUpFragment_to_loginFragment)
                } else {
                    Toast.makeText(requireContext(), "Error: ${task.exception?.localizedMessage}", Toast.LENGTH_SHORT).show()
                }
            }
    }
}
