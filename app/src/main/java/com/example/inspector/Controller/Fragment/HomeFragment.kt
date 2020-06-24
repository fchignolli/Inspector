package com.example.inspector.Controller.Fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.inspector.R
import com.google.firebase.auth.FirebaseAuth

class HomeFragment : Fragment() {
    private lateinit var auth: FirebaseAuth
    private val TAG = "HomeFragment"

    override fun onCreateView (
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_home, container, false)
        val textView: TextView = root.findViewById(R.id.text_home)
        val button: Button = root.findViewById(R.id.button2)

        button.setOnClickListener( {
            print("oi")
        })
        textView.text = "Home"
        auth = FirebaseAuth.getInstance()
        return root
    }

    fun onClickSingOut(view: View) {
        auth.signOut()
        //setupUI(auth.currentUser)
    }
}
