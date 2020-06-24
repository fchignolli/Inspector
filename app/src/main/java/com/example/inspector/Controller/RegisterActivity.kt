package com.example.inspector.Controller

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.view.View
import com.example.inspector.R
import com.example.inspector.Utils.Utils
import com.example.inspector.Utils.Validator
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.UserProfileChangeRequest
import kotlinx.android.synthetic.main.activity_register.*

class RegisterActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private val TAG = "RegisterActivity"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        auth = FirebaseAuth.getInstance()
    }

    fun onClickRegiter(view: View) {
        if(Validator.isEmptyField(nameTextInput) ||
            Validator.isEmptyField(emailTextInput) ||
            !Validator.isEmailPatters(emailTextInput) ||
            Validator.isEmptyField(passwordTexIinput) ||
            Validator.isCorrectSize(passwordTexIinput, 6) ||
            Validator.isEmptyField(confirmPassTextInput) ||
            !Validator.isEqual(passwordTexIinput, confirmPassTextInput)) {
            return
        } else {
            val name = nameTextInput.editText?.text.toString()
            val email = emailTextInput.editText?.text.toString()
            val password = passwordTexIinput.editText?.text.toString()
            register(name, email, password)
        }
    }

    private fun register(name: String, email: String, password: String) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    Log.d(TAG, "createUserWithEmail:success")
                    Utils.alert("Registrado com sucesso.")
                    updateUI(auth.currentUser)
                } else {
                    Log.w(TAG, "createUserWithEmail:failure", task.exception)
                    Utils.alert("O Registro falhou tente novamente.")
                    updateUI(null)
                }
            }
    }

    private fun updateUser(currentUser: FirebaseUser) {
        val name = nameTextInput.editText?.text.toString()
        val profileUpdates = UserProfileChangeRequest.Builder()
            .setDisplayName(name)
            .setPhotoUri(Uri.parse("https://cdn1.iconfinder.com/data/icons/user-pictures/100/unknown-512.png"))
            .build()

        currentUser.updateProfile(profileUpdates)
            .addOnCompleteListener { task ->
                if(task.isSuccessful) {
                    Log.d(TAG, "Perfil do usuário atualizado com sucesso!")
                }

        }
    }

    private fun updateUI(currentUser: FirebaseUser?) {
        if(currentUser != null) {
            updateUser(currentUser)
            finish()
        }
    }
}
