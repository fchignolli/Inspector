package com.example.inspector.Controller

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.example.inspector.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class HomeActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private val TAG = "LoginActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        auth = FirebaseAuth.getInstance()
    }

    override fun onStart() {
        super.onStart()
        updateUI(auth.currentUser)
    }

    fun onClickSingOut(view: View) {
        auth.signOut()
        updateUI(auth.currentUser)
    }

    fun updateUI(currentUser: FirebaseUser?) {
        if(currentUser == null) {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }
    }
}
