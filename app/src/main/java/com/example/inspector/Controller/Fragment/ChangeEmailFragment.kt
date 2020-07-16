package com.example.inspector.Controller.Fragment

import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import com.example.inspector.Controller.Activity.MainActivity
import com.example.inspector.R
import com.example.inspector.Utils.Utils
import com.example.inspector.Utils.Validator
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth

/**
 * Created by Lucas Alves dos Santos on 24/06/2020.
 * lucas.alves0828@gmail.com
 * {@see more in https://github.com/lucasalves08}
 */
class ChangeEmailFragment: Fragment() {
    private lateinit var auth: FirebaseAuth
    private val TAG = "ChangeEmailFragment"
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_change_email, container, false)
        auth = FirebaseAuth.getInstance()
        val emailTextLayout: TextInputLayout = root.findViewById(R.id.emailTextLayoutCEF)
        val newEmailTextLayout: TextInputLayout = root.findViewById(R.id.newEmailTextLayoutCEF)
        val passwordLayout: TextInputLayout = root.findViewById(R.id.passwordTextLayoutCEF)
        val button: Button = root.findViewById(R.id.sendButtonCEF)

        button.setOnClickListener {
            if(Validator.isEmptyField(emailTextLayout) ||
                !Validator.isEmailPatters(emailTextLayout) ||
                Validator.isEmptyField(newEmailTextLayout) ||
                !Validator.isEmailPatters(newEmailTextLayout) ||
                Validator.isEmptyField(passwordLayout) ||
                Validator.isCorrectSize(passwordLayout, 6) ) {
                return@setOnClickListener
            } else {
                val email = emailTextLayout.editText?.text.toString()
                val newEmail = newEmailTextLayout.editText?.text.toString()
                val password = passwordLayout.editText?.text.toString()

                val user = auth.currentUser ?: return@setOnClickListener
                val credential = EmailAuthProvider.getCredential(email, password)
                user.reauthenticate(credential)
                    .addOnCompleteListener {
                        Log.d(TAG, "User re-authenticated.");
                        val currentUser = auth.currentUser
                        currentUser!!.updateEmail(newEmail)
                            .addOnCompleteListener {
                                if(it.isSuccessful) {
                                    Utils.alert("Email atualizado com sucesso")
                                    val alertBuilder = AlertDialog.Builder(context)
                                    alertBuilder.setMessage("Por favor faça o login novamente")
                                        .setPositiveButton(R.string.ok) {_, _ ->
                                            auth.signOut()
                                            activity?.supportFragmentManager?.popBackStack()
                                            (activity as MainActivity).setupUI(auth.currentUser)
                                        }
                                    alertBuilder.show()
                                }
                            }
                    }
            }

        }
        return root
    }
}