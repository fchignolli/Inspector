package com.example.inspector.Controller.Activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.inspector.R
import com.example.inspector.Utils.Utils
import com.example.inspector.Utils.Validator
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_forgot_password.*
import kotlinx.android.synthetic.main.activity_forgot_password.emailTextInput
import kotlinx.android.synthetic.main.activity_register.*

class ForgotPasswordActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgot_password)

        sendButton.setOnClickListener {
            if(Validator.isEmptyField(emailTextInput) || !Validator.isEmailPatters(emailTextInput)) {
                return@setOnClickListener
            } else {
                val emailText = emailTextInput.editText?.text.toString()
                FirebaseAuth.getInstance().sendPasswordResetEmail(emailText)
                    .addOnCompleteListener {
                        Utils.alert("E-mail para troca de senha enviado com sucesso!")
                        finish()
                    }
            }
        }

    }

}
