package com.example.inspector.Controller

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import com.example.inspector.R
import com.example.inspector.Utils.Utils
import com.example.inspector.Utils.Validator
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private val TAG = "LoginActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        auth = FirebaseAuth.getInstance()
    }

    override fun onStart() {
        super.onStart()
        val currentUser = auth.currentUser
        updateUI(currentUser)
    }

    fun onClickRegister(view: View) {
        startActivity(Intent(this, RegisterActivity::class.java))
    }

    fun onClickForgotPassword(view: View) {
//        startActivity(Intent(this, ForgotPassowordActivity::class.java))
    }

    fun onClickLogin(view: View) {
        if (Validator.isEmptyField(emailTextInput) ||
            !Validator.isEmailPatters(emailTextInput) ||
            Validator.isEmptyField(passwordTextInput) ||
            Validator.isCorrectSize(passwordTextInput, 6)) {
            Log.d(TAG, "Some field was not filled in correctly")
            return
        } else {
            val email = emailTextInput.editText?.text.toString()
            val password = passwordTextInput.editText?.text.toString()
            login(email, password)
        }

    }

    private fun login(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG, "signInWithEmail:success")
                    updateUI(auth.currentUser)
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(TAG, "signInWithEmail:failure", task.exception)
                    Utils.alert("Authentication failed.")
                    updateUI(null)
                }
            }
    }

    private fun updateUI(currentUser: FirebaseUser?) {
        if (currentUser != null) {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
    }


}
