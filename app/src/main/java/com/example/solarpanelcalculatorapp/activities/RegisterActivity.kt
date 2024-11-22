package com.example.solarpanelcalculatorapp.activities

import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.solarpanelcalculatorapp.R
import com.example.solarpanelcalculatorapp.api.RetrofitInstance
import com.example.solarpanelcalculatorapp.models.RegisterRequest
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class RegisterActivity : AppCompatActivity() {

    private lateinit var editTextName: EditText
    private lateinit var editTextEmail: EditText
    private lateinit var editTextPassword: EditText
    private lateinit var buttonRegister: Button
    private lateinit var buttonBackToLogin : Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        editTextName = findViewById(R.id.editTextName)
        editTextEmail = findViewById(R.id.editTextEmail)
        editTextPassword = findViewById(R.id.editTextPassword)
        buttonRegister = findViewById(R.id.buttonRegister)
        buttonBackToLogin = findViewById(R.id.buttonBackToLogin)

        buttonRegister.setOnClickListener {
            val name = editTextName.text.toString()
            val email = editTextEmail.text.toString()
            val password = editTextPassword.text.toString()

            // Validação dos campos
            if (validateFields(name, email, password)) {
                registerUser(name, email, password)
            }
        }

        buttonBackToLogin.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun validateFields(name: String, email: String, password: String): Boolean {
        var isValid = true

        // Validação do nome
        if (name.isEmpty()) {
            editTextName.error = "O nome é obrigatório"
            isValid = false
        }

        // Validação do email
        if (email.isEmpty()) {
            editTextEmail.error = "O email é obrigatório"
            isValid = false
        } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            editTextEmail.error = "Email inválido"
            isValid = false
        }

        // Validação da senha
        val passwordRegex = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).{8,}$".toRegex()
        if (password.isEmpty()) {
            editTextPassword.error = "A senha é obrigatória"
            isValid = false
        } else if (!passwordRegex.matches(password)) {
            editTextPassword.error = """
                A senha deve conter:
                - Pelo menos 8 caracteres
                - Uma letra maiúscula
                - Uma letra minúscula
                - Um número
            """.trimIndent()
            isValid = false
        }

        return isValid
    }

    private fun registerUser(name: String, email: String, password: String) {
        val registerRequest = RegisterRequest(name, email, password)

        CoroutineScope(Dispatchers.IO).launch {
            val response = RetrofitInstance.api.register(registerRequest)

            withContext(Dispatchers.Main) {
                if (response.isSuccessful) {
                    Toast.makeText(this@RegisterActivity, "Registro bem-sucedido!", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this@RegisterActivity, LoginActivity::class.java)
                    startActivity(intent)
                    finish()
                } else {
                    Toast.makeText(
                        this@RegisterActivity,
                        "Erro ao registrar: ${response.message()}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }
}
