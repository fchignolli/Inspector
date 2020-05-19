package com.example.inspector.Controller

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.view.View
import android.widget.Toast
import com.example.inspector.Model.User
import com.example.inspector.R
import com.example.inspector.Utils.Utils
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.android.synthetic.main.activity_register.*

class RegisterActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private val TAG = "RegisterActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
    }

    fun onClickRegiter(view: View) {
        validateNameField()
        validateEmailField()
        validatePasswordField()
        val email = emailTextInput.editText?.text.toString()
        val password = passwordTextinput.editText?.text.toString()
        register(email, password)
    }

    private fun register(email: String, password: String) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    Utils.alert(baseContext, "Registrado com sucesso.")
                    updateUI(auth.currentUser)
                } else {
                    Utils.alert(baseContext, "O Registro falhou tente novamente.")
                    updateUI(null)
                }
            }
    }

    private fun updateUI(currentUser: FirebaseUser?) {
        if(currentUser != null) {
            finish()
        }
    }

    private fun validateNameField() {
        if(nameTextInput.editText?.text.toString().isBlank()) {
            nameTextInput.editText?.error = getString(R.string.mandatory_default_field)
            nameTextInput?.requestFocus()
            return
        }
    }

    private fun validateEmailField() {
        if(emailTextInput.editText?.text.toString().isBlank()) {
            emailTextInput.editText?.error = getString(R.string.mandatory_default_field)
            emailTextInput.requestFocus()
            return
        }
        if(!Patterns.EMAIL_ADDRESS.matcher(emailTextInput.editText?.text.toString()).matches()) {
            emailTextInput.editText?.error = getString(R.string.mantaory_email)
            emailTextInput.requestFocus()
            return
        }
    }

    private fun validatePasswordField() {
        if(passwordTextinput.editText?.text.toString().isBlank()){
            passwordTextinput.editText?.error = getString(R.string.mandatory_default_field)
            passwordTextinput.requestFocus()
            return
        }
        if(passwordTextinput.editText?.text.toString().count() < 6) {
            passwordTextinput.editText?.error = getString(R.string.mandatory_password)
            passwordTextinput.requestFocus()
            return
        }
    }
}
