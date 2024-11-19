package com.ppdm.appgame.modelos

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.ppdm.appgame.R

class LoginFragment : Fragment() {

    private lateinit var emailInput: EditText
    private lateinit var passwordInput: EditText
    private lateinit var loginButton: Button
    private lateinit var signUpText: TextView
    private lateinit var forgotPasswordText: TextView
    private lateinit var mAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mAuth = FirebaseAuth.getInstance() // Inicializamos FirebaseAuth
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_login, container, false)

        // Inicialización de vistas
        emailInput = view.findViewById(R.id.emailInput)
        passwordInput = view.findViewById(R.id.passwordInput)
        loginButton = view.findViewById(R.id.loginButton)
        signUpText = view.findViewById(R.id.signUpText)
        forgotPasswordText = view.findViewById(R.id.forgotPasswordText)

        // Configurar listeners
        loginButton.setOnClickListener { handleLogin() }
        signUpText.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_signUpFragment)
        }
        forgotPasswordText.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_forgotPasswordFragment)
        }

        return view
    }

    private fun handleLogin() {
        val email = emailInput.text.toString().trim()
        val password = passwordInput.text.toString().trim()

        // Validación de campos vacíos
        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(requireContext(), "Por favor, completa todos los campos.", Toast.LENGTH_SHORT).show()
            return
        }

        // Llamada a Firebase para autenticación
        validateLogin(email, password)
    }

    private fun validateLogin(email: String, password: String) {
        mAuth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    // Autenticación exitosa, redirigir al WelcomeFragment
                    findNavController().navigate(R.id.action_loginFragment_to_welcomeFragment)
                } else {
                    // Mostrar mensaje de error
                    val errorMessage = task.exception?.localizedMessage ?: "Error al iniciar sesión"
                    Toast.makeText(requireContext(), errorMessage, Toast.LENGTH_SHORT).show()
                }
            }
    }
}
