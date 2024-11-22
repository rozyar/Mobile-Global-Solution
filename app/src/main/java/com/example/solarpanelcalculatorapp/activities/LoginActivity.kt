package com.example.solarpanelcalculatorapp.activities

import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.solarpanelcalculatorapp.R
import com.example.solarpanelcalculatorapp.api.RetrofitInstance
import com.example.solarpanelcalculatorapp.models.LoginRequest
import com.example.solarpanelcalculatorapp.utils.SessionManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class LoginActivity : AppCompatActivity() {

    private lateinit var sessionManager: SessionManager
    private lateinit var editTextEmail: EditText
    private lateinit var editTextPassword: EditText
    private lateinit var buttonLogin: Button
    private lateinit var textViewRegister: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Verifica se o usuário já está logado
        sessionManager = SessionManager(this)
        if (sessionManager.fetchAuthToken() != null) {
            navigateToMain()
        }

        setContentView(R.layout.activity_login)

        editTextEmail = findViewById(R.id.editTextEmail)
        editTextPassword = findViewById(R.id.editTextPassword)
        buttonLogin = findViewById(R.id.buttonLogin)
        textViewRegister = findViewById(R.id.textViewRegister)

        buttonLogin.setOnClickListener {
            val email = editTextEmail.text.toString()
            val password = editTextPassword.text.toString()

            if (email.isNotEmpty() && password.isNotEmpty()) {
                authenticateUser(email, password)
            } else {
                Toast.makeText(this, "Por favor, preencha todos os campos", Toast.LENGTH_SHORT).show()
            }
        }

        textViewRegister.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }
    }

    private fun authenticateUser(email: String, password: String) {
        val loginRequest = LoginRequest(email, password)

        CoroutineScope(Dispatchers.IO).launch {
            val response = RetrofitInstance.api.authenticate(loginRequest)

            withContext(Dispatchers.Main) {
                if (response.isSuccessful && response.body() != null) {
                    val loginResponse = response.body()
                    // Salvar o token
                    sessionManager.saveAuthToken(loginResponse!!.token)
                    navigateToMain()
                } else {
                    val errorMessage = when (response.code()) {
                        401 -> "Credenciais inválidas. Por favor, verifique seu email e senha."
                        500 -> "Erro no servidor. Tente novamente mais tarde."
                        else -> "Erro ao fazer login: ${response.message()}"
                    }
                    Toast.makeText(
                        this@LoginActivity,
                        errorMessage,
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

    private fun navigateToMain() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }
}
