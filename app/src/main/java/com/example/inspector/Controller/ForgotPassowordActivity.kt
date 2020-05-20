package com.example.inspector.Controller

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import com.example.inspector.R
import com.example.inspector.Utils.Utils
import com.example.inspector.Utils.Validator
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_forgot_passoword.*

class ForgotPassowordActivity : AppCompatActivity() {
    private val TAG = "ForgotPassowordActivity"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgot_passoword)
    }

    fun onClickSend(view: View) {
        if  (Validator.isEmptyField(emailTextInput) ||
            !Validator.isEmailPatters(emailTextInput)) {
            Log.d(TAG, "Some field was not filled in correctly")
            return
        } else {
            val email = emailTextInput.editText?.text.toString()
            sendPasswordReset(email)
        }
    }

    fun sendPasswordReset(email: String) {
        FirebaseAuth.getInstance().sendPasswordResetEmail(email)
            .addOnCompleteListener(this) { task ->
                if(task.isSuccessful) {
                    Log.d(TAG, "sendPasswordResetEmail:success")
                    // trocar para alert dialog
                    Utils.alert("Email enviado com sucesso.")
                    //finish()
                } else {
                    Log.w(TAG, "sendPasswordResetEmail:failure")
                }

            }
    }
}
